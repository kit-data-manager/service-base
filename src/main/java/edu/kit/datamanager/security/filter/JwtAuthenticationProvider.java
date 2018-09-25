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
import edu.kit.datamanager.exceptions.NoJwtTokenException;
import edu.kit.datamanager.util.JsonMapper;
import edu.kit.datamanager.util.NetworkUtils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import static java.util.stream.Collectors.toList;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * Basic JWT authentication provider. The provider evaluates a JWToken provided
 * in the Authorization header as Bearer token. First of all, the token is
 * verified using the configured secret key. Afterwards, all contained claims
 * are extracted, e.g. roles, username, first-/lastname, email, and active
 * group. From this list, only the roles claim is mandatory. Finally, a
 * JwtAuthenticationToken is created and returned as authentication object.
 *
 * @author jejkal
 */
public class JwtAuthenticationProvider implements AuthenticationProvider, JsonMapper{

  private final Logger LOGGER;
  private final String secretKey;

  /**
   * Constructor for authentication provider for Jwt authentication.
   *
   * @param secretKey Key for token signing.
   * @param logger Logger instance.
   */
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
      throw new NoJwtTokenException("No JWToken provided. Authentication aborted.");
    }
    try{
      Jws<Claims> claimsJws = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
      Set<Entry<String, Object>> claims = claimsJws.getBody().entrySet();
      Map<String, Object> claimMap = new HashMap<>();
      claims.forEach((entry) -> {
        claimMap.put(entry.getKey(), entry.getValue());
      });
      JwtAuthenticationToken jwToken = JwtAuthenticationToken.factoryToken(token, claimMap);

      if(jwToken instanceof JwtServiceToken && ((JwtServiceToken) jwToken).getSources() != null){
        JwtServiceToken serviceToken = (JwtServiceToken) jwToken;
        LOGGER.debug("Performing source check for JWToken for service {} and sources {}.", serviceToken.getPrincipal(), Arrays.asList(serviceToken.getSources()));
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        String remoteAddr = request.getRemoteAddr();
        LOGGER.debug("Trying to match remote address {} with at least one allowed source.", remoteAddr);
        boolean matchFound = false;
        for(String source : serviceToken.getSources()){
          if(NetworkUtils.matches(remoteAddr, source)){
            matchFound = true;
            break;
          }
        }
        if(!matchFound){
          LOGGER.warn("Invalid request from remote address {} to service {} found. Request denied.", remoteAddr, serviceToken.getPrincipal());
          throw new InvalidAuthenticationException("You are not allowed to authenticate using the provided token from your current location.");
        }
      }
      return jwToken;
    } catch(ExpiredJwtException ex){
      LOGGER.debug("Provided token has expired. Refresh of login required.", ex);
      throw new InvalidAuthenticationException("Your token has expired. Please refresh your login.");
    } catch(SignatureException ex){
      LOGGER.debug("Provided token has invalid signature. Secret key seems not to match.", ex);
      throw new InvalidAuthenticationException("Your token signature is invalid. Please check if the token issuer is trusted by the consumer.");
    } catch(MalformedJwtException ex){
      LOGGER.debug("Provided token is malformed.", ex);
      throw new NoJwtTokenException("The provided token '" + token + "' seems not to be a JWToken.");
    }
  }

  /**
   * Convert a list of strings into a list of authorities.
   *
   * @param roles A set of role strings.
   *
   * @return A list of granted authorities.
   */
  public List<SimpleGrantedAuthority> convertRoleListToGrantedAuthorities(Set<String> roles){
    if(null == roles){
      return new ArrayList<>();
    }
    return roles.stream().map(String::toString).map(SimpleGrantedAuthority::new).collect(toList());
  }
}
