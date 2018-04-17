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

/**
 *
 * @author jejkal
 */
@ApiModel(description = "The title of a resource.")
public class Title extends BaseEntity {

    public enum TYPE {
        ALTERNATIVE_TITLE("AlternativeTitle"),
        SUBTITLE("Subtitle"),
        TRANSLATED_TITLE("TranslatedTitle"),
        OTHER("Other");
        private final String value;

        TYPE(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

    }

    @ApiModelProperty(value = "My sample resource", dataType = "String", required = true)
    private String value;
    //vocab, e.g. Subtitle, AlternativeTitle
    @ApiModelProperty(value = "SUBTITLE", required = false)
    private TYPE titleType;
    @ApiModelProperty(value = "en", required = false)
    private String lang;

    public Title() {
    }

    @Override
    public String getId() {
        return null;
    }

    public static Title createTitle(String value) {
        Title t = new Title();
        t.value = value;
        t.titleType = TYPE.TRANSLATED_TITLE;
        return t;
    }

    public static Title createTitle(String value, TYPE type) {
        Title t = new Title();
        t.titleType = type;
        t.value = value;
        return t;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public TYPE getTitleType() {
        return titleType;
    }

    public void setTitleType(TYPE titleType) {
        this.titleType = titleType;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public String getLang() {
        return lang;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 97 * hash + Objects.hashCode(this.value);
        hash = 97 * hash + Objects.hashCode(this.titleType);
        hash = 97 * hash + Objects.hashCode(this.lang);
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
        final Title other = (Title) obj;
        if (!Objects.equals(this.value, other.value)) {
            return false;
        }
        if (!Objects.equals(this.lang, other.lang)) {
            return false;
        }
        return this.titleType == other.titleType;
    }

    @Override
    public void validate() throws InvalidResourceException {

    }

}
