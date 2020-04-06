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
public enum PERMISSION implements BaseEnum{
  NONE("PERMISSION_NONE"),
  READ("PERMISSION_READ"),
  WRITE("PERMISSION_WRITE"),
  ADMINISTRATE("PERMISSION_ADMINISTRATE");

  private final String value;

  private PERMISSION(String value){
    this.value = value;
  }

  @Override
  public String getValue(){
    return value;
  }

  public static PERMISSION fromValue(String value){
    for(PERMISSION uPermission : values()){
      if(uPermission.value.equals(value)){
        return uPermission;
      }
    }
    throw new IllegalArgumentException("Value argument '" + value + " has no matching PERMISSION.");
  }

  @Override
  public String toString(){
    return value;
  }

  public boolean atLeast(PERMISSION permission){
    return this.ordinal() >= permission.ordinal();
  }

}
