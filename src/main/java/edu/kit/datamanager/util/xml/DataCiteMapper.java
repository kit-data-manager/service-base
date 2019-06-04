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

import edu.kit.datamanager.entities.repo.DataResource;
import edu.kit.datamanager.util.xml.converters.AlternateIdentifierConverter;
import edu.kit.datamanager.util.xml.converters.ContributorsConverter;
import edu.kit.datamanager.util.xml.converters.CreatorsConverter;
import edu.kit.datamanager.util.xml.converters.DatesConverter;
import edu.kit.datamanager.util.xml.converters.DescriptionsConverter;
import edu.kit.datamanager.util.xml.converters.FormatsConverter;
import edu.kit.datamanager.util.xml.converters.FundingReferencesConverter;
import edu.kit.datamanager.util.xml.converters.GeoLocationsConverter;
import edu.kit.datamanager.util.xml.converters.RelatedIdentifierConverter;
import edu.kit.datamanager.util.xml.converters.ResourceTypeConverter;
import edu.kit.datamanager.util.xml.converters.RightsConverter;
import edu.kit.datamanager.util.xml.converters.SizesConverter;
import edu.kit.datamanager.util.xml.converters.SubjectsConverter;
import edu.kit.datamanager.util.xml.converters.TitlesConverter;
import org.datacite.schema.kernel_4.Resource;
import org.dozer.DozerBeanMapper;
import org.dozer.loader.api.BeanMappingBuilder;
import org.dozer.loader.api.FieldsMappingOptions;

/**
 *
 * @author jejkal
 */
public class DataCiteMapper{

  private static DozerBeanMapper mapper;
  private static boolean INITIALIZED = false;

