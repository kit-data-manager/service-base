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
package edu.kit.datamanager.entities.dc40;

import edu.kit.datamanager.entities.BaseEntity;
import edu.kit.datamanager.entities.dc40.exceptions.InvalidResourceException;
import io.swagger.annotations.ApiModel;
import java.util.Objects;
import io.swagger.annotations.ApiModelProperty;
import java.util.UUID;

/**
 *
 * @author jejkal
 */
@ApiModel(description = "An identifier for a resource.")
public class Identifier extends BaseEntity {

    public enum IDENTIFIER_TYPE {
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

        private IDENTIFIER_TYPE(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    @ApiModelProperty(value = "10.1234/foo", dataType = "String", required = true)
    private String value;
    @ApiModelProperty(value = "Controlled vocabulary, e.g. INTERNAL or DOI.", required = true)
    private IDENTIFIER_TYPE identifierType;

    public Identifier() {
    }

    @Override
    public String getId() {
        return value;
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

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public IDENTIFIER_TYPE getIdentifierType() {
        return identifierType;
    }

    public void setIdentifierType(IDENTIFIER_TYPE identifierType) {
        this.identifierType = identifierType;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 19 * hash + Objects.hashCode(this.value);
        hash = 19 * hash + Objects.hashCode(this.identifierType);
        return hash;
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
        return Objects.equals(this.identifierType, other.identifierType);
    }

    @Override
    public void validate() throws InvalidResourceException {

    }
}
