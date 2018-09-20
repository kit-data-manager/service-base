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

import java.util.Collection;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;

/**
 *
 * @author jejkal
 */
public class JwtAuthenticationToken extends AbstractAuthenticationToken{

  public enum TOKEN_SCOPE{
    USER,
    SERVICE;

    public static TOKEN_SCOPE fromString(String value){
      JwtAuthenticationToken.TOKEN_SCOPE result = JwtAuthenticationToken.TOKEN_SCOPE.USER;
      try{
        if(value != null){
          result = JwtAuthenticationToken.TOKEN_SCOPE.valueOf(value);
        }
      } catch(IllegalArgumentException ex){
        //ignore wrong scope
      }
      return result;
    }
  }

  public static final String NOT_AVAILABLE = "N/A";
  private TOKEN_SCOPE scope;
  private String servicename;
  private String username;
  private String firstname;
  private String lastname;
  private String email;
  private String groupId;
  private final String token;

  public JwtAuthenticationToken(String token){
    super(AuthorityUtils.NO_AUTHORITIES);
    this.token = token;
  }

  public static JwtAuthenticationToken createUserToken(Collection<? extends GrantedAuthority> authorities, String username, String firstname, String lastname, String email, String groupId, String token){
    JwtAuthenticationToken result = new JwtAuthenticationToken(authorities, username, firstname, lastname, email, groupId, token);
    result.scope = TOKEN_SCOPE.USER;
    return result;
  }

  public static JwtAuthenticationToken createServiceToken(Collection<? extends GrantedAuthority> authorities, String servicename, String groupId, String token){
    JwtAuthenticationToken result = new JwtAuthenticationToken(authorities, servicename, groupId, token);
    result.scope = TOKEN_SCOPE.SERVICE;
    return result;
  }

  JwtAuthenticationToken(Collection<? extends GrantedAuthority> authorities, String servicename, String groupId, String token){
    super(authorities);
    this.token = token;
    this.servicename = servicename;
    setGroupId(groupId);
    setAuthenticated(true);
  }

  JwtAuthenticationToken(Collection<? extends GrantedAuthority> authorities, String username, String firstname, String lastname, String email, String groupId, String token){
    super(authorities);
    this.token = token;
    this.username = username;
    this.firstname = firstname;
    this.lastname = lastname;
    this.email = email;
    setGroupId(groupId);
    setAuthenticated(true);
  }

  public TOKEN_SCOPE getScope(){
    return scope;
  }

  public String getServicename(){
    return servicename;
  }

  @Override
  public Object getCredentials(){
    return NOT_AVAILABLE;
  }

  @Override
  public Object getPrincipal(){
    if(scope == null){
      //assume user token by default
      return username;
    }
    switch(scope){
      case SERVICE:
        return servicename;
      default:
        return username;
    }
  }

  public String getToken(){
    return token;
  }

  public String getEmail(){
    return email;
  }

  public String getGroupId(){
    return groupId;
  }

  public void setGroupId(String groupId){
    this.groupId = groupId;
  }

  public String getFirstname(){
    return firstname;
  }

  public String getLastname(){
    return lastname;
  }

  @Override
  public final void setAuthenticated(boolean authenticated){
    super.setAuthenticated(authenticated);
  }

}
