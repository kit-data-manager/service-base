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
package edu.kit.datamanager.entities.messaging;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.kit.datamanager.exceptions.MessageValidationException;
import java.io.IOException;
import java.util.Map;
import lombok.Data;
import org.joda.time.DateTimeZone;
import org.joda.time.Instant;

/**
 *
 * @author jejkal
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class BasicMessage implements IAMQPSubmittable{

  private String principal;
  private String sender;
  private long timestamp;

  private String entityId;
  private String action;
  @JsonInclude(Include.NON_NULL)
  private String subCategory;
  @JsonInclude(Include.NON_NULL)
  private Map<String, String> metadata;

  @JsonIgnore
  public String getEntityName(){
    return "generic";
  }

  @JsonIgnore
  @Override
  public final String getRoutingKey(){
    String routingKey = getEntityName().toLowerCase() + "." + getAction().toLowerCase();
    if(getSubCategory() != null){
      routingKey += "." + getSubCategory().toLowerCase();
    }
    return routingKey;
  }

  @Override
  public void validate(){
    if(getEntityName() == null){
      throw new MessageValidationException("Entity name must not be null.");
    }

    if(getAction() == null){
      throw new MessageValidationException("Action must not be null.");
    }

    if(getEntityId() == null){
      throw new MessageValidationException("Entity id must not be null.");
    }
  }

  public final void setCurrentTimestamp(){
    setTimestamp(Instant.now().toDateTime(DateTimeZone.UTC).getMillis());
  }

  public static BasicMessage fromJson(String jsonString) throws IOException{
    return new ObjectMapper().readValue(jsonString, BasicMessage.class);
  }
}
