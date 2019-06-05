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
package edu.kit.datamanager.entities;

import java.net.URI;
import lombok.Data;

/**
 *
 * @author jejkal
 */
@Data
public class CollectionElement{

  private String relativePath;
  private long contentLength;
  private URI contentUri;
  private String checksum;
  private String repositoryLocation;

  private CollectionElement(String relativePath, URI contentUri, String checksum, String repositoryLocation, Long contentLength){
    this.relativePath = relativePath;
    this.contentUri = contentUri;
    this.checksum = checksum;
    this.repositoryLocation = repositoryLocation;
    this.contentLength = contentLength;
  }

  public static CollectionElement createCollectionElement(String relativePath, URI contentUri, String checksum, String repositoryLocation, Long contentLength){
    return new CollectionElement(relativePath, contentUri, checksum, repositoryLocation, contentLength);
  }

  public static CollectionElement createCollectionElement(String relativePath, URI contentUri, String repositoryLocation, Long contentLength){
    return new CollectionElement(relativePath, contentUri, null, repositoryLocation, contentLength);
  }

  public static CollectionElement createCollectionElement(String relativePath, URI contentUri){
    return new CollectionElement(relativePath, contentUri, null, null, 0l);
  }
}