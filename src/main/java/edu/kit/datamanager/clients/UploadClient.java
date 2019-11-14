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
import edu.kit.datamanager.entities.repo.ContentInformation;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

/**
 *
 * @author jejkal
 */
public class UploadClient{

  private final static Logger LOGGER = LoggerFactory.getLogger(UploadClient.class);

  private RestTemplate restTemplate = new RestTemplate();
  private HttpHeaders headers;
  private final String resourceBaseUrl;
  private final String resourceId;

  private File file;
  private InputStream stream;
  private ContentInformation metadata;
  private boolean overwrite;
  private String bearerToken;

  /**
   * Default constructor.
   *
   * @param resourceBaseUrl Base Url for accessing resources.
   * @param resourceId Identifier of the resource to access.
   */
  UploadClient(String resourceBaseUrl, String resourceId){
    restTemplate = new RestTemplate();
    this.headers = new HttpHeaders();
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
  UploadClient(String resourceBaseUrl, String resourceId, String bearerToken){
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
  public UploadClient withFile(File file){
    LOGGER.trace("Setting file to upload to {}.", file);
    this.file = file;
    this.stream = null;
    return this;
  }

  /**
   * Do an upload with the provided stream.
   *
   * @param stream The stream to upload.
   *
   * @return this
   */
  public UploadClient withStream(InputStream stream){
    LOGGER.trace("Setting stream to upload.");
    this.file = null;
    this.stream = stream;
    return this;
  }

  /**
   * Do an upload using the provided content metdata.
   *
   * @param metadata The content metadata.
   *
   * @return this
   */
  public UploadClient withMetadata(ContentInformation metadata){
    LOGGER.trace("Setting metadata to upload to {}.", metadata);
    this.metadata = metadata;
    return this;
  }

  /**
   * Do an upload overwriting existing data. If true, existing data is
   * overwritten. If false, HTTP Conflict will be returned.
   *
   * @param overwrite TRUE for forced upload, FALSE otherwise.
   *
   * @return this
   */
  public UploadClient overwrite(boolean overwrite){
    this.overwrite = overwrite;
    return this;
  }

  public int upload(String relativePath) throws JsonProcessingException, IOException{
    LOGGER.trace("Calling upload({}).", relativePath);
    headers = new HttpHeaders();
    LOGGER.trace("Adding content type header with value {}.", MediaType.MULTIPART_FORM_DATA);
    headers.setContentType(MediaType.MULTIPART_FORM_DATA);
    if(bearerToken != null){
      headers.set("Authorization", "Bearer " + bearerToken);
    }
    MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
    LOGGER.trace("Adding file argument for file {}.", file);
    if(file == null && stream == null && metadata == null){
      throw new IllegalArgumentException("Unable to perform upload with none of file, stream and metadata.");
    } else if(file == null && stream == null && metadata.getContentUri() == null){
      throw new IllegalArgumentException("Unable to perform upload without file, stream and metadata content uri.");
    } else if(file != null){
      body.add("file", new FileSystemResource(file));
    } else if(stream != null){
      body.add("file", new ByteArrayResource(stream.readAllBytes()){
        //overwriting filename required by spring (see https://medium.com/@voziv/posting-a-byte-array-instead-of-a-file-using-spring-s-resttemplate-56268b45140b)
        @Override
        public String getFilename(){
          return "stream#" + UUID.randomUUID().toString();
        }
      });
    }

    if(metadata != null){
      String contentMetadataString = new ObjectMapper().writeValueAsString(metadata);
      LOGGER.trace("Adding metadata argument from JSON document {}.", contentMetadataString);
      body.add("metadata", new ByteArrayResource(contentMetadataString.getBytes()){
        //overwriting filename required by spring (see https://medium.com/@voziv/posting-a-byte-array-instead-of-a-file-using-spring-s-resttemplate-56268b45140b)
        @Override
        public String getFilename(){
          return "metadata.json";
        }

      });
    }
    String destinationUri = resourceBaseUrl + resourceId + "/data/" + relativePath;

    UriComponentsBuilder uriBuilder = UriComponentsBuilder.
            fromHttpUrl(destinationUri).
            queryParam("force", overwrite);
    LOGGER.trace("Uploading content to destination URI {}.", uriBuilder.toUriString());
    ResponseEntity<String> response = restTemplate.postForEntity(uriBuilder.toUriString(), new HttpEntity<>(body, headers), String.class);
    LOGGER.trace("Upload returned with status {}.", response.getStatusCodeValue());
    return response.getStatusCodeValue();
  }
}
