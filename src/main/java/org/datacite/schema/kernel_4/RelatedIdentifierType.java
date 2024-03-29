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
 * <p>Java-Klasse für relatedIdentifierType.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * <p>
 * <pre>
 * &lt;simpleType name="relatedIdentifierType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="ARK"/>
 *     &lt;enumeration value="arXiv"/>
 *     &lt;enumeration value="bibcode"/>
 *     &lt;enumeration value="DOI"/>
 *     &lt;enumeration value="EAN13"/>
 *     &lt;enumeration value="EISSN"/>
 *     &lt;enumeration value="Handle"/>
 *     &lt;enumeration value="IGSN"/>
 *     &lt;enumeration value="ISBN"/>
 *     &lt;enumeration value="ISSN"/>
 *     &lt;enumeration value="ISTC"/>
 *     &lt;enumeration value="LISSN"/>
 *     &lt;enumeration value="LSID"/>
 *     &lt;enumeration value="PMID"/>
 *     &lt;enumeration value="PURL"/>
 *     &lt;enumeration value="UPC"/>
 *     &lt;enumeration value="URL"/>
 *     &lt;enumeration value="URN"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "relatedIdentifierType")
@XmlEnum
public enum RelatedIdentifierType {

    ARK("ARK"),
    @XmlEnumValue("arXiv")
    AR_XIV("arXiv"),
    @XmlEnumValue("bibcode")
    BIBCODE("bibcode"),
    DOI("DOI"),
    @XmlEnumValue("EAN13")
    EAN_13("EAN13"),
    EISSN("EISSN"),
    @XmlEnumValue("Handle")
    HANDLE("Handle"),
    IGSN("IGSN"),
    ISBN("ISBN"),
    ISSN("ISSN"),
    ISTC("ISTC"),
    LISSN("LISSN"),
    LSID("LSID"),
    PMID("PMID"),
    PURL("PURL"),
    UPC("UPC"),
    URL("URL"),
    URN("URN");
    private final String value;

    RelatedIdentifierType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static RelatedIdentifierType fromValue(String v) {
        for (RelatedIdentifierType c: RelatedIdentifierType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
