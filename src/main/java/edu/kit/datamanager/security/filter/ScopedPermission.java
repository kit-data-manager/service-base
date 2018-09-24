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
import edu.kit.datamanager.entities.PERMISSION;
import java.io.IOException;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author jejkal
 */
@Data
public class ScopedPermission{

  private final static Logger LOGGER = LoggerFactory.getLogger(ScopedPermission.class);
  private String resourceType;
  private String resourceId;
  private PERMISSION permission;

  private ScopedPermission(){
  }

  public static ScopedPermission factoryScopedPermission(Class resourceClass, String resourceId, PERMISSION permission){
    ScopedPermission result = new ScopedPermission();
    result.setResourceType(resourceClass.getSimpleName());
    result.setResourceId(resourceId);
    result.setPermission(permission);
    return result;
  }

  public static ScopedPermission factoryScopedPermission(String resourceType, String resourceId, PERMISSION permission){
    ScopedPermission result = new ScopedPermission();
    result.setResourceType(resourceType);
    result.setResourceId(resourceId);
    result.setPermission(permission);
    return result;
  }

  public static ScopedPermission factoryScopedPermission(String serialized){
    ObjectMapper mapper = new ObjectMapper();
    try{
      return mapper.readValue(serialized, ScopedPermission.class);
    } catch(IOException ex){
      LOGGER.error("Failed to factory scoped permission from serialized form " + serialized, ex);
      return null;
    }
  }

  public String toJson(){
    try{
      return new ObjectMapper().writeValueAsString(this);
    } catch(JsonProcessingException ex){
      LOGGER.error("Failed to serialize scoped permission " + this, ex);
      return null;
    }
  }

}
