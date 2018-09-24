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

import edu.kit.datamanager.entities.RepoUserRole;
import edu.kit.datamanager.exceptions.InvalidAuthenticationException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import static java.util.stream.Collectors.toList;
import org.apache.commons.collections4.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

/**
 *
 * @author jejkal
 */
public abstract class JwtAuthenticationToken extends AbstractAuthenticationToken{

  private final static Logger LOGGER = LoggerFactory.getLogger(JwtAuthenticationToken.class);

  public enum TOKEN_TYPE{
    USER,
    SERVICE,
    TEMPORARY;

    public static TOKEN_TYPE fromString(String value){
      JwtAuthenticationToken.TOKEN_TYPE result = JwtAuthenticationToken.TOKEN_TYPE.USER;
      try{
        if(value != null){
          result = JwtAuthenticationToken.TOKEN_TYPE.valueOf(value);
        }
      } catch(IllegalArgumentException ex){
        //ignore wrong scope
      }
      return result;
    }
  }

  public static final String NOT_AVAILABLE = "N/A";

  private String principalName;
  private String groupId;
  private final String token;

  JwtAuthenticationToken(String token){
    super(AuthorityUtils.NO_AUTHORITIES);
    this.token = token;
  }

  public JwtAuthenticationToken(String token, Collection<? extends GrantedAuthority> authorities){
    super(authorities);
    this.token = token;
  }

  JwtAuthenticationToken(String principalName, String groupId, String token, Collection<? extends GrantedAuthority> authorities){
    super(authorities);
    this.principalName = principalName;
    setGroupId(groupId);
    this.token = token;
  }

  public static JwtAuthenticationToken factoryToken(String token){
    return new JwtEmptyToken(token);
  }

  public static JwtAuthenticationToken factoryToken(String token, Map<String, Object> claims){
    String type = MapUtils.getString(claims, "tokenType");
    List<String> rolesList = (List<String>) MapUtils.getObject(claims, "roles");
    if(rolesList == null){
      LOGGER.error("No 'roles' claim found in JWT " + claims + ". Using ROLE_GUEST as default.");
      rolesList = new ArrayList<>();
      rolesList.add(RepoUserRole.GUEST.getValue());
    }

    List<SimpleGrantedAuthority> grantedAuthorities = grantedAuthorities((Set<String>) new HashSet<>(rolesList));

    JwtAuthenticationToken jwToken = null;

    switch(JwtAuthenticationToken.TOKEN_TYPE.fromString(type)){
      case USER:
        jwToken = new JwtUserToken(token, grantedAuthorities);
        break;
      case SERVICE:
        jwToken = new JwtServiceToken(token, grantedAuthorities);
        break;
      case TEMPORARY:
        jwToken = new JwtTemporaryToken(token, grantedAuthorities);
    }

    if(jwToken == null){
      //as long as no additional types are added, we'll never arrive here
      throw new InvalidAuthenticationException("JWTokens of type " + type + " are currently not supported.");
    }

    for(String claim : jwToken.getSupportedClaims()){
      Object value = MapUtils.getObject(claims, claim);
      Class c = jwToken.getClassForClaim(claim);

      if(value != null && !c.isInstance(value)){
        throw new InvalidAuthenticationException("Claim " + claim + " is invalid. Expected type " + c);
      }
      jwToken.setValueFromClaim(claim, value);
    }

    jwToken.validateToken();

    jwToken.setAuthenticated(true);
    return jwToken;
  }

  public static List<SimpleGrantedAuthority> grantedAuthorities(Set<String> roles){
    if(null == roles){
      return new ArrayList<>();
    }
    return roles.stream().map(String::toString).map(SimpleGrantedAuthority::new).collect(toList());
  }

  public abstract String[] getSupportedClaims();

  public abstract Class getClassForClaim(String claim);

  public abstract void setValueFromClaim(String claim, Object value);

  public void validateToken(){
    if(getPrincipal() == null){
      throw new InvalidAuthenticationException("Token validatation failed. No principal assigned.");
    }

    validate();
  }

  public abstract void validate() throws InvalidAuthenticationException;

  public abstract TOKEN_TYPE getTokenType();

  void setPrincipalName(String principalName){
    this.principalName = principalName;
  }

  @Override
  public Object getCredentials(){
    return NOT_AVAILABLE;
  }

  @Override
  public final Object getPrincipal(){
    return principalName;
  }

  public final String getToken(){
    return token;
  }

  public String getGroupId(){
    return groupId;
  }

  public void setGroupId(String groupId){
    this.groupId = groupId;
  }

  @Override
  public final void setAuthenticated(boolean authenticated){
    super.setAuthenticated(authenticated);
  }

}
