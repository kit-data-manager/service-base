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
package edu.kit.datamanager.clients;

import edu.kit.datamanager.entities.repo.DataResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author jejkal
 */
public class SimpleRepositoryClient{

  private final static Logger LOGGER = LoggerFactory.getLogger(SimpleRepositoryClient.class);

  private final String resourceBaseUrl;
  private String bearerToken = null;

  private SimpleRepositoryClient(String resourceBaseUrl){
    this.resourceBaseUrl = resourceBaseUrl;
  }

  public static SimpleRepositoryClient createClient(String resourceBaseUrl){
    LOGGER.trace("Creating SimpleResourceClient with base URL {}.", resourceBaseUrl);
    return new SimpleRepositoryClient(resourceBaseUrl);
  }

  public SimpleRepositoryClient withBearerToken(String bearerToken){
    this.bearerToken = bearerToken;
    return this;
  }

  public SingleResourceAccessClient withResourceId(String resourceId){
    LOGGER.trace("Creating SingleResourceAccessClient with resourceId {}.", resourceId);
    return new SingleResourceAccessClient(resourceBaseUrl, resourceId, bearerToken);
  }

  public MultiResourceAccessClient elementsPerPage(int elementsPerPage){
    LOGGER.trace("Creating SingleResourceAccessClient with {} elements per page.", elementsPerPage);
    return new MultiResourceAccessClient(resourceBaseUrl, bearerToken).elementsPerPage(elementsPerPage);
  }

  public MultiResourceAccessClient page(int page){
    LOGGER.trace("Creating MultiResourceAccessClient with page {}.", page);
    return new MultiResourceAccessClient(resourceBaseUrl, bearerToken).page(page);
  }

  public DataResource createResource(DataResource resource){
    return new SingleResourceAccessClient(resourceBaseUrl, bearerToken).createResource(resource);
  }

//  public static void main(String[] args) throws Exception{
//    System.out.println(SimpleRepositoryClient.createClient("http://localhost:8090/api/v1/dataresources/").accept(MediaType.APPLICATION_JSON).withResourceId("f241b201-aed3-4753-a051-a349caf21fe5").getResource());
    // System.out.println(SimpleRepositoryClient.createClient("http://localhost:8090/api/v1/dataresources/").page(0).getResources());
    //  System.out.println(SimpleRepositoryClient.createClient("http://localhost:8090/api/v1/dataresources/").withResourceId("f241b201-aed3-4753-a051-a349caf21fe5").getContentInformation("generated/f241b201-aed3-4753-a051-a349caf21fe5_metadata.elastic.json"));
//    ContentInformation metadata = new ContentInformation();
//    metadata.getTags().add("file");
//    metadata.getTags().add("uploadClient");
//
//    SimpleRepositoryClient.createClient("http://localhost:8090/api/v1/dataresources/").withResourceId("f241b201-aed3-4753-a051-a349caf21fe5").withFile(new File("/Users/jejkal/Downloads/810434162_3053947.pdf")).withMetadata(metadata).upload("myFile2.pdf");
//    
//    ByteArrayOutputStream bout = new ByteArrayOutputStream();
//    System.out.println(SimpleRepositoryClient.createClient("http://localhost:8090/api/v1/dataresources/").withResourceId("35dd5dcc-9b98-4c0d-a964-e353b0395411").toStream(bout).download("generated/35dd5dcc-9b98-4c0d-a964-e353b0395411_metadata.elastic.json"));
//    
//    System.out.println(bout.toString());
//    
//  }
}
