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

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.kit.datamanager.entities.repo.DataResource;
import edu.kit.datamanager.util.ControllerUtils;
import edu.kit.datamanager.util.ControllerUtils.ContentRange;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.UUID;
import lombok.Data;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RequestCallback;
import org.springframework.web.client.ResponseExtractor;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

/**
 *
 * @author jejkal
 */
public class SimpleServiceClient{

  private final static Logger LOGGER = LoggerFactory.getLogger(SimpleServiceClient.class);
  private RestTemplate restTemplate = new RestTemplate();

  private final String resourceBaseUrl;
  private String resourcePath = null;
  private String bearerToken = null;
  private HttpHeaders headers;
  private Map<String, String> requestedResponseHeaders = null;

  MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
  MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();

  SimpleServiceClient(String resourceBaseUrl){
    this.resourceBaseUrl = resourceBaseUrl;
    headers = new HttpHeaders();
  }

  public void setRestTemplate(RestTemplate restTemplate){
    this.restTemplate = restTemplate;

  }

  public static SimpleServiceClient create(String baseUrl){
    SimpleServiceClient client = new SimpleServiceClient(baseUrl);
    return client;
  }

  public SimpleServiceClient withBearerToken(String bearerToken){
    this.bearerToken = bearerToken;
    if(bearerToken != null){
      return withHeader("Authorization", "Bearer " + bearerToken);
    }
    headers.remove("Authorization");
    return this;
  }

  public SimpleServiceClient withHeader(String field, String value){
    this.headers.add(field, value);
    return this;
  }

  public SimpleServiceClient accept(MediaType... mediaType){
    headers.setAccept(Arrays.asList(mediaType));
    return this;
  }

  public SimpleServiceClient collectResponseHeader(Map<String, String> container){
    requestedResponseHeaders = container;
    return this;
  }

  public SimpleServiceClient withContentType(MediaType contentType){
    headers.setContentType(contentType);
    return this;
  }

  public SimpleServiceClient withResourcePath(String resourcePath){
    LOGGER.trace("Creating SingleResourceAccessClient with resourcePath {}.", resourcePath);
    this.resourcePath = resourcePath;
    return this;
  }

  public SimpleServiceClient withFormParam(String name, Object object) throws IOException{
    if(name == null || object == null){
      throw new IllegalArgumentException("Form element key and value must not be null.");
    }
    if(object instanceof File){
      body.add(name, new FileSystemResource((File) object));
    } else if(object instanceof InputStream){
      body.add(name, new ByteArrayResource(IOUtils.toByteArray((InputStream) object)){
        //overwriting filename required by spring (see https://medium.com/@voziv/posting-a-byte-array-instead-of-a-file-using-spring-s-resttemplate-56268b45140b)
        @Override
        public String getFilename(){
          return "stream#" + UUID.randomUUID().toString();
        }
      });
    } else{
      String metadataString = new ObjectMapper().writeValueAsString(object);
      LOGGER.trace("Adding argument from JSON document {}.", metadataString);
      body.add(name, new ByteArrayResource(metadataString.getBytes()){
        //overwriting filename required by spring (see https://medium.com/@voziv/posting-a-byte-array-instead-of-a-file-using-spring-s-resttemplate-56268b45140b)
        @Override
        public String getFilename(){
          return "metadata#" + UUID.randomUUID().toString() + ".json";
        }
      });
    }
    return this;
  }

  public SimpleServiceClient withQueryParam(String name, String value){
    queryParams.add(name, value);
    return this;
  }

  public <C> C getResource(Class<C> responseType){
    LOGGER.trace("Calling getResource().");
    String destinationUri = resourceBaseUrl + ((resourcePath != null) ? resourcePath : "");
    UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(destinationUri).queryParams(queryParams);
    LOGGER.trace("Obtaining resource from resource URI {}.", uriBuilder.toUriString());
    ResponseEntity<C> response = restTemplate.exchange(uriBuilder.toUriString(), HttpMethod.GET, new HttpEntity<>(headers), responseType);
    collectResponseHeaders(response.getHeaders());
    LOGGER.trace("Request returned with status {}. Returning response body.", response.getStatusCodeValue());
    return response.getBody();
  }

