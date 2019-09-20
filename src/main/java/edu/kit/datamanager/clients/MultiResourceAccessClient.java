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
public class MultiResourceAccessClient{

  private final static Logger LOGGER = LoggerFactory.getLogger(MultiResourceAccessClient.class);

  private RestTemplate restTemplate = new RestTemplate();
  private HttpHeaders headers;
  private final String resourceBaseUrl;
  private int elementsPerPage = 20;
  private int page = 0;

  MultiResourceAccessClient(String resourceBaseUrl){
    this.resourceBaseUrl = resourceBaseUrl;
  }

  protected void setRestTemplate(RestTemplate restTemplate){
    this.restTemplate = restTemplate;
  }

  public MultiResourceAccessClient page(int page){
    LOGGER.trace("Calling withPage({}).", page);
    this.page = page >= 0 ? page : 0;
    return this;
  }

  public MultiResourceAccessClient elementsPerPage(int elementsPerPage){
    LOGGER.trace("Calling withElementsPerPage({}).", elementsPerPage);
    this.elementsPerPage = (elementsPerPage <= 100) ? elementsPerPage : 100;
    return this;
  }

  public DataResource[] getResources(){
    LOGGER.trace("Calling getResource().");
    headers = new HttpHeaders();
    LOGGER.trace("Setting accept header to value {}.", MediaType.APPLICATION_JSON);
    headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
    UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(resourceBaseUrl).queryParam("page", page).queryParam("size", elementsPerPage);
    LOGGER.trace("Requesting resource from URI {}.", uriBuilder.toUriString());
    ResponseEntity<DataResource[]> response = restTemplate.exchange(uriBuilder.toUriString(), HttpMethod.GET, new HttpEntity<>(headers), DataResource[].class);
    LOGGER.trace("Request returned with status {}. Returning response body.", response.getStatusCodeValue());
    return response.getBody();
  }

}
