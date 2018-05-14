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

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.kit.datamanager.service.exceptions.CustomInternalServerError;
import edu.kit.datamanager.util.JsonMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
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
    return getJwtAuthentication(((JwtAuthenticationToken) authentication).getToken());
  }

  @SuppressWarnings("unchecked")
  public Authentication getJwtAuthentication(String token){
    Jws<Claims> claimsJws = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
    List<String> rolesList = new ArrayList<>();
    String roleClaim = claimsJws.getBody().get("roles", String.class);
    if(roleClaim == null){
      LOGGER.error("No 'roles' claim found in JWT " + claimsJws);
      throw new CustomInternalServerError("Unprocessable authentication token.");
    }

    try{
      final JsonNode jsonNode = new ObjectMapper().readTree(roleClaim);
      if(jsonNode.isArray()){
        for(JsonNode node : jsonNode){
          String role = node.asText();
          rolesList.add(role);
        }
      } else{
        throw new IllegalArgumentException("Roles claim '" + roleClaim + "' seems to be no JSON array.");
      }
    } catch(IOException | IllegalArgumentException ex){
      LOGGER.error("Failed to read user roles from " + this, ex);
      throw new CustomInternalServerError("Failed to read user roles.");
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
  }

  public List<SimpleGrantedAuthority> grantedAuthorities(Set<String> roles){
    return roles.stream().map(String::toString).map(SimpleGrantedAuthority::new).collect(toList());
  }
}
