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

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.xml.bind.annotation.XmlEnumValue;

/**
 *
 * @author jejkal
 */
@ApiModel(description = "A data entry of a resource.")
public class Date {

    //Date types
    public enum DATE_TYPE {
        ACCEPTED("Accepted"),
        AVAILABLE("Available"),
        COLLECTED("Collected"),
        COPYRIGHTED("Copyrighted"),
        CREATED("Created"),
        ISSUED("Issued"),
        SUBMITTED("Submitted"),
        UPDATED("Updated"),
        VALID("Valid"),
        REVOKED("Revoked");

        private final String value;

        DATE_TYPE(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    //ISO format
    @ApiModelProperty(value = "The actual date of the entry.", example = "2017-05-10T10:41:00Z", required = true)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "GMT")
    java.util.Date date;
    //vocab, e.g. Created, Issued...
    @ApiModelProperty(value = "Controlled vocabulary value describing the date type.", required = true)
    DATE_TYPE type;

    public Date() {
    }

    public java.util.Date getDate() {
        return date;
    }

    public void setDate(java.util.Date date) {
        this.date = date;
    }

    public DATE_TYPE getType() {
        return type;
    }

    public void setType(DATE_TYPE type) {
        this.type = type;
    }
}
