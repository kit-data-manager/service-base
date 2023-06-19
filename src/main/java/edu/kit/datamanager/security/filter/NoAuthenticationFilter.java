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
import com.nimbusds.jose.util.StandardCharset;
import edu.kit.datamanager.entities.RepoServiceRole;
import edu.kit.datamanager.exceptions.InvalidAuthenticationException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.impl.DefaultClaims;
import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.security.Key;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import javax.crypto.spec.SecretKeySpec;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * Filter adding security context if no authorization is available. 
 * 
 * User has role 'ROLE_ADMINISTRATOR' and username 'SELF'.
 * 
 * @author jejkal
 */
public class NoAuthenticationFilter extends OncePerRequestFilter {

  private final String secretKey;
  private final AuthenticationManager authenticationManager;

  private static final String USERS_GROUP = "USERS";

  public NoAuthenticationFilter(String secretKey, AuthenticationManager authenticationManager) {
    this.secretKey = secretKey;
    this.authenticationManager = authenticationManager;
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException, AuthenticationException {
    Claims claims = new DefaultClaims();
    claims.put(JwtAuthenticationToken.GROUPS_CLAIM, Arrays.asList(USERS_GROUP));
    claims.put(JwtAuthenticationToken.TOKENTYPE_CLAIM, JwtAuthenticationToken.TOKEN_TYPE.SERVICE.toString());
    claims.put(JwtAuthenticationToken.SERVICENAME_CLAIM, JwtServiceToken.SELF_SERVICE_NAME);

    Set<String> rolesAsString = new HashSet<>();
    rolesAsString.add(RepoServiceRole.SERVICE_WRITE.getValue());
    try {
      claims.put(JwtAuthenticationToken.ROLES_CLAIM, new ObjectMapper().writeValueAsString(rolesAsString.toArray(new String[]{})));
    } catch (JsonProcessingException ex) {
      throw new InvalidAuthenticationException("Failed to create JWToken.", ex);
    }
    Set<Map.Entry<String, Object>> claimEntries = claims.entrySet();
    Map<String, Object> claimMap = new HashMap<>();
    claimEntries.forEach(entry -> claimMap.put(entry.getKey(), entry.getValue()));

    Key key = new SecretKeySpec(secretKey.getBytes(StandardCharset.UTF_8), "HmacSHA256");
    String token = Jwts.builder().setClaims(claims).setExpiration(Date.from(Instant.now().plus(1l, ChronoUnit.HOURS))).signWith(key).compact();
    JwtAuthenticationToken res = JwtAuthenticationToken.factoryToken(token, claimMap);
    SecurityContextHolder.getContext().setAuthentication(res);
    //continue filtering
    chain.doFilter(request, response);

  }
}
