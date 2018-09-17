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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import edu.kit.datamanager.annotations.SecureUpdate;
import edu.kit.datamanager.exceptions.PatchApplicationException;
import edu.kit.datamanager.exceptions.UpdateForbiddenException;
import edu.kit.datamanager.exceptions.CustomInternalServerError;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;

/**
 *
 * @author jejkal
 */
public class PatchUtil{

  private static final Logger LOGGER = LoggerFactory.getLogger(PatchUtil.class);

  private PatchUtil(){
  }

  public static <C> C applyPatch(C resource, JsonPatch patch, Class<C> resourceClass, Collection<? extends GrantedAuthority> authorities){
    ObjectMapper tmpObjectMapper = new ObjectMapper();
    tmpObjectMapper.registerModule(new JavaTimeModule());
    JsonNode resourceAsNode = tmpObjectMapper.convertValue(resource, JsonNode.class);
    C updated;
    try{
      // Apply the patch
      JsonNode patchedDataResourceAsNode = patch.apply(resourceAsNode);
      //convert resource back to POJO
      updated = tmpObjectMapper.treeToValue(patchedDataResourceAsNode, resourceClass);
    } catch(JsonPatchException | JsonProcessingException ex){
      LOGGER.error("Failed to apply patch '" + patch.toString() + " to resource " + resource, ex);
      throw new PatchApplicationException("Failed to apply patch to resource.");
    }

    if(!PatchUtil.canUpdate(resource, updated, authorities)){
      throw new UpdateForbiddenException("Patch not applicable.");
    }

    return updated;
  }

  private static boolean canUpdate(Object originalObj, Object patched, Collection<? extends GrantedAuthority> authorities){
    for(Field field : patched.getClass().getDeclaredFields()){
      SecureUpdate secureUpdate = field.getAnnotation(SecureUpdate.class);
      if(secureUpdate != null){
        try{
          field.setAccessible(true);
          Object persistedField = field.get(patched);
          Object originalField = field.get(originalObj);
          String[] allowedRoles = secureUpdate.value();

          if(!Objects.equals(persistedField, originalField)){
            boolean canUpdate = false;
            for(String role : allowedRoles){//go though all roles allowed to update
              for(GrantedAuthority authority : authorities){//check owned authorities
                if(authority.getAuthority().equalsIgnoreCase(role)){//the current authority allows to update, proceed to next field
                  canUpdate = true;
                  break;
                }
              }
              if(canUpdate){
                //this field can be updated
                break;
              }
            }
            if(!canUpdate){
              //at least one field cannot be updated
              LOGGER.warn("Patching of field " + field + " is allowed by " + Arrays.asList(allowedRoles) + ", but caller only offered the following authorities: " + authorities + ".");
              return false;
            }
          }
        } catch(IllegalAccessException | IllegalArgumentException | SecurityException e){
          LOGGER.error("Failed to check patch applicability.", e);
          throw new CustomInternalServerError("Unable to check if patch is applicable. Message: " + e.getMessage());
        }
      }
    }

    return true;
  }

}
