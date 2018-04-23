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
package edu.kit.datamanager.entities;

import edu.kit.datamanager.annotations.Searchable;
import edu.kit.datamanager.annotations.SecureUpdate;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.UUID;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import lombok.Data;

/**
 *
 * @author jejkal
 */
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "IdType")
@ApiModel(description = "An identifier for a resource.")
@Data
public class Identifier{

  public enum IDENTIFIER_TYPE{
    ARK("ARK"),
    AR_XIV("arXiv"),
    BIBCODE("bibcode"),
    DOI("DOI"),
    EAN_13("EAN13"),
    EISSN("EISSN"),
    HANDLE("Handle"),
    IGSN("IGSN"),
    ISBN("ISBN"),
    ISSN("ISSN"),
    ISTC("ISTC"),
    LISSN("LISSN"),
    LSID("LSID"),
    PMID("PMID"),
    PURL("PURL"),
    UPC("UPC"),
    URL("URL"),
    URN("URN"),
    INTERNAL("INTERNAL"),
    OTHER("OTHER");

    private final String value;

    private IDENTIFIER_TYPE(String value){
      this.value = value;
    }

    public String getValue(){
      return value;
    }
  }

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @SecureUpdate({"FORBIDDEN"})
  @Searchable
  private Long id;
  @ApiModelProperty(value = "10.1234/foo", dataType = "String", required = true)
  private String value;
  @ApiModelProperty(value = "Controlled vocabulary, e.g. INTERNAL or DOI.", required = true)
  @Enumerated(EnumType.STRING)
  private IDENTIFIER_TYPE identifierType;

  public static Identifier factoryInternalIdentifier(String identifier){
    Identifier result = new Identifier();
    result.setIdentifierType(IDENTIFIER_TYPE.INTERNAL);
    result.setValue(identifier);
    return result;
  }

  public static Identifier factoryInternalIdentifier(){
    return factoryInternalIdentifier(UUID.randomUUID().toString());
  }
}
