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

import java.util.Optional;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 *
 * @author jejkal
 */
public class AuthenticationHelper{

  public static Authentication getAuthentication(){
    return SecurityContextHolder.getContext().getAuthentication();
  }

  public static boolean hasAuthority(final String authority){
    return getAuthentication().getAuthorities().stream().filter(a -> a.getAuthority().equals(authority)).count() > 0;
  }

  public static boolean isUser(String userId){
    Optional<String> oUserId = Optional.of(userId);
    return oUserId.isPresent() && oUserId.get().equals((String) getAuthentication().getPrincipal());
  }

  public static boolean isAnonymous(){
    return isUser("anonymousUser");
  }
}
