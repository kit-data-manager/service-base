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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Streams;
import edu.kit.datamanager.entities.repo.ContentInformation;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RequestCallback;
import org.springframework.web.client.ResponseExtractor;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

/**
 *
 * @author jejkal
 */
public class DownloadClient{

  private final static Logger LOGGER = LoggerFactory.getLogger(DownloadClient.class);

  private RestTemplate restTemplate = new RestTemplate();
  private final String resourceBaseUrl;
  private final String resourceId;

  private File file;
  private OutputStream stream;
  private ContentInformation metadata;
  private boolean overwrite;
  private String bearerToken;

  /**
   * Default constructor.
   *
   * @param resourceBaseUrl Base Url for accessing resources.
   * @param resourceId Identifier of the resource to access.
   */
  DownloadClient(String resourceBaseUrl, String resourceId){
    restTemplate = new RestTemplate();
    this.resourceBaseUrl = resourceBaseUrl;
    this.resourceId = resourceId;
  }

  /**
   * Default constructor.
   *
   * @param resourceBaseUrl Base Url for accessing resources.
   * @param resourceId Identifier of the resource to access.
   * @param bearerToken Bearer token for authentication.
   */
  DownloadClient(String resourceBaseUrl, String resourceId, String bearerToken){
    this(resourceBaseUrl, resourceId);
    this.bearerToken = bearerToken;
  }

  /**
   * Set the rest template externally.
   */
  protected void setRestTemplate(RestTemplate restTemplate){
    this.restTemplate = restTemplate;
  }

  /**
   * Do an upload with the provided file.
   *
   * @param file The file to upload.
   *
   * @return this
   */
  public DownloadClient toFile(File file){
    LOGGER.trace("Setting file to download to {}.", file);
    this.file = file;
    this.stream = null;
    return this;
  }

  /**
   * Do a download to the provided stream.
   *
   * @param stream The stream to download to.
   *
   * @return this
   */
  public DownloadClient toStream(OutputStream stream){
    LOGGER.trace("Setting stream to download to.");
    this.file = null;
    this.stream = stream;
    return this;
  }

  public int download(String relativePath) throws JsonProcessingException, IOException{
    LOGGER.trace("Calling download({}).", relativePath);

    String destinationUri = resourceBaseUrl + resourceId + "/data/" + relativePath;

    UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(destinationUri);
    LOGGER.trace("Downloading content from source URI {}.", uriBuilder.toUriString());

    RequestCallback requestCallback = request -> {
      request.getHeaders().setAccept(Arrays.asList(MediaType.APPLICATION_OCTET_STREAM, MediaType.ALL));
      if(bearerToken != null){
        request.getHeaders().set("Authorization", "Bearer " + bearerToken);
      }
    };

    ResponseExtractor<Integer> responseExtractor = response -> {
      if(file != null){
        Path path = Paths.get(file.toURI());
        Files.copy(response.getBody(), path);
      } else{
        response.getBody().transferTo(stream);
      }
      return response.getRawStatusCode();
    };
    Integer status = restTemplate.execute(uriBuilder.toUriString(), HttpMethod.GET, requestCallback, responseExtractor);

    LOGGER.trace("Download returned with status {}.", status);
    return status;
  }
 
}
