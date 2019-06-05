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
import java.io.Serializable;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapters;
import lombok.Data;
import org.dozer.Mapping;

/**
 *
 * @author jejkal
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "resource", namespace = "http://datacite.org/schema/kernel-4")
public class DataResource implements Serializable{

  //The internal resource identifier assigned once during creation
  //mandatory
  private PrimaryIdentifier identifier;

  //vocab
  @XmlElementWrapper(name = "creators")
  @XmlElement(name = "creator")
  private Set<Agent> creators = new HashSet<>();
  @XmlElementWrapper(name = "titles")
  @XmlElement(name = "title")
  private Set<Title> titles = new HashSet<>();
  private String publisher;

  //format: YYYY
  private String publicationYear;
  @XmlElement(name = "resourceType")
  private ResourceType resourceType;

  //recommended
  @XmlElementWrapper(name = "subjects")
  @XmlElement(name = "subject")
  private Set<Subject> subjects = new HashSet<>();
  @XmlElementWrapper(name = "contributors")
  @XmlElement(name = "contributor")
  private Set<Contributor> contributors = new HashSet<>();
  @XmlElementWrapper(name = "dates")
  @XmlElement(name = "date")
  private Set<Date> dates = new HashSet<>();
  @XmlElementWrapper(name = "relatedIdentifiers")
  @XmlElement(name = "relatedIdentifier")
  private Set<RelatedIdentifier> relatedIdentifiers = new HashSet<>();
  @XmlElementWrapper(name = "descriptions")
  @XmlElement(name = "description")
  private Set<Description> descriptions = new HashSet<>();
  @XmlElementWrapper(name = "geoLocations")
  @XmlElement(name = "geoLocation")
  private Set<GeoLocation> geoLocations = new HashSet<>();
  private String language;
  @XmlElementWrapper(name = "alternateIdentifiers")
  @XmlElement(name = "alternateIdentifier")
  private Set<Identifier> alternateIdentifiers = new HashSet<>();
  @XmlElementWrapper(name = "sizes")
  @XmlElement(name = "size")
  private Set<String> sizes = new HashSet<>();
  @XmlElementWrapper(name = "formats")
  @XmlElement(name = "format")
  private Set<String> formats = new HashSet<>();
  private String version;
  @XmlElementWrapper(name = "rightsList")
  @XmlElement(name = "rights")
  private Set<Scheme> rights = new HashSet<>();
  private Set<FundingReference> fundingReferences = new HashSet<>();

}