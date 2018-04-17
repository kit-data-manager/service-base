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

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import edu.kit.datamanager.entities.BaseEntity;
import edu.kit.datamanager.entities.dc40.exceptions.InvalidResourceException;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.HashSet;
import java.util.Set;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.IteratorUtils;
import edu.kit.datamanager.util.FilenameUtils;
import java.text.SimpleDateFormat;
import java.util.Objects;
import org.slf4j.LoggerFactory;

/**
 *
 * @author jejkal
 */
@ApiModel(description = "Data resource element")
public class DataResource extends BaseEntity {

    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(DataResource.class);

    @JsonIgnore
    private String _key;

    public enum State {
        VOLATILE,
        FIXED,
        REVOKED;
    }

    //mandatory
    @ApiModelProperty(required = true)
    private PrimaryIdentifier identifier;
    //The internal resource identifier assigned once during creation
    @ApiModelProperty(hidden = true)
    private String resourceIdentifier = null;
    //vocab
    @ApiModelProperty(required = true)
    private Set<Agent> creator;
    @ApiModelProperty(required = true)
    private Set<Title> title;
    @ApiModelProperty(value = "Publisher, e.g. institution", example = "Karlsruhe Institute of Technology", required = true)
    private String publisher;
    //format: YYYY
    @ApiModelProperty(value = "Publication year (could be aquisition year, if publication year is not feasible)", example = "2017", required = true)
    private String publicationYear;
    @ApiModelProperty(required = true)
    private ResourceType resourceType;

    //recommended
    @ApiModelProperty(value = "One or more subjects describing the resource (recommended).", required = false)
    private Set<Subject> subject;
    @ApiModelProperty(value = "One or more contributors that have contributed to the resource (recommended).", required = false)
    private Set<Contributor> contributor;
    @ApiModelProperty(value = "One or more dates related to the resource, e.g. creation or publication date (recommended).", required = false)
    private Set<Date> date;
    @ApiModelProperty(value = "One or more related identifiers the can be used to identify related resources, e.g. metadata, parts or derived resources (recommended).", required = false)
    private Set<RelatedIdentifier> relatedIdentifier;
    @ApiModelProperty(value = "One or more description entries providing additional information, e.g. abstract or technical information (recommended).", required = false)
    private Set<Description> description;
    @ApiModelProperty(value = "One or more geolocation entries providing information about the location of the resource, e.g. storage or aquisition location (recommended).", required = false)
    private Set<GeoLocation> geoLocation;

    //optional
    @ApiModelProperty(value = "The primary language of the resource. Possible codes are IETF BCP 47 or ISO 639-1.", example = "en, de, fr", required = false)
    private String language;
    @ApiModelProperty(value = "One or more alternate identifiers the can be used to identify the resources in addition to the primary identifier.", required = false)
    private Set<Identifier> alternateIdentifier;
    @ApiModelProperty(value = "Unstructured size information about the resource or its contents.", example = "15 files, 10 page, 100 bytes", required = false)
    private Set<String> size;
    @ApiModelProperty(value = "Format information about the resource or its contents. Preferably, mime types or file extensions are used.", example = "text/plain, xml, application/pdf", required = false)
    private Set<String> format;
    //e.g. major.minor
    @ApiModelProperty(value = "Version of the resource, e.g. major.minor.", example = "1.0", required = false)
    private String version;
    //e.g. CC-0
    @ApiModelProperty(value = "Intellectual property information.", required = false)
    private Set<Scheme> rights;
    @ApiModelProperty(value = "Funding information, e.g. funder, award number and title.", required = false)
    private Set<FundingReference> fundingReference;

