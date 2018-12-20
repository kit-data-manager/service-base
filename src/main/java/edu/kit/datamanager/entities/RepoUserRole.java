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

/**
 *
 * @author jejkal
 */
public enum RepoUserRole implements RepoRole{
  INACTIVE("ROLE_INACTIVE"),
  GUEST("ROLE_GUEST"),
  USER("ROLE_USER"),
  ADMINISTRATOR("ROLE_ADMINISTRATOR"),
  CURATOR("ROLE_CURATOR");

  private final String value;

  RepoUserRole(String role){
    this.value = role;
  }

  @Override
  public String getValue(){
    return value;
  }

  @Override
  public String toString(){
    return value;
  }

  public static RepoUserRole fromValue(String value){
    for(RepoUserRole uRole : values()){
      if(uRole.value.equals(value)){
        return uRole;
      }
    }
    throw new IllegalArgumentException("Value argument '" + value + " has no matching UserRole.");
  }

  public boolean atLeast(RepoUserRole otherRole){
    return this.ordinal() >= otherRole.ordinal();
  }
}
