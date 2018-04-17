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
package edu.kit.datamanager.util;

import edu.kit.datamanager.entities.dc40.Agent;
import edu.kit.datamanager.entities.dc40.Contributor;
import edu.kit.datamanager.entities.dc40.DataResource;
import edu.kit.datamanager.entities.dc40.Date;
import edu.kit.datamanager.entities.dc40.Description;
import edu.kit.datamanager.entities.dc40.FundingReference;
import edu.kit.datamanager.entities.dc40.GeoLocation;
import edu.kit.datamanager.entities.dc40.Group;
import edu.kit.datamanager.entities.dc40.Identifier;
import edu.kit.datamanager.entities.dc40.RelatedIdentifier;
import edu.kit.datamanager.entities.dc40.ResourceType;
import edu.kit.datamanager.entities.dc40.Scheme;
import edu.kit.datamanager.entities.dc40.Subject;
import edu.kit.datamanager.entities.dc40.Title;
import edu.kit.datamanager.entities.dc40.User;
import java.text.SimpleDateFormat;
import org.datacite.schema.kernel_4.Box;
import org.datacite.schema.kernel_4.ContributorType;
import org.datacite.schema.kernel_4.DateType;
import org.datacite.schema.kernel_4.DescriptionType;
import org.datacite.schema.kernel_4.FunderIdentifierType;
import org.datacite.schema.kernel_4.RelatedIdentifierType;
import org.datacite.schema.kernel_4.RelationType;
import org.datacite.schema.kernel_4.Resource;
import org.datacite.schema.kernel_4.TitleType;

/**
 *
 * @author jejkal
 */
public class DCTransformationHelper{

  public static Resource toDataCite(DataResource resource){
    Resource res = new Resource();
    res.setPublisher(resource.getPublisher());
    res.setPublicationYear(resource.getPublicationYear());
    res.setLanguage(resource.getLanguage());
    res.setVersion(resource.getVersion());
    res.setIdentifier(transformIdentifier(resource.getIdentifier()));
    res.setResourceType(transformResourceType(resource.getResourceType()));
    if(!resource.getSize().isEmpty()){
      res.setSizes(new Resource.Sizes());
      res.getSizes().getSize().addAll(resource.getSize());
    }
    if(!resource.getFormat().isEmpty()){
      res.setFormats(new Resource.Formats());
      res.getFormats().getFormat().addAll(resource.getFormat());
    }

    if(!resource.getCreator().isEmpty()){
      res.setCreators(new Resource.Creators());
      resource.getCreator().forEach((creator) -> {
        res.getCreators().getCreator().add(transformCreator(creator));
      });
    }
    if(!resource.getContributor().isEmpty()){
      res.setContributors(new Resource.Contributors());
      resource.getContributor().forEach((contributor) -> {
        res.getContributors().getContributor().add(transformContributor(contributor));
      });
    }

    if(!resource.getTitle().isEmpty()){
      res.setTitles(new Resource.Titles());
      resource.getTitle().forEach((title) -> {
        res.getTitles().getTitle().add(transformTitle(title));
      });
    }

    if(!resource.getSubject().isEmpty()){
      res.setSubjects(new Resource.Subjects());
      resource.getSubject().forEach((subject) -> {
        res.getSubjects().getSubject().add(transformSubject(subject));
      });
    }

    if(!resource.getDate().isEmpty()){
      res.setDates(new Resource.Dates());
      resource.getDate().forEach((date) -> {
        res.getDates().getDate().add(transformDate(date));
      });
    }

    if(!resource.getAlternateIdentifier().isEmpty()){
      res.setAlternateIdentifiers(new Resource.AlternateIdentifiers());
      resource.getAlternateIdentifier().forEach((identifier) -> {
        res.getAlternateIdentifiers().getAlternateIdentifier().add(transformAlternateIdentifier(identifier));
      });
    }

    if(!resource.getRelatedIdentifier().isEmpty()){
      res.setRelatedIdentifiers(new Resource.RelatedIdentifiers());
      resource.getRelatedIdentifier().forEach((identifier) -> {
        res.getRelatedIdentifiers().getRelatedIdentifier().add(transformRelatedIdentifier(identifier));
      });
    }
    if(!resource.getRights().isEmpty()){
      res.setRightsList(new Resource.RightsList());
      resource.getRights().forEach((right) -> {
        res.getRightsList().getRights().add(transformRights(right));
      });
    }
    if(!resource.getDescription().isEmpty()){
      res.setDescriptions(new Resource.Descriptions());
      resource.getDescription().forEach((description) -> {
        res.getDescriptions().getDescription().add(transformDescription(description));
      });
    }
    if(!resource.getGeoLocation().isEmpty()){
      res.setGeoLocations(new Resource.GeoLocations());
      resource.getGeoLocation().forEach((geolocation) -> {
        res.getGeoLocations().getGeoLocation().add(transformGeolocation(geolocation));
      });
    }
    if(!resource.getFundingReference().isEmpty()){
      res.setFundingReferences(new Resource.FundingReferences());
      resource.getFundingReference().forEach((reference) -> {
        res.getFundingReferences().getFundingReference().add(transformFundingReference(reference));
      });
    }
    return res;
  }

