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
@ApiModel(description = "A resource's funding information.")
public class FundingReference {

    @ApiModelProperty(dataType = "String", required = true)
    private String funderName;
    //use identifier?
    @ApiModelProperty(required = false)
    private FunderIdentifier funderIdentifier;
    @ApiModelProperty(required = false)
    private Scheme awardNumber;
    @ApiModelProperty(dataType = "String", required = false)
    private String awardUri;
    @ApiModelProperty(dataType = "String", required = false)
    private String awardTitle;

    public FundingReference() {
    }

    public String getFunderName() {
        return funderName;
    }

    public void setFunderName(String funderName) {
        this.funderName = funderName;
    }

    public FunderIdentifier getFunderIdentifier() {
        return funderIdentifier;
    }

    public void setFunderIdentifier(FunderIdentifier funderIdentifier) {
        this.funderIdentifier = funderIdentifier;
    }

    public Scheme getAwardNumber() {
        return awardNumber;
    }

    public void setAwardNumber(Scheme awardNumber) {
        this.awardNumber = awardNumber;
    }

    public String getAwardTitle() {
        return awardTitle;
    }

    public void setAwardTitle(String awardTitle) {
        this.awardTitle = awardTitle;
    }

    public String getAwardUri() {
        return awardUri;
    }

    public void setAwardUri(String awardUri) {
        this.awardUri = awardUri;
    }

}
