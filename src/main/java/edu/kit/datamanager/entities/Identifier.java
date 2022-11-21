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
import edu.kit.datamanager.util.EnumUtils;
import java.util.Objects;
import java.util.UUID;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.validation.constraints.NotBlank;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import lombok.Data;

/**
 *
 * @author jejkal
 */
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Data
@XmlAccessorType(XmlAccessType.FIELD)
public class Identifier {

  public enum IDENTIFIER_TYPE implements BaseEnum {
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
    W_3_ID("w3id"),
    INTERNAL("INTERNAL"),
    OTHER("OTHER");

    private final String value;

    private IDENTIFIER_TYPE(String value) {
      this.value = value;
    }

    @Override
    public String getValue() {
      return value;
    }
  }

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @SecureUpdate({"FORBIDDEN"})
  @Searchable
  private Long id;
  @NotBlank
  private String value;
  // @NotBlank
  @Enumerated(EnumType.STRING)
  private IDENTIFIER_TYPE identifierType;

  public static Identifier factoryIdentifier(String value, IDENTIFIER_TYPE type) {
    Identifier result = new Identifier();
    result.value = value;
    result.identifierType = type;
    return result;
  }

  public static Identifier factoryInternalIdentifier(String identifier) {
    Identifier result = new Identifier();
    result.setIdentifierType(IDENTIFIER_TYPE.INTERNAL);
    result.setValue(identifier);
    return result;
  }

  public static Identifier factoryInternalIdentifier() {
    return factoryInternalIdentifier(UUID.randomUUID().toString());
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final Identifier other = (Identifier) obj;
    if (!Objects.equals(this.value, other.value)) {
      return false;
    }
    if (!Objects.equals(this.id, other.id)) {
      return false;
    }
    return EnumUtils.equals(this.identifierType, other.identifierType);
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 79 * hash + Objects.hashCode(this.id);
    hash = 79 * hash + Objects.hashCode(this.value);
    hash = 79 * hash + EnumUtils.hashCode(this.identifierType);
    return hash;
  }

}
