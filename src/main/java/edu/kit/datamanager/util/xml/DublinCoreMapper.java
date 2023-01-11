/*
 * Copyright 2019 Karlsruhe Institute of Technology.
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
package edu.kit.datamanager.util.xml;

import edu.kit.datamanager.entities.repo.Agent;
import edu.kit.datamanager.entities.repo.Box;
import edu.kit.datamanager.entities.repo.Contributor;
import edu.kit.datamanager.entities.repo.DataResource;
import edu.kit.datamanager.entities.repo.Date;
import edu.kit.datamanager.entities.repo.Description;
import edu.kit.datamanager.entities.repo.FunderIdentifier;
import edu.kit.datamanager.entities.repo.FundingReference;
import edu.kit.datamanager.entities.repo.GeoLocation;
import edu.kit.datamanager.entities.repo.Identifier;
import edu.kit.datamanager.entities.repo.Point;
import edu.kit.datamanager.entities.repo.Polygon;
import edu.kit.datamanager.entities.repo.PrimaryIdentifier;
import edu.kit.datamanager.entities.repo.RelatedIdentifier;
import edu.kit.datamanager.entities.repo.ResourceType;
import edu.kit.datamanager.entities.repo.Scheme;
import edu.kit.datamanager.entities.repo.Subject;
import edu.kit.datamanager.entities.repo.Title;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import org.purl.dc.elements._1.ElementContainer;
import org.purl.dc.elements._1.ObjectFactory;
import org.purl.dc.elements._1.SimpleLiteral;

/**
 * Mapper from internal DataCite format to DublinCore.
 *
 * @author jejkal
 */
public class DublinCoreMapper{

  private static ObjectFactory factory = new ObjectFactory();
  private static DateTimeFormatter fmt = DateTimeFormatter.ISO_DATE_TIME.withZone(ZoneOffset.UTC);

  public static ElementContainer dataResourceToDublinCoreContainer(DataResource resource){
    ElementContainer container = new ElementContainer();
    mapTitles(resource, container);
    mapCreators(resource, container);
    mapSubjects(resource, container);
    mapDescriptions(resource, container);
    mapPublisher(resource, container);
    mapContributors(resource, container);
    mapDates(resource, container);
    mapResourceType(resource, container);
    mapFormats(resource, container);
    mapIdentifier(resource, container);
    mapRelatedIdentifiers(resource, container);
    mapLanguage(resource, container);
    mapRights(resource, container);
    return container;
  }

  private static void mapTitles(DataResource resource, ElementContainer container){
    for(Title t : resource.getTitles()){
      container.getAny().add(factory.createTitle(createSimpleLiteral(t.getValue(), t.getLang())));
    }
  }

  private static void mapCreators(DataResource resource, ElementContainer container){
    for(Agent a : resource.getCreators()){
      if(a.getGivenName() != null && a.getFamilyName() != null){
        container.getAny().add(factory.createCreator(createSimpleLiteral(a.getFamilyName() + ", " + a.getGivenName())));
      } else if(a.getGivenName() == null && a.getFamilyName() == null){
        //skip element
      } else if(a.getGivenName() == null){
        container.getAny().add(factory.createCreator(createSimpleLiteral(a.getFamilyName())));
      } else{
        container.getAny().add(factory.createCreator(createSimpleLiteral(a.getGivenName())));
      }
    }
  }

  private static void mapSubjects(DataResource resource, ElementContainer container){
    for(Subject s : resource.getSubjects()){
      container.getAny().add(factory.createSubject(createSimpleLiteral(s.getValue(), s.getLang())));
    }
  }

  private static void mapDescriptions(DataResource resource, ElementContainer container){
    for(Description d : resource.getDescriptions()){
      container.getAny().add(factory.createDescription(createSimpleLiteral(d.getDescription(), d.getLang())));
    }
  }

  private static void mapPublisher(DataResource resource, ElementContainer container){
    if(resource.getPublisher() != null){
      container.getAny().add(factory.createPublisher(createSimpleLiteral(resource.getPublisher())));
    }
  }

