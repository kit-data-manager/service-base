/*
 * Copyright 2018 Karlsruhe Institute of Technology.
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
package edu.kit.datamanager.service;

import java.net.URI;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

/**
 *
 * @author jejkal
 */
public interface IContentProvider{

  /**
   * Provide the content available at 'contentUri' with media type 'mediaType'
   * and name 'filename'. The result is a ResponseEntity holding either the bit
   * stream to the content or another appropriate response, e.g. a redirect.
   *
   * @param contentUri The URI at which the content is available.
   * @param mediaType The media type of the content used for content
   * negotiation.
   * @param filename The filename the content is available locally. This
   * filename may differ from the last path element of contentUri.
   *
   * @return A ResponseEntity containing the bit stream or another valid status.
   */
  ResponseEntity provide(URI contentUri, MediaType mediaType, String filename);

  /**
   * Check if this provider implementation is capable of providing content
   * accessible via protocol 'schema', e.g. http(s) or file.
   *
   * @param schema The schema of 'contentUri'.
   *
   * @return TRUE if the content can be provided, FALSE otherwise.
   */
  boolean canProvide(String schema);

}
