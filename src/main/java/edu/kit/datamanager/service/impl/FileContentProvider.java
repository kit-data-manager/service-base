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
package edu.kit.datamanager.service.impl;

import edu.kit.datamanager.service.IContentProvider;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 *
 * @author jejkal
 */
@Component
public class FileContentProvider implements IContentProvider{

  @Override
  public ResponseEntity provide(URI contentUri, MediaType mediaType, String filename){
    if(!Files.exists(Paths.get(contentUri))){
      throw new edu.kit.datamanager.exceptions.ResourceNotFoundException("The provided resource was not found on the server.");
    }

    return ResponseEntity.
            ok().
            contentType(mediaType).
            header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"").
            header(HttpHeaders.CONTENT_LENGTH, String.valueOf(new File(contentUri).length())).
            body(new FileSystemResource(new File(contentUri)));
  }

  @Override
  public boolean canProvide(String schema){
    return "file".equals(schema);
  }

}