  public static Resource dataResourceToDataciteResource(DataResource resource){
    if(!INITIALIZED){
      mapper = new DozerBeanMapper();
      BeanMappingBuilder builder = new BeanMappingBuilder(){
        protected void configure(){
          mapping(DataResource.class, Resource.class).
                  fields("alternateIdentifiers", "alternateIdentifiers", FieldsMappingOptions.customConverter(AlternateIdentifierConverter.class)).
                  fields("relatedIdentifiers", "relatedIdentifiers", FieldsMappingOptions.customConverter(RelatedIdentifierConverter.class)).
                  fields("creators", "creators", FieldsMappingOptions.customConverter(CreatorsConverter.class)).
                  fields("titles", "titles", FieldsMappingOptions.customConverter(TitlesConverter.class)).
                  fields("resourceType", "resourceType", FieldsMappingOptions.customConverter(ResourceTypeConverter.class)).
                  fields("contributors", "contributors", FieldsMappingOptions.customConverter(ContributorsConverter.class)).
                  fields("subjects", "subjects", FieldsMappingOptions.customConverter(SubjectsConverter.class)).
                  fields("dates", "dates", FieldsMappingOptions.customConverter(DatesConverter.class)).
                  fields("formats", "formats", FieldsMappingOptions.customConverter(FormatsConverter.class)).
                  fields("sizes", "sizes", FieldsMappingOptions.customConverter(SizesConverter.class)).
                  fields("descriptions", "descriptions", FieldsMappingOptions.customConverter(DescriptionsConverter.class)).
                  fields("rights", "rightsList", FieldsMappingOptions.customConverter(RightsConverter.class)).
                  fields("fundingReferences", "fundingReferences", FieldsMappingOptions.customConverter(FundingReferencesConverter.class)).
                  fields("geoLocations", "geoLocations", FieldsMappingOptions.customConverter(GeoLocationsConverter.class));
        }
      };
      mapper.addMapping(builder);
      INITIALIZED = true;
    }

    return mapper.map(resource, Resource.class);
  }

//  public static void main(String[] args) throws Exception{
//    DataResource res = new DataResource();
////
//    PrimaryIdentifier id = new PrimaryIdentifier();
//    id.setIdentifierType("DOI");
//    id.setValue("test");
//    res.setIdentifier(id);
////
//    Identifier alt = new Identifier();
//    alt.setIdentifierType(Identifier.IDENTIFIER_TYPE.DOI);
//    alt.setValue("altTest");
//    res.getAlternateIdentifiers().add(alt);
////
//    RelatedIdentifier rel = new RelatedIdentifier();
//    rel.setIdentifierType(RelatedIdentifier.RELATED_IDENTIFIER_TYPE.HANDLE);
//    rel.setRelationType(RelatedIdentifier.RELATION_TYPES.DOCUMENTS);
//    rel.setRelatedMetadataScheme("scheme1");
//    rel.setValue("relatedDocument");
//    Scheme s = new Scheme();
//    s.setSchemeId("scheme1");
//    s.setSchemeUri("http://heise.de");
//    rel.setScheme(s);
//    res.getRelatedIdentifiers().add(rel);
/////
//    Agent a = new Agent();
//    a.setGivenName("Thomas");
//    a.setFamilyName("Jejkal");
//    res.getCreators().add(a);
////
//    Title t = new Title();
//    t.setLang("en");
//    t.setTitleType(Title.TITLE_TYPE.SUBTITLE);
//    t.setValue("This is the title");
//    res.getTitles().add(t);
////
//    ResourceType type = new ResourceType();
//    type.setTypeGeneral(ResourceType.TYPE_GENERAL.DATASET);
//    type.setValue("Custom Dataset");
//    res.setResourceType(type);
////
//    Contributor con = new Contributor();
//    con.setUser(a);
//    con.setContributionType(Contributor.CONTRIBUTOR_TYPE.DATA_COLLECTOR);
//    res.getContributors().add(con);
////
//    Subject su = new Subject();
//    su.setLang("en");
//    su.setValue("My Subject");
//    su.setValueUri("http://google.com");
//    Scheme s2 = new Scheme();
//    s2.setSchemeId("scheme2");
//    s2.setSchemeUri("http://golem.de");
//    su.setScheme(s2);
//    res.getSubjects().add(su);
////
//    Date d = new Date();
//    d.setType(Date.DATE_TYPE.ACCEPTED);
//    d.setValue(Instant.now());
//    res.getDates().add(d);
////
//    res.getSizes().add("test");
//    res.getFormats().add("image/jpg");
////
//    Description des = new Description();
//    des.setLang("en");
//    des.setDescription("This is the decriptions");
//    des.setType(Description.TYPE.ABSTRACT);
//    res.getDescriptions().add(des);
//// 
//    Scheme rights = new Scheme();
//    rights.setSchemeUri("http://apachecommons.org");
//    rights.setSchemeId("Apache 2.0");
//    res.getRights().add(rights);
////
//    FundingReference ref = new FundingReference();
//    Scheme award = new Scheme();
//    award.setSchemeId("awardId");
//    award.setSchemeUri("http://bmbf.de");
//    ref.setAwardNumber(award);
//    ref.setFunderName("BMBF");
//    ref.setAwardTitle("BMBF Award");
//    FunderIdentifier fid = new FunderIdentifier();
//    fid.setValue("BMBF");
//    fid.setType(FunderIdentifier.FUNDER_TYPE.GRID);
//    ref.setFunderIdentifier(fid);
//    res.getFundingReferences().add(ref);
////
//    GeoLocation geo = new GeoLocation();
//    Point po = new Point();
//    po.setLatitude(1.0f);
//    po.setLongitude(2.0f);
//    geo.setPoint(po);
//    geo.setPlace("Karlsruhe");
//
//    Box box = new Box();
//    box.setEastLongitude(0.0f);
//    box.setNorthLatitude(1.0f);
//    box.setSouthLatitude(2.0f);
//    box.setWestLongitude(3.0f);
//    geo.setBox(box);
//    Polygon poly = new Polygon();
//    Point p1 = new Point();
//    p1.setLatitude(1.0f);
//    p1.setLongitude(2.0f);
//    Point p2 = new Point();
//    p2.setLatitude(3.0f);
//    p2.setLongitude(4.0f);
//    Point p3 = new Point();
//    p3.setLatitude(5.0f);
//    p3.setLongitude(6.0f);
//    poly.getPoints().add(p1);
//    poly.getPoints().add(p2);
//    poly.getPoints().add(p3);
//    geo.setPolygon(poly);
//    res.getGeoLocations().add(geo);
//
//    Resource destObject = dataResourceToDataciteResource(res);
//
//    JAXBContext jaxbContext = JAXBContext.newInstance(Resource.class);
//    Marshaller marshaller = jaxbContext.createMarshaller();
//    marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
//
//    marshaller.marshal(destObject, new FileOutputStream("datacite.xml"));
//
//  }
}
