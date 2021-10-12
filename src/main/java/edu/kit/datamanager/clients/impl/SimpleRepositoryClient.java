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
package edu.kit.datamanager.clients.impl;

import edu.kit.datamanager.clients.SimpleServiceClient;
import edu.kit.datamanager.clients.SimpleServiceClient.ResultPage;
import edu.kit.datamanager.entities.repo.ContentInformation;
import edu.kit.datamanager.entities.repo.DataResource;
import edu.kit.datamanager.util.ControllerUtils;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

/**
 *
 * @author jejkal
 */
public class SimpleRepositoryClient{

  private final static Logger LOGGER = LoggerFactory.getLogger(SimpleRepositoryClient.class);
  private final String resourceBaseUrl;
  private final String bearerToken;

  SimpleRepositoryClient(String resourceBaseUrl, String bearerToken){
    this.resourceBaseUrl = resourceBaseUrl;
    this.bearerToken = bearerToken;
  }

  /**
   * Create repository client.
   * @param resourceBaseUrl Base URL of repo.
   * @return Repository client.
   */
  public static SimpleRepositoryClient create(String resourceBaseUrl){
    return create(resourceBaseUrl, null);
  }

  /**
   * Create repository client.
   * @param resourceBaseUrl Base URL of repo.
   * @param bearerToken Bearer token.
   * @return Repository client.
   */
  public static SimpleRepositoryClient create(String resourceBaseUrl, String bearerToken){
    return new SimpleRepositoryClient(resourceBaseUrl, bearerToken);
  }

  /**
   * Get resource.
   * @param resourceId Id of resource.
   * @return Resource.
   */
  public DataResource getResource(String resourceId){
    return SimpleServiceClient.create(resourceBaseUrl).withResourcePath(resourceId).accept(MediaType.APPLICATION_JSON).withBearerToken(bearerToken).getResource(DataResource.class);
  }

  /**
   * Get multiple resources.
   * @param page Page number.
   * @param elementsPerPage Elements per page.
   * @param fields Sorting fields.
   * @return Page holding resources.
   */
  public ResultPage<DataResource> getResources(int page, int elementsPerPage, SimpleServiceClient.SortField... fields){
    return getResources(null, page, elementsPerPage, fields);
  }

  /**
   * Find resources by example.
   * @param example Example.
   * @param page Page number.
   * @param elementsPerPage Elements per page.
   * @param fields Sorting fields.
   * @return Page holding resources.
   */
  public ResultPage<DataResource> getResources(DataResource example, int page, int elementsPerPage, SimpleServiceClient.SortField... fields){
    SimpleServiceClient client = SimpleServiceClient.
            create(resourceBaseUrl).
            accept(MediaType.APPLICATION_JSON).
            withBearerToken(bearerToken).
            withQueryParam("page", Integer.toString(page)).
            withQueryParam("size", Integer.toString(elementsPerPage));

    if(fields != null){
      for(SimpleServiceClient.SortField field : fields){
        client = client.withQueryParam("sort", field.toQueryParam());
      }
    }

    if(example == null){
      return client.getResources(DataResource[].class);
    }
    return client.findResources(example, DataResource[].class);
  }

  /**
   * Get data from repository.
   * @param resourceId Id of resource.
   * @param relativePath Path of resource.
   * @param stream Stream holding data.
   * @return Status.
   */
  public int getData(String resourceId, String relativePath, OutputStream stream){
    return SimpleServiceClient.create(resourceBaseUrl).withResourcePath(resourceId + "/data/" + relativePath).accept(MediaType.APPLICATION_OCTET_STREAM).withBearerToken(bearerToken).getResource(stream);
  }

  /**
   * Upload data to repository.
   * @param resourceId Id of resource.
   * @param relativePath Path of resource.
   * @param stream Stream holding data.
   * @param force Overwrite existing data.
   * @return HTTP status.
   * @throws IOException Error while writing data.
   */
  public HttpStatus uploadData(String resourceId, String relativePath, InputStream stream, boolean force) throws IOException{
    return uploadData(resourceId, relativePath, stream, null, force);
  }