    //internal properties
    //state of the resource (VOLATILE by default)
    @ApiModelProperty(value = "State information of the resource. After creation each resource is classified as VOLATILE", required = false)
    private State state = State.VOLATILE;
    //embargo date that should reveive a value 'resourceCreationTime + DefaultEmbargoSpan' on resource creation time
    @ApiModelProperty(value = "Date at which the embargo ends, e.g. after which the resource is published.", example = "2020-05-16'T'13:09:12.000'Z'", required = true)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "GMT")
    private java.util.Date embargoDate;

    public static DataResource factoryNewDataResource() {
        DataResource result = new DataResource();
        result.setIdentifier(PrimaryIdentifier.factoryPrimaryIdentifier());
        Identifier internal = Identifier.factoryInternalIdentifier();
        result.addInternalIdentifier(internal);
        result._key = internal.getValue();
        result.resourceIdentifier = internal.getValue();
        return result;
    }

    public static DataResource factoryDataResourceWithDoi(String doi) {
        DataResource result = new DataResource();
        result.setIdentifier(PrimaryIdentifier.factoryPrimaryIdentifier(doi));
        Identifier internal = Identifier.factoryInternalIdentifier(doi);
        result.addInternalIdentifier(internal);
        result._key = internal.getValue();
        result.resourceIdentifier = internal.getValue();
        return result;
    }

    public static DataResource factoryNewDataResource(String internalIdentifier) {
        if (internalIdentifier == null) {
            throw new IllegalArgumentException("Internal identifier must not be null.");
        }
        DataResource result = new DataResource();
        result.setIdentifier(PrimaryIdentifier.factoryPrimaryIdentifier());
        result.addInternalIdentifier(Identifier.factoryInternalIdentifier(internalIdentifier));
        result._key = internalIdentifier;
        result.resourceIdentifier = internalIdentifier;
        return result;
    }

    public DataResource() {
    }

    public String getResourceIdentifier() {
        return resourceIdentifier;
    }

    public void setResourceIdentifier(String resourceIdentifier) {
        if (this.resourceIdentifier == null) {
            this.resourceIdentifier = resourceIdentifier;
        } else {
            LOGGER.debug("A resource identifier is already assigned. Skipping identifier assignment.");
        }
    }

    @JsonIgnore
    public String getDataLocation() throws InvalidResourceException {
        StringBuilder relativePath = new StringBuilder();
        SimpleDateFormat f = new SimpleDateFormat("YYYY/MM");
        java.util.Date creationDate = getCreationDate();
        if (creationDate == null) {
            throw new InvalidResourceException(InvalidResourceException.ERROR_TYPE.NO_CREATION_DATE);
        }

        relativePath.append(f.format(creationDate)).append("/").append(FilenameUtils.escapeStringAsFilename(getResourceIdentifier()));
        return relativePath.toString();
    }

    public PrimaryIdentifier getIdentifier() {
        return identifier;
    }

    public void setIdentifier(PrimaryIdentifier identifier) {
        if (identifier == null) {
            throw new IllegalArgumentException("Argument 'identifier' must not be null.");
        }
        this.identifier = identifier;
    }

    public Set<Agent> getCreator() {
        if (creator == null) {
            creator = new HashSet<>();
        }
        return creator;
    }

    public void setCreator(Set<Agent> creator) {
        this.creator = creator;
    }

    public Set<Title> getTitle() {
        if (title == null) {
            title = new HashSet<>();
        }
        return title;
    }

    public void setTitle(Set<Title> title) {
        this.title = title;
    }

    public String getPublicationYear() {
        return publicationYear;
    }

    public void setPublicationYear(String publicationYear) {
        this.publicationYear = publicationYear;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public ResourceType getResourceType() {
        return resourceType;
    }

    public void setResourceType(ResourceType resourceType) {
        this.resourceType = resourceType;
    }

    public Set<Subject> getSubject() {
        if (subject == null) {
            subject = new HashSet<>();
        }
        return subject;
    }

    public void setSubject(Set<Subject> subject) {
        this.subject = subject;
    }

    public Set<Contributor> getContributor() {
        if (contributor == null) {
            contributor = new HashSet<>();
        }
        return contributor;
    }

    public void setContributor(Set<Contributor> contributor) {
        this.contributor = contributor;
    }

    public Set<Date> getDate() {
        if (date == null) {
            date = new HashSet<>();
        }
        return date;
    }

    public void addDate(Date newDate) {
        if (Date.DATE_TYPE.CREATED.equals(newDate.getType())) {
            Date existingCreationDate = IteratorUtils.find(getDate().iterator(), (t) -> {
                return t.getType().equals(Date.DATE_TYPE.CREATED);
            });
            if (existingCreationDate != null) {
                LOGGER.trace("Creation data is already set at resource " + _key + ". Ignoring new date.");
                return;
            }
        }
        getDate().add(newDate);
    }

    public java.util.Date getCreationDate() {
        Date existingCreationDate = IteratorUtils.find(getDate().iterator(), (t) -> {
            return t.getType().equals(Date.DATE_TYPE.CREATED);
        });
        if (existingCreationDate == null) {
            return null;
        }
        return existingCreationDate.getDate();
    }

    public void setDate(Set<Date> date) {
        this.date = date;
    }

    public Set<RelatedIdentifier> getRelatedIdentifier() {
        if (relatedIdentifier == null) {
            relatedIdentifier = new HashSet<>();
        }
        return relatedIdentifier;
    }

    public void setRelatedIdentifier(Set<RelatedIdentifier> relatedIdentifier) {
        this.relatedIdentifier = relatedIdentifier;
    }

    public Set<Description> getDescription() {
        if (description == null) {
            description = new HashSet<>();
        }
        return description;
    }

    public void setDescription(Set<Description> description) {
        this.description = description;
    }

    public Set<GeoLocation> getGeoLocation() {
        if (geoLocation == null) {
            geoLocation = new HashSet<>();
        }
        return geoLocation;
    }

    public void setGeoLocation(Set<GeoLocation> geoLocation) {
        this.geoLocation = geoLocation;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    private void addInternalIdentifier(Identifier altIdentifier) {
        if (Identifier.IDENTIFIER_TYPE.INTERNAL.equals(altIdentifier.getIdentifierType())) {
            if (CollectionUtils.filter(getAlternateIdentifier(), (Identifier t) -> t.getIdentifierType().equals(altIdentifier.getIdentifierType()))) {
                throw new IllegalArgumentException("DataResource already contains an internal identifier.");
            }
        }
        getAlternateIdentifier().add(altIdentifier);
    }

    public Set<Identifier> getAlternateIdentifier() {
        if (alternateIdentifier == null) {
            alternateIdentifier = new HashSet<>();
        }
        return alternateIdentifier;
    }

    public void setAlternateIdentifier(Set<Identifier> alternateIdentifier) {
        this.alternateIdentifier = alternateIdentifier;
    }

    public Set<String> getSize() {
        if (size == null) {
            size = new HashSet<>();
        }
        return size;
    }

    public void setSize(Set<String> size) {
        this.size = size;
    }

    public Set<String> getFormat() {
        if (format == null) {
            format = new HashSet<>();
        }
        return format;
    }

    public void setFormat(Set<String> format) {
        this.format = format;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public Set<Scheme> getRights() {
        if (rights == null) {
            rights = new HashSet<>();
        }
        return rights;
    }

    public void setRights(Set<Scheme> rights) {
        this.rights = rights;
    }

    public Set<FundingReference> getFundingReference() {
        if (fundingReference == null) {
            fundingReference = new HashSet<>();
        }
        return fundingReference;
    }

    public void setFundingReference(Set<FundingReference> fundingReference) {
        this.fundingReference = fundingReference;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public java.util.Date getEmbargoDate() {
        return embargoDate;
    }

    public void setEmbargoDate(java.util.Date embargoDate) {
        this.embargoDate = embargoDate;
    }

    @Override
    public String getId() {
        if (_key == null) {
            _key = getResourceIdentifier();
        }
        return _key;
    }

    @Override
    public void validate() throws InvalidResourceException {
        //check identifier...this throws an InvalidResourceException in case of an error
        getResourceIdentifier();
        //check mandatory fields that cannot be assigned automatically
        if (getTitle().isEmpty()) {
            throw new InvalidResourceException(InvalidResourceException.ERROR_TYPE.NO_TITLE);
        }
        if (publisher == null) {
            throw new InvalidResourceException(InvalidResourceException.ERROR_TYPE.NO_PUBLISHER);
        }
        if (resourceType == null) {
            throw new InvalidResourceException(InvalidResourceException.ERROR_TYPE.NO_RESOURCE_TYPE);
        }
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 79 * hash + Objects.hashCode(this._key);
        hash = 79 * hash + Objects.hashCode(this.identifier);
        hash = 79 * hash + Objects.hashCode(this.creator);
        hash = 79 * hash + Objects.hashCode(this.title);
        hash = 79 * hash + Objects.hashCode(this.publisher);
        hash = 79 * hash + Objects.hashCode(this.publicationYear);
        hash = 79 * hash + Objects.hashCode(this.resourceType);
        hash = 79 * hash + Objects.hashCode(this.subject);
        hash = 79 * hash + Objects.hashCode(this.contributor);
        hash = 79 * hash + Objects.hashCode(this.date);
        hash = 79 * hash + Objects.hashCode(this.relatedIdentifier);
        hash = 79 * hash + Objects.hashCode(this.description);
        hash = 79 * hash + Objects.hashCode(this.geoLocation);
        hash = 79 * hash + Objects.hashCode(this.language);
        hash = 79 * hash + Objects.hashCode(this.alternateIdentifier);
        hash = 79 * hash + Objects.hashCode(this.size);
        hash = 79 * hash + Objects.hashCode(this.format);
        hash = 79 * hash + Objects.hashCode(this.version);
        hash = 79 * hash + Objects.hashCode(this.rights);
        hash = 79 * hash + Objects.hashCode(this.fundingReference);
        hash = 79 * hash + Objects.hashCode(this.state);
        hash = 79 * hash + Objects.hashCode(this.embargoDate);
        return hash;
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
        final DataResource other = (DataResource) obj;
        if (!Objects.equals(this._key, other._key)) {
            return false;
        }
        if (!Objects.equals(this.publisher, other.publisher)) {
            return false;
        }
        if (!Objects.equals(this.publicationYear, other.publicationYear)) {
            return false;
        }
        if (!Objects.equals(this.language, other.language)) {
            return false;
        }
        if (!Objects.equals(this.version, other.version)) {
            return false;
        }
        if (!Objects.equals(this.identifier, other.identifier)) {
            return false;
        }
        if (!Objects.equals(this.creator, other.creator)) {
            return false;
        }
        if (!Objects.equals(this.title, other.title)) {
            return false;
        }
        if (!Objects.equals(this.resourceType, other.resourceType)) {
            return false;
        }
        if (!Objects.equals(this.subject, other.subject)) {
            return false;
        }
        if (!Objects.equals(this.contributor, other.contributor)) {
            return false;
        }
        if (!Objects.equals(this.date, other.date)) {
            return false;
        }
        if (!Objects.equals(this.relatedIdentifier, other.relatedIdentifier)) {
            return false;
        }
        if (!Objects.equals(this.description, other.description)) {
            return false;
        }
        if (!Objects.equals(this.geoLocation, other.geoLocation)) {
            return false;
        }
        if (!Objects.equals(this.alternateIdentifier, other.alternateIdentifier)) {
            return false;
        }
        if (!Objects.equals(this.size, other.size)) {
            return false;
        }
        if (!Objects.equals(this.format, other.format)) {
            return false;
        }
        if (!Objects.equals(this.rights, other.rights)) {
            return false;
        }
        if (!Objects.equals(this.fundingReference, other.fundingReference)) {
            return false;
        }
        if (this.state != other.state) {
            return false;
        }
        if (!Objects.equals(this.embargoDate, other.embargoDate)) {
            return false;
        }
        return true;
    }

}
