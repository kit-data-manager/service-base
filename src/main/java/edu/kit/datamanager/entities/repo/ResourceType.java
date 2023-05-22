/*
 * Copyright 2017 Karlsruhe Institute of Technology.
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
package edu.kit.datamanager.entities.repo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import edu.kit.datamanager.entities.BaseEnum;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import lombok.Data;

/**
 *
 * @author jejkal
 */
@Data
@XmlAccessorType(XmlAccessType.FIELD)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ResourceType{

  public enum TYPE_GENERAL implements BaseEnum{

    AUDIOVISUAL("Audiovisual"),
    COLLECTION("Collection"),
    DATASET("Dataset"),
    EVENT("Event"),
    IMAGE("Image"),
    INTERACTIVE_RESOURCE("InteractiveResource"),
    MODEL("Model"),
    PHYSICAL_OBJECT("PhysicalObject"),
    SERVICE("Service"),
    SOFTWARE("Software"),
    SOUND("Sound"),
    TEXT("Text"),
    WORKFLOW("Workflow"),
    OTHER("Other");

    private final String value;

    private TYPE_GENERAL(String value){
      this.value = value;
    }

    @Override
    public String getValue(){
      return value;
    }

  }
  private long id;
  private String value;
  //vocab, e.g. Dataset, Image....
  private TYPE_GENERAL typeGeneral;

  public static ResourceType createResourceType(String value){
    return createResourceType(value, TYPE_GENERAL.DATASET);
  }

  public static ResourceType createResourceType(String value, TYPE_GENERAL typeGeneral){
    ResourceType type = new ResourceType();
    type.value = value;
    type.typeGeneral = typeGeneral;
    return type;
  }
}
