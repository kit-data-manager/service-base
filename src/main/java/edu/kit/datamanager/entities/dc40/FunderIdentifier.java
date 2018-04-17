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
package edu.kit.datamanager.entities.dc40;

import javax.xml.bind.annotation.XmlEnumValue;

/**
 *
 * @author jejkal
 */
public class FunderIdentifier extends Identifier {

    public enum TYPE {
        ISNI("ISNI"),
        GRID("GRID"),
        CROSSREF_FUNDER_ID("Crossref Funder ID"),
        OTHER("Other");

        private final String value;

        private TYPE(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    private TYPE type;

    public FunderIdentifier(TYPE type) {
        this.type = type;
    }

    public TYPE getType() {
        return type;
    }

    public void setType(TYPE type) {
        this.type = type;
    }

}
