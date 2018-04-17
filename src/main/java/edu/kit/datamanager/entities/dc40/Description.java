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
@ApiModel(description = "A description entry of a resource.")
public class Description {

    public enum TYPE {
        ABSTRACT("Abstract"),
        METHODS("Methods"),
        SERIES_INFORMATION("SeriesInformation"),
        TABLE_OF_CONTENTS("TableOfContents"),
        TECHNICAL_INFO("TechnicalInfo"),
        OTHER("Other");

        private final String value;

        private TYPE(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    @ApiModelProperty(value = "The actual description as full text.", dataType = "String", required = true)
    private String description;
    //vocab, e.g. Abstract
    @ApiModelProperty(value = "Controlled vocabulary value describing the description type.", required = true)
    private TYPE type;
    @ApiModelProperty(value = "Description language.", required = false)
    private String lang;

    public Description() {
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public TYPE getType() {
        return type;
    }

    public void setType(TYPE type) {
        this.type = type;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

}
