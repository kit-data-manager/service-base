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

import edu.kit.datamanager.entities.repo.ContentInformation;
import edu.kit.datamanager.entities.repo.DataResource;
import java.io.File;
import java.io.InputStream;
import java.util.Arrays;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

/**
 *
 * @author jejkal
 */
public class SingleResourceAccessClient{

  private final static Logger LOGGER = LoggerFactory.getLogger(SingleResourceAccessClient.class);

  private RestTemplate restTemplate = new RestTemplate();
  private HttpHeaders headers;
  private final String resourceBaseUrl;
  private String resourceId;
  private String bearerToken;

  public SingleResourceAccessClient(String resourceBaseUrl){
    this.resourceBaseUrl = resourceBaseUrl;
  }

  public SingleResourceAccessClient(String resourceBaseUrl, String resourceId){
    this(resourceBaseUrl);
    this.resourceId = resourceId;
  }

  public SingleResourceAccessClient(String resourceBaseUrl, String resourceId, String bearerToken){
    this(resourceBaseUrl, resourceId);
    this.bearerToken = bearerToken;
  }

  protected void setRestTemplate(RestTemplate restTemplate){
    this.restTemplate = restTemplate;
  }

  /**
   * Get a single resource.
   *
   * @return The resource or null if not resource was found.
   */
  public DataResource getResource(){
    LOGGER.trace("Calling getResource().");
    String destinationUri = resourceBaseUrl + resourceId;
    UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(destinationUri);
    LOGGER.trace("Obtaining resource from resource URI {}.", uriBuilder.toUriString());
    headers = new HttpHeaders();
    headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
    if(bearerToken != null){
      headers.set("Authorization", "Bearer " + bearerToken);
    }
    ResponseEntity<DataResource> response = restTemplate.exchange(uriBuilder.toUriString(), HttpMethod.GET, new HttpEntity<>(headers), DataResource.class);
    LOGGER.trace("Request returned with status {}. Returning response body.", response.getStatusCodeValue());
    return response.getBody();
  }

  /**
   * Get a single resource as string.
   *
   * @return The resource or null if not resource was found.
   */
  public String getResourceAsString(){
    LOGGER.trace("Calling getResourceAsString().");
    String destinationUri = resourceBaseUrl + resourceId;
    UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(destinationUri);
    LOGGER.trace("Obtaining resource from resource URI {}.", uriBuilder.toUriString());
    headers = new HttpHeaders();
    headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
    if(bearerToken != null){
      headers.set("Authorization", "Bearer " + bearerToken);
    }
    ResponseEntity<String> response = restTemplate.exchange(uriBuilder.toUriString(), HttpMethod.GET, new HttpEntity<>(headers), String.class);
    LOGGER.trace("Request returned with status {}. Returning response body.", response.getStatusCodeValue());
    return response.getBody();
  }

  /**
   * Create a new resource.
   *
   * @param resource The resource to create.
   *
   * @return The resource.
   */
  public DataResource createResource(DataResource resource){
    LOGGER.trace("Calling createResource(#DataResource).");

    headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
    if(bearerToken != null){
      headers.set("Authorization", "Bearer " + bearerToken);
    }
    String destinationUri = resourceBaseUrl;
    UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(destinationUri);

    LOGGER.trace("Sending POST request for resource.");
    ResponseEntity<DataResource> response = restTemplate.exchange(uriBuilder.toUriString(), HttpMethod.POST, new HttpEntity<>(resource, headers), DataResource.class);
    LOGGER.trace("Request returned with status {}. Returning response body.", response.getStatusCodeValue());
    return response.getBody();
  }

  /**
   * Update a new resource.
   *
   * @param resource The resource to update.
   *
   * @return The resource.
   */
  public DataResource updateResource(DataResource resource){
    LOGGER.trace("Calling updateResource(#DataResource).");

    headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
    if(bearerToken != null){
      headers.set("Authorization", "Bearer " + bearerToken);
    }
    String destinationUri = resourceBaseUrl + resourceId;
    UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(destinationUri);
    LOGGER.trace("Obtaining resource from resource URI {}.", uriBuilder.toUriString());
    ResponseEntity<DataResource> response = restTemplate.exchange(uriBuilder.toUriString(), HttpMethod.GET, new HttpEntity<>(headers), DataResource.class);
    LOGGER.trace("Reading ETag from response header.");
    String etag = response.getHeaders().getFirst("ETag");
    LOGGER.trace("Obtained ETag value {}.", etag);

    LOGGER.trace("Sending PUT request for resource with ETag {}.", etag);
    headers.setIfMatch(etag);
    response = restTemplate.exchange(uriBuilder.toUriString(), HttpMethod.PUT, new HttpEntity<>(resource, headers), DataResource.class);
    LOGGER.trace("Request returned with status {}. Returning response body.", response.getStatusCodeValue());
    return response.getBody();
  }