  /**
   *
   * Upload data to repository.
   * @param resourceId Id of resource.
   * @param relativePath Path of resource.
   * @param stream Stream holding data.
   * @param metadata Metadata describing data.
   * @param force Overwrite existing data.
   * @return HTTP status.
   * @throws IOException Error while writing data.
   */
  public HttpStatus uploadData(String resourceId, String relativePath, InputStream stream, ContentInformation metadata, boolean force) throws IOException{
    return SimpleServiceClient.create(resourceBaseUrl).withResourcePath(resourceId + "/data/" + relativePath).withBearerToken(bearerToken).withQueryParam("force", Boolean.toString(force)).withFormParam("file", stream).withFormParam("metadata", metadata).postForm();
  }

  /**
   *
   * Upload data to repository.
   * @param resourceId Id of resource.
   * @param relativePath Path of resource.
   * @param file File holding data.
   * @param force Overwrite existing data.
   * @return HTTP status.
   * @throws IOException Error while writing data.
   */
  public HttpStatus uploadData(String resourceId, String relativePath, File file, boolean force) throws IOException{
    return uploadData(resourceId, relativePath, file, null, force);
  }

  /**
   *
   * Upload data to repository.
   * @param resourceId Id of resource.
   * @param relativePath Path of resource.
   * @param file File holding data.
    * @param metadata Metadata describing data.
  * @param force Overwrite existing data.
   * @return HTTP status.
   * @throws IOException Error while writing data.
   */
  public HttpStatus uploadData(String resourceId, String relativePath, File file, ContentInformation metadata, boolean force) throws IOException{
    return SimpleServiceClient.create(resourceBaseUrl).withResourcePath(resourceId + "/data/" + relativePath).withQueryParam("force", Boolean.toString(force)).withBearerToken(bearerToken).withFormParam("file", file).withFormParam("metadata", metadata).postForm();
  }

  /**
   * Get content information.
   * @param resourceId Id of resource
   * @param relativePath Relative path of data.
   * @return Content information.
   */
  public ContentInformation[] getContentInformation(String resourceId, String relativePath){
    if(relativePath == null || relativePath.endsWith("/")){
      return SimpleServiceClient.create(resourceBaseUrl).withResourcePath(resourceId + "/data/" + relativePath).accept(ContentInformation.CONTENT_INFORMATION_MEDIA_TYPE).withBearerToken(bearerToken).getResource(ContentInformation[].class);
    }
    return new ContentInformation[]{SimpleServiceClient.create(resourceBaseUrl).withResourcePath(resourceId + "/data/" + relativePath).accept(ContentInformation.CONTENT_INFORMATION_MEDIA_TYPE).withBearerToken(bearerToken).getResource(ContentInformation.class)};
  }

  /**
   * Create resource.
   * @param resource Resource.
   * @return Created resource.
   */
  public DataResource createResource(DataResource resource){
    return SimpleServiceClient.create(resourceBaseUrl).withContentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).withBearerToken(bearerToken).postResource(resource, DataResource.class);
  }

  /**
   * Update resource.
   * @param resource Resource.
   * @return Updated resource.
   */
  public DataResource updateResource(DataResource resource){
    return SimpleServiceClient.create(resourceBaseUrl).withContentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).withBearerToken(bearerToken).withResourcePath(resource.getId()).putResource(resource, DataResource.class);
  }

  /**
   * Delete resource.
   * @param resourceId Id of resource.
   */
  public void deleteResource(String resourceId){
    SimpleServiceClient.create(resourceBaseUrl).withResourcePath(resourceId).withContentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).withBearerToken(bearerToken).deleteResource();
  }

//  public static void main(String[] args) throws Exception{
//       String baseUrl = "http://localhost:8090/api/v1/schemas/";
//    String resourceId = "35dd5dcc-9b98-4c0d-a964-e353b0395411";
//
//    System.out.println(SimpleRepositoryClient.create(baseUrl).getResource(resourceId));
//    ByteArrayOutputStream bout = new ByteArrayOutputStream();
//    System.out.println(SimpleRepositoryClient.create(baseUrl).getData(resourceId, "generated/35dd5dcc-9b98-4c0d-a964-e353b0395411_metadata.elastic.json", bout));
//    System.out.println(bout.toString());
//    System.out.println(SimpleRepositoryClient.create(baseUrl).getContentInformation(resourceId, "generated/35dd5dcc-9b98-4c0d-a964-e353b0395411_metadata.elastic.json"));
//    System.out.println(SimpleRepositoryClient.create(baseUrl).uploadData(resourceId, "generated/myFile.json", new File("README.md"), null));
//    System.out.println(SimpleRepositoryClient.create(baseUrl).uploadData(resourceId, "generated/myFile1.json", new ByteArrayInputStream("Success".getBytes())));
//  }
}
