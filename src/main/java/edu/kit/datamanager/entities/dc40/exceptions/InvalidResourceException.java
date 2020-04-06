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
package edu.kit.datamanager.entities.dc40.exceptions;

/**
 *
 * @author jejkal
 */
public class InvalidResourceException extends Exception {

    public enum ERROR_TYPE {
        NO_ENTITY("No resource provided."),
        NO_IDENTIFIER("No valid primary identifier and no internal identifier found."),
        NO_TITLE("Mandatory attribute 'title' is missing."),
        NO_PUBLISHER("Mandatory attribute 'publisher' is missing."),
        NO_RESOURCE_TYPE("Mandatory attribute 'resourceType' is missing."),
        NO_AGENT_NAME("Mandatory attribute 'name' is missing."),
        NO_CONTENT("Mandatory resource content is missing."),
        NO_CREATION_DATE("Creation date missing.");

        private final String message;

        private ERROR_TYPE(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }

    }

    private final ERROR_TYPE type;

    public InvalidResourceException(ERROR_TYPE type) {
        super(type.getMessage());
        this.type = type;
    }

    public ERROR_TYPE getType() {
        return type;
    }

    
}
