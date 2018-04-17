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

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.xml.bind.annotation.XmlEnumValue;

/**
 *
 * @author jejkal
 */
@ApiModel(description = "A related identifier for a resource.")
public class RelatedIdentifier extends Identifier {

    public enum RELATION_TYPES {
        IS_CITED_BY("IsCitedBy"),
        CITES("Cites"),
        IS_SUPPLEMENT_TO("IsSupplementTo"),
        IS_SUPPLEMENTED_BY("IsSupplementedBy"),
        IS_CONTINUED_BY("IsContinuedBy"),
        CONTINUES("Continues"),
        IS_NEW_VERSION_OF("IsNewVersionOf"),
        IS_PREVIOUS_VERSION_OF("IsPreviousVersionOf"),
        IS_PART_OF("IsPartOf"),
        HAS_PART("HasPart"),
        IS_REFERENCED_BY("IsReferencedBy"),
        REFERENCES("References"),
        IS_DOCUMENTED_BY("IsDocumentedBy"),
        DOCUMENTS("Documents"),
        IS_COMPILED_BY("IsCompiledBy"),
        COMPILES("Compiles"),
        IS_VARIANT_FORM_OF("IsVariantFormOf"),
        IS_ORIGINAL_FORM_OF("IsOriginalFormOf"),
        IS_IDENTICAL_TO("IsIdenticalTo"),
        HAS_METADATA("HasMetadata"),
        IS_METADATA_FOR("IsMetadataFor"),
        REVIEWS("Reviews"),
        IS_REVIEWED_BY("IsReviewedBy"),
        IS_DERIVED_FROM("IsDerivedFrom"),
        IS_SOURCE_OF("IsSourceOf");

        private final String value;

        private RELATION_TYPES(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    //vocab, e.g. IsMetadataFor...
    @ApiModelProperty(value = "Controlled vocabulary value describing the relation type, e.g. IS_PART_OF or IS_METADATA_FOR.", dataType = "String", required = true)
    private RELATION_TYPES relationType;
    @ApiModelProperty(value = "Identifier scheme.", required = false)
    private Scheme scheme;
    @ApiModelProperty(value = "Related metadata scheme.", required = false)
    private String relatedMetadataScheme;

    public RelatedIdentifier() {
    }

    public RELATION_TYPES getRelationType() {
        return relationType;
    }

    public void setRelationType(RELATION_TYPES relationType) {
        this.relationType = relationType;
    }

    public Scheme getScheme() {
        return scheme;
    }

    public void setScheme(Scheme scheme) {
        this.scheme = scheme;
    }

    public String getRelatedMetadataScheme() {
        return relatedMetadataScheme;
    }

    public void setRelatedMetadataScheme(String relatedMetadataScheme) {
        this.relatedMetadataScheme = relatedMetadataScheme;
    }

}
