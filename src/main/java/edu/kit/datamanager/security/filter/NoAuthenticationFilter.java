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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.kit.datamanager.entities.RepoServiceRole;
import edu.kit.datamanager.exceptions.InvalidAuthenticationException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.DefaultClaims;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 *
 * @author jejkal
 */
public class NoAuthenticationFilter extends OncePerRequestFilter{

  private final String secretKey;
  private final AuthenticationManager authenticationManager;

  public NoAuthenticationFilter(String secretKey, AuthenticationManager authenticationManager){
    this.secretKey = secretKey;
    this.authenticationManager = authenticationManager;
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException, AuthenticationException{
    Claims claims = new DefaultClaims();
    claims.put("groupid", "USERS");
    claims.put("tokenType", JwtAuthenticationToken.TOKEN_TYPE.SERVICE.toString());
    claims.put("servicename", JwtServiceToken.SELF_SERVICE_NAME);

    Set<String> rolesAsString = new HashSet<>();
    rolesAsString.add(RepoServiceRole.SERVICE_WRITE.getValue());
    try{
      claims.put("roles", new ObjectMapper().writeValueAsString(rolesAsString.toArray(new String[]{})));
    } catch(JsonProcessingException ex){
      throw new InvalidAuthenticationException("Failed to create JWToken.", ex);
    }
    Set<Map.Entry<String, Object>> claimEntries = claims.entrySet();
    Map<String, Object> claimMap = new HashMap<>();
    claimEntries.forEach((entry) -> {
      claimMap.put(entry.getKey(), entry.getValue());
    });

    String token = Jwts.builder().setClaims(claims).setExpiration(DateUtils.addHours(new Date(), 1)).signWith(SignatureAlgorithm.HS256, secretKey).compact();
    JwtAuthenticationToken res = JwtAuthenticationToken.factoryToken(token, claimMap);
    SecurityContextHolder.getContext().setAuthentication(res);
    //continue filtering
    chain.doFilter(request, response);

  }
}
