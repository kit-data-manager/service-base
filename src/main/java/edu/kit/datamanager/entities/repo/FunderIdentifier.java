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
package edu.kit.datamanager.entities.repo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import edu.kit.datamanager.entities.BaseEnum;
import edu.kit.datamanager.entities.Identifier;
import edu.kit.datamanager.entities.Identifier.IDENTIFIER_TYPE;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 *
 * @author jejkal
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@EqualsAndHashCode(callSuper = false)
@XmlAccessorType(XmlAccessType.FIELD)
public class FunderIdentifier{

  public enum FUNDER_IDENTIFIER_TYPE implements BaseEnum{
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

    private FUNDER_IDENTIFIER_TYPE(String value){
      this.value = value;
    }

    @Override
    public String getValue(){
      return value;
    }
  }

  public enum FUNDER_TYPE implements BaseEnum{
    ISNI("ISNI"),
    GRID("GRID"),
    CROSSREF_FUNDER_ID("Crossref Funder ID"),
    OTHER("Other");

    private final String value;

    private FUNDER_TYPE(String value){
      this.value = value;
    }

    @Override
    public String getValue(){
      return value;
    }
  }
  private long id;
  private FUNDER_IDENTIFIER_TYPE identifierType;
  private String value;
  private FUNDER_TYPE type;
}
