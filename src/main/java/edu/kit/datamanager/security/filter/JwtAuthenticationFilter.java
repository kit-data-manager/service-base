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
import edu.kit.datamanager.exceptions.UnauthorizedAccessException;
import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 *
 * @author jejkal
 */
public class JwtAuthenticationFilter extends OncePerRequestFilter{

  public final static String AUTHORIZATION_HEADER = "Authorization";
  public final static String BEARER_TOKEN_IDENTIFIER = "Bearer ";
  private final AuthenticationManager authenticationManager;

  public JwtAuthenticationFilter(AuthenticationManager authenticationManager){
    this.authenticationManager = authenticationManager;
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException, AuthenticationException{
    //obtain authentication token from header
    String authToken = request.getHeader(AUTHORIZATION_HEADER);
    //check if token exists and is BEARER token
    if(authToken == null || !authToken.startsWith(BEARER_TOKEN_IDENTIFIER)){
      //not token or not a bearer token, continue with authentication chain
      chain.doFilter(request, response);
    } else{
      //found BEARER token in header, try JWTAuthentication
      try{
        Authentication authentication = authenticationManager.authenticate(new JwtEmptyToken(authToken.substring(BEARER_TOKEN_IDENTIFIER.length())));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        //continue filtering
        chain.doFilter(request, response);
      } catch(NoJwtTokenException e){
        //No valid JWToken, continue with filter chain
        chain.doFilter(request, response);
      } catch(InvalidAuthenticationException | UnauthorizedAccessException e){
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.getWriter().write(e.getLocalizedMessage());
      }
    }
  }
}
