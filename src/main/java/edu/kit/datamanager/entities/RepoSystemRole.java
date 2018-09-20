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
package edu.kit.datamanager.entities;

import static edu.kit.datamanager.entities.RepoUserRole.values;

/**
 *
 * @author jejkal
 */
public enum RepoSystemRole{
  SYSTEM_READ("ROLE_SYSTEM_READ"),
  SYSTEM_WRITE("ROLE_SYSTEM_WRITE"),
  SYSTEM_ADMINISTRATR("ROLE_SYSTEM_ADMINISTRATE");

  private final String value;

  RepoSystemRole(String role){
    this.value = role;
  }

  public String getValue(){
    return value;
  }

  @Override
  public String toString(){
    return value;
  }

  public static RepoSystemRole fromValue(String value){
    for(RepoSystemRole uRole : values()){
      if(uRole.value.equals(value)){
        return uRole;
      }
    }
    throw new IllegalArgumentException("Value argument '" + value + " has no matching SystemRole.");
  }
}
