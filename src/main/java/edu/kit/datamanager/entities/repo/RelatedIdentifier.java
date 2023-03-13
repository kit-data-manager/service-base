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
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import lombok.Data;

/**
 *
 * @author jejkal
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@XmlAccessorType(XmlAccessType.FIELD)
public class RelatedIdentifier {

    public enum RELATED_IDENTIFIER_TYPE implements BaseEnum {
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

        private RELATED_IDENTIFIER_TYPE(String value) {
            this.value = value;
        }

        @Override
        public String getValue() {
            return value;
        }
    }

    public enum RELATION_TYPES implements BaseEnum {
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
        IS_SOURCE_OF("IsSourceOf"),
        IS_VERSION_OF("IsVersionOf"),
        HAS_VERSION("HasVersion"),
        ISCITEDBY("IsCitedBy"),
        ISSUPPLEMENTTO("IsSupplementTo"),
        ISSUPPLEMENTEDBY("IsSupplementedBy"),
        ISCONTINUEDBY("IsContinuedBy"),
        ISNEWVERSIONOF("IsNewVersionOf"),
        ISPREVIOUSVERSIONOF("IsPreviousVersionOf"),
        ISPARTOF("IsPartOf"),
        HASPART("HasPart"),
        ISREFERENCEDBY("IsReferencedBy"),
        ISDOCUMENTEDBY("IsDocumentedBy"),
        ISCOMPILEDBY("IsCompiledBy"),
        ISVARIANTFORMOF("IsVariantFormOf"),
        ISORIGINALFORMOF("IsOriginalFormOf"),
        ISIDENTICALTO("IsIdenticalTo"),
        HASMETADATA("HasMetadata"),
        ISMETADATAFOR("IsMetadataFor"),
        ISREVIEWEDBY("IsReviewedBy"),
        ISDERIVEDFROM("IsDerivedFrom"),
        ISSOURCEOF("IsSourceOf"),
        ISVERSIONOF("IsVersionOf"),
        HASVERSION("HasVersion");

        private final String value;

        private RELATION_TYPES(String value) {
            this.value = value;
        }

        @Override
        public String getValue() {
            return value;
        }
    }
    private long id;
    private RELATED_IDENTIFIER_TYPE identifierType;
    private String value;
    //vocab, e.g. IsMetadataFor...
    private RELATION_TYPES relationType;
    private Scheme scheme;
    private String relatedMetadataScheme;
}