  private static void mapContributors(DataResource resource, ElementContainer container){
    for(Contributor d : resource.getContributors()){
      Agent a = d.getUser();
      if(a.getGivenName() != null && a.getFamilyName() != null){
        container.getAny().add(factory.createContributor(createSimpleLiteral(a.getFamilyName() + ", " + a.getGivenName())));
      } else if(a.getGivenName() == null && a.getFamilyName() == null){
        //skip element
      } else if(a.getGivenName() == null){
        container.getAny().add(factory.createContributor(createSimpleLiteral(a.getFamilyName())));
      } else{
        container.getAny().add(factory.createContributor(createSimpleLiteral(a.getGivenName())));
      }
    }
  }

  private static void mapDates(DataResource resource, ElementContainer container){
    for(Date d : resource.getDates()){
      container.getAny().add(factory.createDate(createSimpleLiteral(fmt.format(d.getValue()))));
    }
  }

  private static void mapResourceType(DataResource resource, ElementContainer container){
    if(resource.getResourceType() != null){
      container.getAny().add(factory.createType(createSimpleLiteral(resource.getResourceType().getTypeGeneral().getValue())));
    }
  }

  private static void mapFormats(DataResource resource, ElementContainer container){
    for(String f : resource.getFormats()){
      container.getAny().add(factory.createFormat(createSimpleLiteral(f)));
    }
  }

  private static void mapIdentifier(DataResource resource, ElementContainer container){
    if(resource.getIdentifier() != null){
      container.getAny().add(factory.createIdentifier(createSimpleLiteral(resource.getIdentifier().getValue())));
    }
  }

  private static void mapRelatedIdentifiers(DataResource resource, ElementContainer container){
    for(RelatedIdentifier i : resource.getRelatedIdentifiers()){
      if(RelatedIdentifier.RELATION_TYPES.IS_NEW_VERSION_OF.equals(i.getRelationType())
              || RelatedIdentifier.RELATION_TYPES.IS_DERIVED_FROM.equals(i.getRelationType())){
        container.getAny().add(factory.createSource(createSimpleLiteral(i.getValue())));
      } else{
        container.getAny().add(factory.createRelation(createSimpleLiteral(i.getValue())));
      }
    }
  }

  private static void mapLanguage(DataResource resource, ElementContainer container){
    if(resource.getLanguage() != null){
      container.getAny().add(factory.createLanguage(createSimpleLiteral(resource.getLanguage())));
    }
  }

  private static void mapRights(DataResource resource, ElementContainer container){
    for(Scheme r : resource.getRights()){
      container.getAny().add(factory.createRights(createSimpleLiteral(r.getSchemeUri())));
    }
  }

  private static SimpleLiteral createSimpleLiteral(String value){
    return createSimpleLiteral(value, null);
  }

  private static SimpleLiteral createSimpleLiteral(String value, String lang){
    SimpleLiteral lit = factory.createSimpleLiteral();
    lit.getContent().add(value);
    lit.setLang(lang);
    return lit;
  }