  private static Resource.Identifier transformIdentifier(Identifier identifier){
    Resource.Identifier result = new Resource.Identifier();
    result.setIdentifierType(identifier.getIdentifierType().toString());
    result.setValue(identifier.getValue());
    return result;
  }

  private static Resource.Creators.Creator transformCreator(Agent agent){
    Resource.Creators.Creator result = new Resource.Creators.Creator();
    result.setCreatorName(agent.getName());
    if(agent.getAgentType().equals(User.USER_AGENT_TYPE)){
      User userAgent = (User) agent;
      result.setFamilyName(userAgent.getFamilyName());
      result.setGivenName(userAgent.getGivenName());
      if(userAgent.getAffiliation() != null){
        result.getAffiliation().add(userAgent.getAffiliation());
      }
      if(userAgent.getIdentifier() != null){
        Resource.Creators.Creator.NameIdentifier nameIdentifier = new Resource.Creators.Creator.NameIdentifier();
        nameIdentifier.setValue(userAgent.getIdentifier());
        if(userAgent.getIdentifierScheme() != null){
          nameIdentifier.setNameIdentifierScheme(userAgent.getIdentifierScheme().getSchemeId());
          nameIdentifier.setSchemeURI(userAgent.getIdentifierScheme().getSchemeUri());
        }
        result.getNameIdentifier().add(nameIdentifier);
      }
    } else if(agent.getAgentType().equals(Group.GROUP_AGENT_TYPE)){
      Group groupAgent = (Group) agent;
      Resource.Creators.Creator.NameIdentifier nameIdentifier = new Resource.Creators.Creator.NameIdentifier();
      nameIdentifier.setValue(groupAgent.getIdentifier());
      if(groupAgent.getIdentifierScheme() != null){
        nameIdentifier.setNameIdentifierScheme(groupAgent.getIdentifierScheme().getSchemeId());
        nameIdentifier.setSchemeURI(groupAgent.getIdentifierScheme().getSchemeUri());
      }
      result.getNameIdentifier().add(nameIdentifier);
    }

    return result;
  }

  private static Resource.Titles.Title transformTitle(Title title){
    Resource.Titles.Title result = new Resource.Titles.Title();
    result.setValue(title.getValue());
    result.setLang(title.getLang());
    try{
      result.setTitleType(TitleType.fromValue(title.getTitleType().getValue()));
    } catch(IllegalArgumentException ex){
      //invalid enum
      result.setTitleType(TitleType.OTHER);
    }
    return result;
  }

  private static Resource.ResourceType transformResourceType(ResourceType type){
    Resource.ResourceType result = new Resource.ResourceType();
    result.setValue(type.getValue());
    try{
      result.setResourceTypeGeneral(org.datacite.schema.kernel_4.ResourceType.fromValue(type.getTypeGeneral().getValue()));
    } catch(IllegalArgumentException ex){
      //invalid enum
      result.setResourceTypeGeneral(org.datacite.schema.kernel_4.ResourceType.OTHER);
    }

    return result;
  }

