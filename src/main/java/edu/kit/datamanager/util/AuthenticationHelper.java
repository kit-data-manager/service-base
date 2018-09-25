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
package edu.kit.datamanager.util;

import edu.kit.datamanager.entities.PERMISSION;
import edu.kit.datamanager.security.filter.JwtAuthenticationToken;
import edu.kit.datamanager.security.filter.JwtTemporaryToken;
import edu.kit.datamanager.security.filter.JwtUserToken;
import edu.kit.datamanager.security.filter.ScopedPermission;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 *
 * @author jejkal
 */
public class AuthenticationHelper{

  private AuthenticationHelper(){
  }

  /**
   * Get the authentication object from the current SecurityContextHolder.
   *
   * @return The authentication object.
   */
  public static Authentication getAuthentication(){
    return SecurityContextHolder.getContext().getAuthentication();
  }

  /**
   * Check if the current authentication has the provided authority.
   *
   * @param authority The authority to check for.
   *
   * @return TRUE if the authentication object has the provided authority, FALSE
   * otherwise.
   */
  public static boolean hasAuthority(final String authority){
    return getAuthentication().getAuthorities().stream().filter(a -> a.getAuthority().equals(authority)).count() > 0;
  }

  /**
   * Get the firstname attribute from the current authentication object. This
   * method required the authentication object to be of type
   * JwtAuthenticationToken and the token to be a USER token. In all other
   * cases, 'null' is returned.
   *
   * @return The firstname attribute or null.
   */
  public static String getFirstname(){
    if(getAuthentication() instanceof JwtUserToken){
      return ((JwtUserToken) getAuthentication()).getFirstname();
    }
    return null;
  }

  /**
   * Get the lastname attribute from the current authentication object. This
   * method required the authentication object to be of type
   * JwtAuthenticationToken and the token to be a USER token. In all other
   * cases, 'null' is returned.
   *
   * @return The lastname attribute or null.
   */
  public static String getLastname(){
    if(getAuthentication() instanceof JwtUserToken){
      return ((JwtUserToken) getAuthentication()).getLastname();
    }
    return null;
  }

  /**
   * Get the principal from the current authentication object. Depending on the
   * authentication object, the principal is either a username or a servicename.
   *
   * @return The principal of the authentication object.
   */
  public static String getPrincipal(){
    return (String) getAuthentication().getPrincipal();
  }

  /**
   * Return a list of identities contained in the current authorization. The
   * list contains at least the principal itself, obtained via {@link #getPrincipal()
   * }. If the authentication object is of type JwtAuthenticationToken, the list
   * may also contain the current groupId.
   *
   * @return A list of identities.
   */
  public static List<String> getAuthorizationIdentities(){
    List<String> identifiers = new ArrayList<>();
    identifiers.add(getPrincipal());
    if(getAuthentication() instanceof JwtAuthenticationToken){
      identifiers.add(((JwtAuthenticationToken) getAuthentication()).getGroupId());
    }
    return identifiers;
  }

  /**
   * Check if the current authorization context matches the provided principal.
   *
   * @param principal The principal to check for.
   *
   * @return TRUE if the authorization has the provided principal, FALSE
   * otherwise.
   */
  public static boolean isPrincipal(String principal){
    Optional<String> oUserId = Optional.of(principal);
    return oUserId.isPresent() && oUserId.get().equals((String) getAuthentication().getPrincipal());
  }

  /**
   * Check if the current authorization context contains the provided identity.
   *
   * @param identity The identity to check for.
   *
   * @return TRUE if the authorization has the provided identity, FALSE
   * otherwise.
   */
  public static boolean hasIdentity(final String identity){
    return getAuthorizationIdentities().contains(identity);
  }

  /**
   * Returns TRUE if the current authentication belongs to principal
   * 'anonymousUser'.
   *
   * @return TRUE in case of anonymous access.
   */
  public static boolean isAnonymous(){
    return isPrincipal("anonymousUser");
  }

  /**
   * Get the permission of a scoped permission entry available in a
   * JwtTemporaryToken. In order to obtain a scoped permission for a certain
   * resource, its resourceType and resourceId must be provided. If the caller
   * has not authenticated using a temporary token, PERMISSION.NONE will be
   * returned. Otherwise, the resource permission will be returned if available.
   *
   * @param resourceType The resource type, typically the class name.
   * @param resourceId The unique resource identifier.
   *
   * @return The permission of the caller for the resource identifier by type
   * and id or NONE.
   */
  public static PERMISSION getScopedPermission(String resourceType, String resourceId){
    if(getAuthentication() instanceof JwtTemporaryToken){
      ScopedPermission[] scopedPermissions = ((JwtTemporaryToken) getAuthentication()).getScopedPermissions();
      for(ScopedPermission permission : scopedPermissions){
        if(permission.getResourceType().equals(resourceType) && permission.getResourceId().equals(resourceId)){
          return permission.getPermission();
        }
      }
    }
    return PERMISSION.NONE;
  }
}
