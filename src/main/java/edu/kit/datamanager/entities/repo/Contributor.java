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
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import lombok.Data;

/**
 *
 * @author jejkal
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@XmlAccessorType(XmlAccessType.FIELD)
public class Contributor{

  public enum CONTRIBUTOR_TYPE implements BaseEnum{
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

    private long id;
    private final String value;

    CONTRIBUTOR_TYPE(String value){
      this.value = value;
    }

    @Override
    public String getValue(){
      return value;
    }
  }

  private Agent user;

  //vocab, e.g. Producer, Editor...
  private CONTRIBUTOR_TYPE contributionType;

}