  public static void main(String[] args) throws Exception{
    DataResource res = new DataResource();
//
    PrimaryIdentifier id = new PrimaryIdentifier();
    id.setIdentifierType("DOI");
    id.setValue("test");
    res.setIdentifier(id);
//
    Identifier alt = new Identifier();
    alt.setIdentifierType(Identifier.IDENTIFIER_TYPE.DOI);
    alt.setValue("altTest");
    res.getAlternateIdentifiers().add(alt);
//
    RelatedIdentifier rel = new RelatedIdentifier();
    rel.setIdentifierType(RelatedIdentifier.RELATED_IDENTIFIER_TYPE.HANDLE);
    rel.setRelationType(RelatedIdentifier.RELATION_TYPES.DOCUMENTS);
    rel.setRelatedMetadataScheme("scheme1");
    rel.setValue("relatedDocument");
    Scheme s = new Scheme();
    s.setSchemeId("scheme1");
    s.setSchemeUri("http://heise.de");
    rel.setScheme(s);
    res.getRelatedIdentifiers().add(rel);

    RelatedIdentifier relSource = new RelatedIdentifier();
    relSource.setIdentifierType(RelatedIdentifier.RELATED_IDENTIFIER_TYPE.HANDLE);
    relSource.setRelationType(RelatedIdentifier.RELATION_TYPES.IS_DERIVED_FROM);
    relSource.setRelatedMetadataScheme("scheme1");
    relSource.setValue("ThisIsASource");
    res.getRelatedIdentifiers().add(relSource);
///
    Agent a = new Agent();
    a.setGivenName("Thomas");
    a.setFamilyName("Jejkal");
    res.getCreators().add(a);
//
    Title t = new Title();
    t.setLang("en");
    t.setTitleType(Title.TITLE_TYPE.SUBTITLE);
    t.setValue("This is the title");
    res.getTitles().add(t);
//
    ResourceType type = new ResourceType();
    type.setTypeGeneral(ResourceType.TYPE_GENERAL.DATASET);
    type.setValue("Custom Dataset");
    res.setResourceType(type);
//
    Contributor con = new Contributor();
    con.setUser(a);
    con.setContributionType(Contributor.CONTRIBUTOR_TYPE.DATA_COLLECTOR);
    res.getContributors().add(con);
//
    Subject su = new Subject();
    su.setLang("en");
    su.setValue("My Subject");
    su.setValueUri("http://google.com");
    Scheme s2 = new Scheme();
    s2.setSchemeId("scheme2");
    s2.setSchemeUri("http://golem.de");
    su.setScheme(s2);
    res.getSubjects().add(su);
//
    Date d = new Date();
    d.setType(Date.DATE_TYPE.ACCEPTED);
    d.setValue(Instant.now().truncatedTo( ChronoUnit.MILLIS ));
    res.getDates().add(d);
//
    res.getSizes().add("test");
    res.getFormats().add("image/jpg");
//
    Description des = new Description();
    des.setLang("en");
    des.setDescription("This is the decriptions");
    des.setType(Description.TYPE.ABSTRACT);
    res.getDescriptions().add(des);
// 
    Scheme rights = new Scheme();
    rights.setSchemeUri("http://apachecommons.org");
    rights.setSchemeId("Apache 2.0");
    res.getRights().add(rights);
//
    FundingReference ref = new FundingReference();
    Scheme award = new Scheme();
    award.setSchemeId("awardId");
    award.setSchemeUri("http://bmbf.de");
    ref.setAwardNumber(award);
    ref.setFunderName("BMBF");
    ref.setAwardTitle("BMBF Award");
    FunderIdentifier fid = new FunderIdentifier();
    fid.setValue("BMBF");
    fid.setType(FunderIdentifier.FUNDER_TYPE.GRID);
    ref.setFunderIdentifier(fid);
    res.getFundingReferences().add(ref);
//
    GeoLocation geo = new GeoLocation();
    Point po = new Point();
    po.setLatitude(1.0f);
    po.setLongitude(2.0f);
    geo.setPoint(po);
    geo.setPlace("Karlsruhe");

    Box box = new Box();
    box.setEastLongitude(0.0f);
    box.setNorthLatitude(1.0f);
    box.setSouthLatitude(2.0f);
    box.setWestLongitude(3.0f);
    geo.setBox(box);
    Polygon poly = new Polygon();
    Point p1 = new Point();
    p1.setLatitude(1.0f);
    p1.setLongitude(2.0f);
    Point p2 = new Point();
    p2.setLatitude(3.0f);
    p2.setLongitude(4.0f);
    Point p3 = new Point();
    p3.setLatitude(5.0f);
    p3.setLongitude(6.0f);
    poly.getPoints().add(p1);
    poly.getPoints().add(p2);
    poly.getPoints().add(p3);
    geo.setPolygon(poly);
    res.getGeoLocations().add(geo);

    res.setLanguage("en");
    ElementContainer cont = DublinCoreMapper.dataResourceToDublinCoreContainer(res);

    JAXBContext jaxbContext = JAXBContext.newInstance(ElementContainer.class);
    Marshaller marshaller = jaxbContext.createMarshaller();
    marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

    marshaller.marshal(cont, System.out);
  }

}
