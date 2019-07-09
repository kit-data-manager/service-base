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
  private final String resourceId;

  public SingleResourceAccessClient(String resourceBaseUrl, String resourceId){
    this.resourceBaseUrl = resourceBaseUrl;
    this.resourceId = resourceId;
  }

  protected void setRestTemplate(RestTemplate restTemplate){
    this.restTemplate = restTemplate;
  }

  public DataResource getResource(){
    LOGGER.trace("Calling getResource().");
    String destinationUri = resourceBaseUrl + resourceId;
    UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(destinationUri);
    LOGGER.trace("Obtaining resource from resource URI {}.", uriBuilder.toUriString());
    ResponseEntity<DataResource> response = restTemplate.exchange(uriBuilder.toUriString(), HttpMethod.GET, new HttpEntity<>(headers), DataResource.class);
    LOGGER.trace("Request returned with status {}. Returning response body.", response.getStatusCodeValue());
    return response.getBody();
  }

  public String getResourceAsString(){
    LOGGER.trace("Calling getResourceAsString().");
    String destinationUri = resourceBaseUrl + resourceId;
    UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(destinationUri);
    LOGGER.trace("Obtaining resource from resource URI {}.", uriBuilder.toUriString());
    ResponseEntity<String> response = restTemplate.exchange(uriBuilder.toUriString(), HttpMethod.GET, new HttpEntity<>(headers), String.class);
    LOGGER.trace("Request returned with status {}. Returning response body.", response.getStatusCodeValue());
    return response.getBody();
  }

  public ContentInformation[] getContentInformation(String relativePath){
    LOGGER.trace("Calling getContentInformation({}).", relativePath);

    if(relativePath == null || relativePath.equals("/")){
      LOGGER.debug("Relative path is either null or /. Setting new relative path to empty string.");
      relativePath = "";
    }

    LOGGER.trace("Setting accept header to value {}.", ContentInformation.CONTENT_INFORMATION_MEDIA_TYPE);
    headers = new HttpHeaders();
    headers.setAccept(Arrays.asList(ContentInformation.CONTENT_INFORMATION_MEDIA_TYPE));

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

  public UploadClient withFile(File file){
    LOGGER.trace("Calling withFile({}) and switching to UploadClient.", file);
    UploadClient client = new UploadClient(resourceBaseUrl, resourceId);
    return client.withFile(file);
  }

  public UploadClient withMetadata(ContentInformation metadata){
    LOGGER.trace("Calling withMetadata({}) and switching to UploadClient.", metadata);
    UploadClient client = new UploadClient(resourceBaseUrl, resourceId);
    return client.withMetadata(metadata);
  }

  public UploadClient overwrite(boolean overwrite){
    LOGGER.trace("Calling overwrite({}) and switching to UploadClient.", overwrite);
    UploadClient client = new UploadClient(resourceBaseUrl, resourceId);
    return client.overwrite(overwrite);
  }
}
