//
// Diese Datei wurde mit der JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 generiert 
// Siehe <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Änderungen an dieser Datei gehen bei einer Neukompilierung des Quellschemas verloren. 
// Generiert: 2018.01.10 um 07:42:46 AM CET 
//
package org.datacite.schema.kernel_4;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlType;

/**
 * <p>
 * Java-Klasse für contributorType.
 *
 * <p>
 * Das folgende Schemafragment gibt den erwarteten Content an, der in dieser
 * Klasse enthalten ist.
 * <p>
 * <
 * pre>
 * &lt;simpleType name="contributorType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="ContactPerson"/>
 *     &lt;enumeration value="DataCollector"/>
 *     &lt;enumeration value="DataCurator"/>
 *     &lt;enumeration value="DataManager"/>
 *     &lt;enumeration value="Distributor"/>
 *     &lt;enumeration value="Editor"/>
 *     &lt;enumeration value="HostingInstitution"/>
 *     &lt;enumeration value="Other"/>
 *     &lt;enumeration value="Producer"/>
 *     &lt;enumeration value="ProjectLeader"/>
 *     &lt;enumeration value="ProjectManager"/>
 *     &lt;enumeration value="ProjectMember"/>
 *     &lt;enumeration value="RegistrationAgency"/>
 *     &lt;enumeration value="RegistrationAuthority"/>
 *     &lt;enumeration value="RelatedPerson"/>
 *     &lt;enumeration value="ResearchGroup"/>
 *     &lt;enumeration value="RightsHolder"/>
 *     &lt;enumeration value="Researcher"/>
 *     &lt;enumeration value="Sponsor"/>
 *     &lt;enumeration value="Supervisor"/>
 *     &lt;enumeration value="WorkPackageLeader"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 *
 */
@XmlType(name = "contributorType")
@XmlEnum
public enum ContributorType {

    @XmlEnumValue("ContactPerson")
    CONTACT_PERSON("ContactPerson"),
    @XmlEnumValue("DataCollector")
    DATA_COLLECTOR("DataCollector"),
    @XmlEnumValue("DataCurator")
    DATA_CURATOR("DataCurator"),
    @XmlEnumValue("DataManager")
    DATA_MANAGER("DataManager"),
    @XmlEnumValue("Distributor")
    DISTRIBUTOR("Distributor"),
    @XmlEnumValue("Editor")
    EDITOR("Editor"),
    @XmlEnumValue("HostingInstitution")
    HOSTING_INSTITUTION("HostingInstitution"),
    @XmlEnumValue("Other")
    OTHER("Other"),
    @XmlEnumValue("Producer")
    PRODUCER("Producer"),
    @XmlEnumValue("ProjectLeader")
    PROJECT_LEADER("ProjectLeader"),
    @XmlEnumValue("ProjectManager")
    PROJECT_MANAGER("ProjectManager"),
    @XmlEnumValue("ProjectMember")
    PROJECT_MEMBER("ProjectMember"),
    @XmlEnumValue("RegistrationAgency")
    REGISTRATION_AGENCY("RegistrationAgency"),
    @XmlEnumValue("RegistrationAuthority")
    REGISTRATION_AUTHORITY("RegistrationAuthority"),
    @XmlEnumValue("RelatedPerson")
    RELATED_PERSON("RelatedPerson"),
    @XmlEnumValue("ResearchGroup")
    RESEARCH_GROUP("ResearchGroup"),
    @XmlEnumValue("RightsHolder")
    RIGHTS_HOLDER("RightsHolder"),
    @XmlEnumValue("Researcher")
    RESEARCHER("Researcher"),
    @XmlEnumValue("Sponsor")
    SPONSOR("Sponsor"),
    @XmlEnumValue("Supervisor")
    SUPERVISOR("Supervisor"),
    @XmlEnumValue("WorkPackageLeader")
    WORK_PACKAGE_LEADER("WorkPackageLeader");
    private final String value;

    ContributorType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static ContributorType fromValue(String v) {
        for (ContributorType c : ContributorType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