  private static Resource.Subjects.Subject transformSubject(Subject subject){
    Resource.Subjects.Subject result = new Resource.Subjects.Subject();
    result.setLang(subject.getLang());
    result.setValue(subject.getValue());
    result.setValueURI(subject.getValueUri());
    if(subject.getScheme() != null){
      result.setSchemeURI(subject.getScheme().getSchemeUri());
      result.setSubjectScheme(subject.getScheme().getSchemeId());
    }
    return result;
  }

  private static Resource.Contributors.Contributor transformContributor(Contributor contributor){
    Resource.Contributors.Contributor result = new Resource.Contributors.Contributor();
    result.setContributorName(contributor.getAgent().getName());
    if(contributor.getAgent().getAgentType().equals(User.USER_AGENT_TYPE)){
      User userAgent = (User) contributor.getAgent();
      result.setFamilyName(userAgent.getFamilyName());
      result.setGivenName(userAgent.getGivenName());
      if(userAgent.getAffiliation() != null){
        result.getAffiliation().add(userAgent.getAffiliation());
      }
      if(userAgent.getIdentifier() != null){
        Resource.Contributors.Contributor.NameIdentifier nameIdentifier = new Resource.Contributors.Contributor.NameIdentifier();
        nameIdentifier.setValue(userAgent.getIdentifier());
        if(userAgent.getIdentifierScheme() != null){
          nameIdentifier.setNameIdentifierScheme(userAgent.getIdentifierScheme().getSchemeId());
          nameIdentifier.setSchemeURI(userAgent.getIdentifierScheme().getSchemeUri());
        }
        result.getNameIdentifier().add(nameIdentifier);
      }
    } else if(contributor.getAgent().getAgentType().equals(Group.GROUP_AGENT_TYPE)){
      Group groupAgent = (Group) contributor.getAgent();
      Resource.Contributors.Contributor.NameIdentifier nameIdentifier = new Resource.Contributors.Contributor.NameIdentifier();
      nameIdentifier.setValue(groupAgent.getIdentifier());
      if(groupAgent.getIdentifierScheme() != null){
        nameIdentifier.setNameIdentifierScheme(groupAgent.getIdentifierScheme().getSchemeId());
        nameIdentifier.setSchemeURI(groupAgent.getIdentifierScheme().getSchemeUri());
      }
      result.getNameIdentifier().add(nameIdentifier);
    }

    try{
      result.setContributorType(ContributorType.fromValue(contributor.getContributionType().getValue()));
    } catch(IllegalArgumentException ex){
      result.setContributorType(ContributorType.OTHER);
    }

    return result;
  }