  public <C> ResultPage<C> getResources(Class<C[]> responseType){
    LOGGER.trace("Calling getResource().");
    String destinationUri = resourceBaseUrl + ((resourcePath != null) ? resourcePath : "");
    UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(destinationUri).queryParams(queryParams);
    LOGGER.trace("Obtaining resource from resource URI {}.", uriBuilder.toUriString());
    ResponseEntity<C[]> response = restTemplate.exchange(uriBuilder.toUriString(), HttpMethod.GET, new HttpEntity<>(headers), responseType);
    LOGGER.trace("Request returned with status {}. Returning response body.", response.getStatusCodeValue());
    ContentRange contentRange = ControllerUtils.parseContentRangeHeader(response.getHeaders().getFirst("Content-Range"));
    collectResponseHeaders(response.getHeaders());
    return new ResultPage<>(response.getBody(), contentRange);
  }

  public <C> ResultPage<C> findResources(C resource, Class<C[]> responseType){
    LOGGER.trace("Calling getResource().");
    String destinationUri = resourceBaseUrl + "search";
    UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(destinationUri).queryParams(queryParams);
    LOGGER.trace("Obtaining resource from resource URI {}.", uriBuilder.toUriString());
    ResponseEntity<C[]> response = restTemplate.exchange(uriBuilder.toUriString(), HttpMethod.POST, new HttpEntity<>(resource, headers), responseType);
    LOGGER.trace("Request returned with status {}. Returning response body.", response.getStatusCodeValue());
    ContentRange contentRange = ControllerUtils.parseContentRangeHeader(response.getHeaders().getFirst("Content-Range"));
    collectResponseHeaders(response.getHeaders());
    return new ResultPage<>(response.getBody(), contentRange);
  }

  public int getResource(OutputStream outputStream){
    String sourceUri = resourceBaseUrl + ((resourcePath != null) ? resourcePath : "");

    UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(sourceUri).queryParams(queryParams);
    LOGGER.trace("Downloading content from source URI {}.", uriBuilder.toUriString());

    RequestCallback requestCallback = request -> {
      Set<Entry<String, List<String>>> entries = headers.entrySet();
      entries.forEach((entry) -> {
        request.getHeaders().addAll(entry.getKey(), entry.getValue());
      });
    };

    ResponseExtractor<ClientHttpResponse> responseExtractor = response -> {
      IOUtils.copy(response.getBody(), outputStream);

      return response;
    };

    ClientHttpResponse response = restTemplate.execute(uriBuilder.toUriString(), HttpMethod.GET, requestCallback, responseExtractor);
    int status = -1;
    try{
      status = response.getRawStatusCode();
      LOGGER.trace("Download returned with status {}.", status);
      collectResponseHeaders(response.getHeaders());
    } catch(IOException ex){
      LOGGER.error("Failed to extract raw status from response.", ex);
    }
    return status;
  }

  public <C> C postResource(C resource, Class<C> responseType){
    LOGGER.trace("Calling createResource(#DataResource).");

    String destinationUri = resourceBaseUrl + ((resourcePath != null) ? resourcePath : "");
    UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(destinationUri).queryParams(queryParams);

    LOGGER.trace("Sending POST request for resource.");
    ResponseEntity<C> response = restTemplate.exchange(uriBuilder.toUriString(), HttpMethod.POST, new HttpEntity<>(resource, headers), responseType);
    LOGGER.trace("Request returned with status {}. Returning response body.", response.getStatusCodeValue());
    collectResponseHeaders(response.getHeaders());
    return response.getBody();
  }

  public HttpStatus postForm(){
    return postForm(MediaType.MULTIPART_FORM_DATA);
  }