  /**
   * Get all content information associated with the resource.
   *
   * @param relativePath The relative path to list. If <code>relativePath</code>
   * is <code>null</code> or "/", the root path is listed.
   *
   * @return Al ist of content information accessible below
   * <code>relativePath</code>.
   */
  public ContentInformation[] getContentInformation(String relativePath){
    LOGGER.trace("Calling getContentInformation({}).", relativePath);

    if(relativePath == null || relativePath.equals("/")){
      LOGGER.debug("Relative path is either null or /. Setting new relative path to empty string.");
      relativePath = "";
    }

    LOGGER.trace("Setting accept header to value {}.", ContentInformation.CONTENT_INFORMATION_MEDIA_TYPE);
    headers = new HttpHeaders();
    headers.setAccept(Arrays.asList(ContentInformation.CONTENT_INFORMATION_MEDIA_TYPE));
    if(bearerToken != null){
      headers.set("Authorization", "Bearer " + bearerToken);
    }
    String destinationUri = resourceBaseUrl + resourceId + "/data/" + relativePath;
    UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(destinationUri);
    LOGGER.trace("Obtaining content information from URI {}.", uriBuilder.toUriString());
    if(relativePath.length() == 0 || relativePath.endsWith("/")){
      LOGGER.trace("Expecting content information collection for relative path '{}'.", relativePath);
      ResponseEntity<ContentInformation[]> response = restTemplate.exchange(uriBuilder.toUriString(), HttpMethod.GET, new HttpEntity<>(headers), ContentInformation[].class);
      LOGGER.trace("Request returned with status {}. Returning response body.", response.getStatusCodeValue());
      return response.getBody();
    } else{
      LOGGER.trace("Expecting single content information element for relative path '{}'.", relativePath);
      ResponseEntity<ContentInformation> response = restTemplate.exchange(uriBuilder.toUriString(), HttpMethod.GET, new HttpEntity<>(headers), ContentInformation.class);
      LOGGER.trace("Request returned with status {}. Returning array of response body.", response.getStatusCodeValue());
      return new ContentInformation[]{response.getBody()};
    }
  }

  /**
   * Delete a resource. This call, if supported and authorized, should always
   * return without result.
   */
  public void delete(){
    LOGGER.trace("Calling delete().");

    headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
    if(bearerToken != null){
      headers.set("Authorization", "Bearer " + bearerToken);
    }
    String destinationUri = resourceBaseUrl + resourceId;
    UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(destinationUri);
    LOGGER.trace("Obtaining resource from resource URI {}.", uriBuilder.toUriString());
    ResponseEntity<DataResource> response = restTemplate.exchange(uriBuilder.toUriString(), HttpMethod.GET, new HttpEntity<>(headers), DataResource.class);
    LOGGER.trace("Reading ETag from response header.");
    String etag = response.getHeaders().getFirst("ETag");
    LOGGER.trace("Obtained ETag value {}.", etag);

    LOGGER.trace("Sending DELETE request for resource with ETag {}.", etag);
    headers.setIfMatch(etag);
    response = restTemplate.exchange(uriBuilder.toUriString(), HttpMethod.DELETE, new HttpEntity<>(headers), DataResource.class);
    LOGGER.trace("Request returned with status {}. No response body expected.", response.getStatusCodeValue());
  }

  /**
   * Create an upload client for uploading the provided file to the addressed
   * resource. Content information metadata can be provided at the UploadClient.
   *
   * @param file The file to upload.
   *
   * @return The upload client.
   */
  public UploadClient withFile(File file){
    LOGGER.trace("Calling withFile({}) and switching to UploadClient.", file);
    UploadClient client = new UploadClient(resourceBaseUrl, resourceId, bearerToken);
    return client.withFile(file);
  }

  /**
   * Create an upload client for uploading the provided stream to the addressed
   * resource. Content information metadata can be provided at the UploadClient.
   *
   * @param stream The stream to upload.
   *
   * @return The upload client.
   */
  public UploadClient withStream(InputStream stream){
    LOGGER.trace("Calling withStream(#stream) and switching to UploadClient.");
    UploadClient client = new UploadClient(resourceBaseUrl, resourceId, bearerToken);
    return client.withStream(stream);
  }

  /**
   * Create an upload client for uploading the provided metadata to the
   * addressed resource. File/stream data can be provided at the UploadClient.
   *
   * @param metadata The content information metadata.
   *
   * @return The upload client.
   */
  public UploadClient withMetadata(ContentInformation metadata){
    LOGGER.trace("Calling withMetadata({}) and switching to UploadClient.", metadata);
    UploadClient client = new UploadClient(resourceBaseUrl, resourceId, bearerToken);
    return client.withMetadata(metadata);
  }

  /**
   * Create an upload client forcing the upload. Content information metadata as
   * well as file/stream data can be provided at the UploadClient.
   *
   * @param overwrite TRUE to overwrite existing files, FALSE otherwise..
   *
   * @return The upload client.
   */
  public UploadClient overwrite(boolean overwrite){
    LOGGER.trace("Calling overwrite({}) and switching to UploadClient.", overwrite);
    UploadClient client = new UploadClient(resourceBaseUrl, resourceId, bearerToken);
    return client.overwrite(overwrite);
  }

}
