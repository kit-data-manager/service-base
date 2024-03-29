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
 * Entity for description.
 *
 * @author jejkal
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@XmlAccessorType(XmlAccessType.FIELD)
@SuppressWarnings("UnnecessarilyFullyQualified")
public class Description{

  public enum TYPE implements BaseEnum{
    ABSTRACT("Abstract"),
    METHODS("Methods"),
    SERIES_INFORMATION("SeriesInformation"),
    TABLE_OF_CONTENTS("TableOfContents"),
    TECHNICAL_INFO("TechnicalInfo"),
    OTHER("Other");

    private final String value;

    private TYPE(String value){
      this.value = value;
    }

    @Override
    public String getValue(){
      return value;
    }
  }

  private long id;
  private String description;
  //vocab, e.g. Abstract
  private TYPE type;
  private String lang;

}
