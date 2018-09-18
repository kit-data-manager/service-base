/*
 * Copyright 2018 Karlsruhe Institute of Technology.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package edu.kit.datamanager.security.filter;

import edu.kit.datamanager.exceptions.InvalidAuthenticationException;
import edu.kit.datamanager.util.JsonMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import static java.util.stream.Collectors.toList;
import org.slf4j.Logger;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

/**
 *
 * @author jejkal
 */
public class JwtAuthenticationProvider implements AuthenticationProvider, JsonMapper{

  private final Logger LOGGER;
  private final String secretKey;

  public JwtAuthenticationProvider(String secretKey, Logger logger){
    this.secretKey = secretKey;
    this.LOGGER = logger;
  }

  @Override
  public boolean supports(Class<?> authentication){
    return JwtAuthenticationToken.class.isAssignableFrom(authentication);
  }

  @Override
  public Authentication authenticate(Authentication authentication) throws AuthenticationException{
    if(null == authentication){
      throw new InvalidAuthenticationException("No authentication received. Aborting authentication.");
    }
    if(!(authentication instanceof JwtAuthenticationToken)){
      throw new InvalidAuthenticationException("Provided authentication is not supported. Expecting JwtAuthenticationToken, received " + authentication.getClass() + ".");
    }
    return getJwtAuthentication(((JwtAuthenticationToken) authentication).getToken());
  }

  @SuppressWarnings("unchecked")
  public Authentication getJwtAuthentication(String token) throws AuthenticationException{
    if(null == token){
      throw new InvalidAuthenticationException("No JWT token provided. Authentication aborted.");
    }
    try{
      Jws<Claims> claimsJws = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
      List<String> rolesList = claimsJws.getBody().get("roles", ArrayList.class);
      if(rolesList == null){
        LOGGER.error("No 'roles' claim found in JWT " + claimsJws);
        throw new InvalidAuthenticationException("Invalid authentication token.");
      }

      List<SimpleGrantedAuthority> grantedAuthorities = grantedAuthorities((Set<String>) new HashSet<>(rolesList));
      String username = claimsJws.getBody().get("username", String.class);
      String firstname = claimsJws.getBody().get("firstname", String.class);
      String lastname = claimsJws.getBody().get("lastname", String.class);
      String groupId = claimsJws.getBody().get("activeGroup", String.class);

      JwtAuthenticationToken jwtToken = new JwtAuthenticationToken(
              grantedAuthorities,
              username,
              firstname,
              lastname,
              groupId,
              token);
      return jwtToken;
    } catch(ExpiredJwtException ex){
      LOGGER.debug("Provided token has expired. Refresh of login required.", ex);
      throw new InvalidAuthenticationException("Your token has expired. Please refresh your login.");
    } catch(SignatureException ex){
      LOGGER.debug("Provided token has invalid signature. Secret key seems not to match.", ex);
      throw new InvalidAuthenticationException("Your token signature is invalid. Please check if the token issuer is trusted by the consumer.");
    } catch(MalformedJwtException ex){
      LOGGER.debug("Provided token is malformed.", ex);
      throw new InvalidAuthenticationException("Your token is malformed. Please reload the token from its source.");
    }
  }

  public List<SimpleGrantedAuthority> grantedAuthorities(Set<String> roles){
    if(null == roles){
      return new ArrayList<>();
    }
    return roles.stream().map(String::toString).map(SimpleGrantedAuthority::new).collect(toList());
  }
}
