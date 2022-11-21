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
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@XmlAccessorType(XmlAccessType.FIELD)
public class Title {

    public enum TITLE_TYPE implements BaseEnum {
        ALTERNATIVE_TITLE("AlternativeTitle"),
        SUBTITLE("Subtitle"),
        TRANSLATED_TITLE("TranslatedTitle"),
        OTHER("Other");
        private final String value;

        TITLE_TYPE(String value) {
            this.value = value;
        }

        @Override
        public String getValue() {
            return value;
        }

    }
    private long id;
    private String value;
    //vocab, e.g. Subtitle, AlternativeTitle
    private TITLE_TYPE titleType;
    private String lang;

    public static Title createTitle(String value) {
        return createTitle(value, null);
    }

    public static Title createTitle(String value, TITLE_TYPE type) {
        Title t = new Title();
        t.titleType = type;
        t.value = value;
        return t;
    }

}
