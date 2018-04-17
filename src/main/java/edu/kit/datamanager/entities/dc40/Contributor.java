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
import javax.xml.bind.annotation.XmlEnumValue;

/**
 *
 * @author jejkal
 */
@ApiModel(description = "A contributor to a resource.")
public class Contributor extends BaseEntity {

    public enum TYPE {
        CONTACT_PERSON("ContactPerson"),
        DATA_COLLECTOR("DataCollector"),
        DATA_CURATOR("DataCurator"),
        DATA_MANAGER("DataManager"),
        DISTRIBUTOR("Distributor"),
        EDITOR("Editor"),
        HOSTING_INSTITUTION("HostingInstitution"),
        OTHER("Other"),
        PRODUCER("Producer"),
        PROJECT_LEADER("ProjectLeader"),
        PROJECT_MANAGER("ProjectManager"),
        PROJECT_MEMBER("ProjectMember"),
        REGISTRATION_AGENCY("RegistrationAgency"),
        REGISTRATION_AUTHORITY("RegistrationAuthority"),
        RELATED_PERSON("RelatedPerson"),
        RESEARCH_GROUP("ResearchGroup"),
        RIGHTS_HOLDER("RightsHolder"),
        RESEARCHER("Researcher"),
        SPONSOR("Sponsor"),
        SUPERVISOR("Supervisor"),
        WORK_PACKAGE_LEADER("WorkPackageLeader");

        private final String value;

        TYPE(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    @ApiModelProperty(value = "Contributing agent, e.g. a user or a group.", dataType = "edu.kit.dama.entities.dc40.Agent", required = true)
    private Agent agent;

    //vocab, e.g. Producer, Editor...
    @ApiModelProperty(value = "Controlled vocabulary value describing the contribution type, e.g. Producer.", required = true)
    private TYPE contributionType;

    @Override
    public String getId() {
        return agent.getId();
    }

    public Contributor() {
    }

    public TYPE getContributionType() {
        return contributionType;
    }

    public void setContributionType(TYPE contributionType) {
        this.contributionType = contributionType;
    }

    public Agent getAgent() {
        return agent;
    }

    public void setAgent(Agent agent) {
        this.agent = agent;
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
        final Contributor other = (Contributor) obj;
        if (!Objects.equals(this.contributionType, other.contributionType)) {
            return false;
        }

        return Objects.equals(this, obj);
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 29 * hash + Objects.hashCode(this.contributionType);
        hash = 29 * hash + Objects.hashCode(this.agent);
        return hash;
    }

    @Override
    public void validate() throws InvalidResourceException {
    }
}