  public HttpStatus postForm(MediaType contentType){
    LOGGER.trace("Adding content type header with value {}.", contentType);
    headers.setContentType(contentType);

    String destinationUri = resourceBaseUrl + ((resourcePath != null) ? resourcePath : "");

    UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(destinationUri).queryParams(queryParams);

    LOGGER.trace("Uploading content to destination URI {}.", uriBuilder.toUriString());
    ResponseEntity<String> response = restTemplate.postForEntity(uriBuilder.toUriString(), new HttpEntity<>(body, headers), String.class);
    LOGGER.trace("Upload returned with status {}.", response.getStatusCodeValue());
    collectResponseHeaders(response.getHeaders());
    return response.getStatusCode();

  }

  public <C> C putResource(C resource, Class<C> responseType){
    LOGGER.trace("Calling updateResource(#DataResource).");

    String destinationUri = resourceBaseUrl + ((resourcePath != null) ? resourcePath : "");
    UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(destinationUri).queryParams(queryParams);
    LOGGER.trace("Obtaining resource from resource URI {}.", uriBuilder.toUriString());
    ResponseEntity<C> response = restTemplate.exchange(uriBuilder.toUriString(), HttpMethod.GET, new HttpEntity<>(headers), responseType);
    LOGGER.trace("Reading ETag from response header.");
    String etag = response.getHeaders().getFirst("ETag");
    LOGGER.trace("Sending PUT request for resource with ETag {}.", etag);
    headers.setIfMatch(etag);
    response = restTemplate.exchange(uriBuilder.toUriString(), HttpMethod.PUT, new HttpEntity<>(resource, headers), responseType);
    collectResponseHeaders(response.getHeaders());
    LOGGER.trace("Request returned with status {}. Returning response body.", response.getStatusCodeValue());
    return response.getBody();
  }

  /**
   * Delete a resource. This call, if supported and authorized, should always
   * return without result.
   */
  public void deleteResource(){
    LOGGER.trace("Calling delete().");
    String destinationUri = resourceBaseUrl + ((resourcePath != null) ? resourcePath : "");
    UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(destinationUri).queryParams(queryParams);
    LOGGER.trace("Obtaining resource from resource URI {}.", uriBuilder.toUriString());
    ResponseEntity<DataResource> response = restTemplate.exchange(uriBuilder.toUriString(), HttpMethod.GET, new HttpEntity<>(headers), DataResource.class);
    LOGGER.trace("Reading ETag from response header.");
    String etag = response.getHeaders().getFirst("ETag");
    LOGGER.trace("Obtained ETag value {}.", etag);

    LOGGER.trace("Sending DELETE request for resource with ETag {}.", etag);
    headers.setIfMatch(etag);
    response = restTemplate.exchange(uriBuilder.toUriString(), HttpMethod.DELETE, new HttpEntity<>(headers), DataResource.class);
    collectResponseHeaders(response.getHeaders());
    LOGGER.trace("Request returned with status {}. No response body expected.", response.getStatusCodeValue());
  }

  private void collectResponseHeaders(HttpHeaders responseHeaders){
    if(requestedResponseHeaders != null){
      Set<Entry<String, String>> entries = requestedResponseHeaders.entrySet();

      entries.forEach((entry) -> {
        requestedResponseHeaders.put(entry.getKey(), responseHeaders.getFirst(entry.getKey()));
      });
    }
  }

  public static void main(String[] args){
    ResultPage<DataResource> result = SimpleServiceClient.create("http://localhost:8090/api/v1/dataresources1/").getResources(DataResource[].class);
    System.out.println(result.getContentRange());
    for(DataResource r : result.getResources()){
      System.out.println(r);
    }
  }

  @Data
  public static class ResultPage<C>{

    public ResultPage(C[] resources, ControllerUtils.ContentRange range){
      this.resources = resources;
      this.contentRange = range;
    }

    ControllerUtils.ContentRange contentRange;
    C[] resources;
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

    public String toQueryParam(){
      return fieldName + ((direction != null) ? "," + direction.toString().toLowerCase() : "");
    }

  }
}