  private static Resource.Dates.Date transformDate(Date date){
    Resource.Dates.Date result = new Resource.Dates.Date();
    result.setValue(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").format(date.getDate()));
    try{
      result.setDateType(DateType.fromValue(date.getType().getValue()));
    } catch(IllegalArgumentException ex){
      //no date type available
    }

    return result;
  }

  private static Resource.AlternateIdentifiers.AlternateIdentifier transformAlternateIdentifier(Identifier identifier){
    Resource.AlternateIdentifiers.AlternateIdentifier result = new Resource.AlternateIdentifiers.AlternateIdentifier();
    if(identifier.getIdentifierType() != null){
      result.setAlternateIdentifierType(identifier.getIdentifierType().getValue());
    } else{
      result.setAlternateIdentifierType(Identifier.IDENTIFIER_TYPE.OTHER.getValue());
    }
    result.setValue(identifier.getValue());
    return result;
  }

  private static Resource.RelatedIdentifiers.RelatedIdentifier transformRelatedIdentifier(RelatedIdentifier identifier){
    Resource.RelatedIdentifiers.RelatedIdentifier result = new Resource.RelatedIdentifiers.RelatedIdentifier();
    if(identifier.getIdentifierType() != null){
      try{
        result.setRelatedIdentifierType(RelatedIdentifierType.fromValue(identifier.getIdentifierType().getValue()));
      } catch(IllegalArgumentException ex){
        //no type for 'Other' or 'Internal'
        result.setRelatedIdentifierType(null);
      }
    } else{
      result.setRelatedIdentifierType(null);
    }

    result.setRelationType(RelationType.fromValue(identifier.getRelationType().getValue()));

    if(identifier.getScheme() != null){
      result.setSchemeType(identifier.getScheme().getSchemeId());
      result.setSchemeURI(identifier.getScheme().getSchemeUri());
    }
    result.setRelatedMetadataScheme(identifier.getRelatedMetadataScheme());
    result.setValue(identifier.getValue());
    return result;
  }

  private static Resource.RightsList.Rights transformRights(Scheme rights){
    Resource.RightsList.Rights result = new Resource.RightsList.Rights();
    result.setRightsURI(rights.getSchemeUri());
    result.setValue(rights.getSchemeId());
    return result;
  }

  private static Resource.Descriptions.Description transformDescription(Description description){
    Resource.Descriptions.Description result = new Resource.Descriptions.Description();
    try{
      result.setDescriptionType(DescriptionType.fromValue(description.getType().getValue()));
    } catch(IllegalArgumentException ex){
      //use OTHER as fallback
      result.setDescriptionType(DescriptionType.OTHER);
    }
    result.setLang(description.getLang());
    result.getContent().add(description.getDescription());
    return result;
  }

  private static Resource.GeoLocations.GeoLocation transformGeolocation(GeoLocation geolocation){
    Resource.GeoLocations.GeoLocation result = new Resource.GeoLocations.GeoLocation();
    result.setGeoLocationPlace(geolocation.getPlace());
    if(geolocation.getPoint() != null){
      org.datacite.schema.kernel_4.Point point = new org.datacite.schema.kernel_4.Point();
      point.setPointLatitude(geolocation.getPoint().getLatitude());
      point.setPointLongitude(geolocation.getPoint().getLongitude());
      result.setGeoLocationPoint(point);
    } else if(geolocation.getBox() != null){
      Box box = new Box();
      box.setEastBoundLongitude(geolocation.getBox().getEastLongitude());
      box.setWestBoundLongitude(geolocation.getBox().getWestLongitude());
      box.setNorthBoundLatitude(geolocation.getBox().getNorthLatitude());
      box.setSouthBoundLatitude(geolocation.getBox().getSouthLatitude());
      result.setGeoLocationBox(box);
    } else if(geolocation.getPolygon() != null){
      Resource.GeoLocations.GeoLocation.GeoLocationPolygon polygon = new Resource.GeoLocations.GeoLocation.GeoLocationPolygon();
      geolocation.getPolygon().getPoints().stream().map((point) -> {
        org.datacite.schema.kernel_4.Point p = new org.datacite.schema.kernel_4.Point();
        p.setPointLatitude(point.getLatitude());
        p.setPointLongitude(point.getLongitude());
        return p;
      }).forEachOrdered((p) -> {
        polygon.getPolygonPoint().add(p);
      });
      return result;
    }

    return result;
  }

  private static Resource.FundingReferences.FundingReference transformFundingReference(FundingReference reference){
    Resource.FundingReferences.FundingReference result = new Resource.FundingReferences.FundingReference();
    result.setAwardTitle(reference.getAwardTitle());
    result.setFunderName(reference.getFunderName());
    if(reference.getAwardNumber() != null){
      Resource.FundingReferences.FundingReference.AwardNumber awardNumber = new Resource.FundingReferences.FundingReference.AwardNumber();
      awardNumber.setAwardURI(reference.getAwardNumber().getSchemeUri());
      awardNumber.setValue(reference.getAwardNumber().getSchemeId());
      result.setAwardNumber(awardNumber);
    }

    if(reference.getFunderIdentifier() != null){
      Resource.FundingReferences.FundingReference.FunderIdentifier funderIdentifier = new Resource.FundingReferences.FundingReference.FunderIdentifier();
      try{
        funderIdentifier.setFunderIdentifierType(FunderIdentifierType.fromValue(reference.getFunderIdentifier().getIdentifierType().getValue()));
      } catch(IllegalArgumentException ex){
        //fallback to OTHER
        funderIdentifier.setFunderIdentifierType(FunderIdentifierType.OTHER);
      }
      funderIdentifier.setValue(reference.getFunderIdentifier().getValue());
      result.setFunderIdentifier(funderIdentifier);
    }
    return result;
  }

  public static DataResource fromDataCite(Resource resource){
    DataResource res = new DataResource();
    return res;
  }

}
