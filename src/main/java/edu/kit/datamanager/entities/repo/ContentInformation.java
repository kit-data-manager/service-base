/*
 * Copyright 2017 Karlsruhe Institute of Technology.
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
package edu.kit.datamanager.entities.repo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.http.InvalidMediaTypeException;
import org.springframework.http.MediaType;

/**
 *
 * @author jejkal
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@EqualsAndHashCode(callSuper = false)
public class ContentInformation implements Serializable{

  public static final MediaType CONTENT_INFORMATION_MEDIA_TYPE = MediaType.parseMediaType("application/vnd.datamanager.content-information+json");

  private String relativePath;
  private String contentUri;
  private String uploader;
  private String mediaType;
  private String hash;
  private long size;
  private Map<String, String> metadata = new HashMap<>();
  private Set<String> tags = new HashSet<>();

  public String getFilename(){

    if(relativePath == null){
      return null;
    }
    return relativePath.substring(relativePath.lastIndexOf("/") + 1);
  }

  @JsonIgnore
  public void setMediaTypeAsObject(MediaType mediaType){
    if(mediaType == null){
      throw new IllegalArgumentException("Argument must not be null.");
    }
    this.mediaType = mediaType.toString();
  }

  @JsonIgnore
  public MediaType getMediaTypeAsObject(){
    try{
      return MediaType.parseMediaType(mediaType);
    } catch(InvalidMediaTypeException ex){
      return MediaType.APPLICATION_OCTET_STREAM;
    }
  }
}
