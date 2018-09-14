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

  private String username;
  private String firstname;
  private String lastname;
  private String groupId;
  private final String token;

  public JwtAuthenticationToken(String token){
    super(AuthorityUtils.NO_AUTHORITIES);
    this.token = token;
  }

  public JwtAuthenticationToken(Collection<? extends GrantedAuthority> authorities, String username, String firstname, String lastname, String groupId, String token){
    super(authorities);
    this.token = token;
    this.username = username;
    this.firstname = firstname;
    this.lastname = lastname;
    this.groupId = groupId;
    setAuthenticated(true);
  }

  @Override
  public Object getCredentials(){
    return "N/A";
  }

  @Override
  public Object getPrincipal(){
    return username;
  }

  public String getToken(){
    return token;
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
