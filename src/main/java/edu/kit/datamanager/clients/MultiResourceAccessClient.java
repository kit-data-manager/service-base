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
import edu.kit.datamanager.util.ControllerUtils;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import lombok.Data;
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
  private final List<SortField> sortFields = new ArrayList<>();

  MultiResourceAccessClient(String resourceBaseUrl){
    this.resourceBaseUrl = resourceBaseUrl;
  }

  protected void setRestTemplate(RestTemplate restTemplate){
    this.restTemplate = restTemplate;
  }

  /**
   * Set the returned page.
   *
   * @param page The returned page.
   *
   * @return this
   */
  public MultiResourceAccessClient page(int page){
    LOGGER.trace("Calling withPage({}).", page);
    this.page = page >= 0 ? page : 0;
    return this;
  }

  /**
   * Set the maximum number of elements per page.
   *
   * @param elementsPerPage The max number of elements per page.
   *
   * @return this
   */
  public MultiResourceAccessClient elementsPerPage(int elementsPerPage){
    LOGGER.trace("Calling withElementsPerPage({}).", elementsPerPage);
    this.elementsPerPage = (elementsPerPage <= 100) ? elementsPerPage : 100;
    return this;
  }

  /**
   * Set a single sort field.
   *
   * @param field A list of sort field.
   *
   * @return this
   */
  public MultiResourceAccessClient sortBy(SortField field){
    return sortBy(Arrays.asList(field));
  }

  /**
   * Set a list of sort fields.
   *
   * @param fields A list of sort fields.
   *
   * @return this
   */
  public MultiResourceAccessClient sortBy(List<SortField> fields){
    if(fields != null && !fields.isEmpty()){
      this.sortFields.addAll(fields);
    }
    return this;
  }

  /**
   * Get a list of resources depending on the first result index, the page size
   * and the sorting. The result is returned as ResultPage containing a list of
   * results and page information, e.g. the first index, the number of elements
   * on the page and the number of total elements.
   *
   * @return The ResultPage which might be empty.
   */
  public ResultPage getResources(){
    LOGGER.trace("Calling getResource().");
    headers = new HttpHeaders();
    LOGGER.trace("Setting accept header to value {}.", MediaType.APPLICATION_JSON);
    headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
    UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(resourceBaseUrl).queryParam("page", page).queryParam("size", elementsPerPage);

    for(SortField field : sortFields){
      uriBuilder = uriBuilder.queryParam("sort", field.getFieldName()).queryParam(field.getFieldName() + ".dir", field.getDirection().toString().toLowerCase());
    }

    LOGGER.trace("Requesting resources from URI {}.", uriBuilder.toUriString());
    ResponseEntity<DataResource[]> response = restTemplate.exchange(uriBuilder.toUriString(), HttpMethod.GET, new HttpEntity<>(headers), DataResource[].class);

    LOGGER.trace("Request returned with status {}. Returning response body.", response.getStatusCodeValue());
    String contentRangeHeader = response.getHeaders().getFirst("Content-Range");
    ControllerUtils.ContentRange range = new ControllerUtils.ContentRange();
    if(contentRangeHeader != null){
      range = ControllerUtils.parseContentRangeHeader(contentRangeHeader);
    }

    return new ResultPage(response.getBody(), range);
  }

  /**
   * Find resources by example. The result is returned as ResultPage containing
   * a list of results and page information, e.g. the first index, the number of
   * elements on the page and the number of total elements.
   *
   * @param example The search example document.
   *
   * @return The ResultPage which might be empty.
   */
  public ResultPage findResources(DataResource example){
    LOGGER.trace("Calling findResources({}).", example);
    headers = new HttpHeaders();
    LOGGER.trace("Setting accept header to value {}.", MediaType.APPLICATION_JSON);
    headers.setContentType(MediaType.APPLICATION_JSON);
    headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
    UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(resourceBaseUrl + "search").queryParam("page", page).queryParam("size", elementsPerPage);

    for(SortField field : sortFields){
      uriBuilder = uriBuilder.queryParam("sort", field.getFieldName()).queryParam(field.getFieldName() + ".dir", field.getDirection().toString().toLowerCase());
    }

    System.out.println("URI " + uriBuilder.toUriString());
    LOGGER.trace("Requesting resources from URI {}.", uriBuilder.toUriString());

    ResponseEntity<DataResource[]> response = restTemplate.postForEntity(uriBuilder.toUriString(), new HttpEntity<>(example, headers), DataResource[].class);

    LOGGER.trace("Request returned with status {}. Returning response body.", response.getStatusCodeValue());
    String contentRangeHeader = response.getHeaders().getFirst("Content-Range");
    ControllerUtils.ContentRange range = new ControllerUtils.ContentRange();
    if(contentRangeHeader != null){
      range = ControllerUtils.parseContentRangeHeader(contentRangeHeader);
    }

    return new ResultPage(response.getBody(), range);
  }

  @Data
  public static class ResultPage{

    public static ResultPage empty(){
      return new ResultPage(new DataResource[0], ControllerUtils.ContentRange.empty());
    }

    public ResultPage(DataResource[] resources, ControllerUtils.ContentRange range){
      this.resources = resources;
      this.contentRange = range;
    }

    ControllerUtils.ContentRange contentRange;
    DataResource[] resources;
  }

  @Data
  public static class SortField{

    public enum DIR{
      ASC,
      DESC;
    }

    String fieldName;
    DIR direction;

    public SortField(String fieldName, DIR direction){
      this.fieldName = fieldName;
      this.direction = direction;
    }

  }
}
