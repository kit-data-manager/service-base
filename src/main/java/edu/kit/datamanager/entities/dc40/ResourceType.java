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

/**
 *
 * @author jejkal
 */
@ApiModel(description = "The type of a resource.")
public class ResourceType {

    public enum TYPE_GENERAL {

        AUDIOVISUAL("Audiovisual"),
        COLLECTION("Collection"),
        DATASET("Dataset"),
        EVENT("Event"),
        IMAGE("Image"),
        INTERACTIVE_RESOURCE("InteractiveResource"),
        MODEL("Model"),
        PHYSICAL_OBJECT("PhysicalObject"),
        SERVICE("Service"),
        SOFTWARE("Software"),
        SOUND("Sound"),
        TEXT("Text"),
        WORKFLOW("Workflow"),
        OTHER("Other");

        private final String value;

        private TYPE_GENERAL(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

    }

    @ApiModelProperty(value = "Measurement Data", dataType = "String", required = true)
    private String value;
    //vocab, e.g. Dataset, Image....
    @ApiModelProperty(value = "DATASET", required = true)
    private TYPE_GENERAL typeGeneral;

    public ResourceType() {
    }

    public static ResourceType createResourceType(String value) {
        ResourceType type = new ResourceType();
        type.value = value;
        type.typeGeneral = TYPE_GENERAL.DATASET;
        return type;
    }

    public static ResourceType createResourceType(String value, TYPE_GENERAL typeGeneral) {
        ResourceType type = new ResourceType();
        type.value = value;
        type.typeGeneral = typeGeneral;
        return type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String type) {
        this.value = type;
    }

    public TYPE_GENERAL getTypeGeneral() {
        return typeGeneral;
    }

    public void setTypeGeneral(TYPE_GENERAL typeGeneral) {
        this.typeGeneral = typeGeneral;
    }

}
