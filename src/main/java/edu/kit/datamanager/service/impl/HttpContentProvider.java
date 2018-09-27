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
import java.io.IOException;
import java.net.URI;
import org.apache.http.Header;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
public class HttpContentProvider implements IContentProvider{

  private final static Logger LOGGER = LoggerFactory.getLogger(HttpContentProvider.class);

  @Override
  public ResponseEntity provide(URI contentUri, MediaType mediaType, String filename){
    final String uriString = contentUri.toString();

    //try to redirect transfer if scheme is not file
    CloseableHttpClient client = HttpClients.createDefault();
    HttpGet httpGet = new HttpGet(contentUri.toString());
    HttpHeaders headers = new HttpHeaders();
    HttpStatus returnedStatus;
    try{
      CloseableHttpResponse httpResponse = client.execute(httpGet);
      HttpStatus responseStatus = HttpStatus.resolve(httpResponse.getStatusLine().getStatusCode());

      if(responseStatus == null){
        LOGGER.warn("Received unknown response status {} while accessing resource URI " + uriString + ". Returning resource URI in Content-Location header and HTTP SERVICE_UNAVAILABLE.", httpResponse.getStatusLine().getStatusCode(), uriString);
        headers.add("Content-Location", uriString);
        returnedStatus = HttpStatus.SERVICE_UNAVAILABLE;
      } else{
        returnedStatus = responseStatus;
        switch(responseStatus){
          case OK: {
            //add location header in order to trigger redirect
            returnedStatus = HttpStatus.SEE_OTHER;
            headers.add("Location", uriString);
            break;
          }
          case SEE_OTHER:
          case FOUND:
          case MOVED_PERMANENTLY:
          case TEMPORARY_REDIRECT:
          case PERMANENT_REDIRECT: {
            Header location = httpResponse.getFirstHeader("Location");
            if(location != null){
              headers.add("Location", location.getValue());
            } else{
              LOGGER.error("Received status {} but no location header.", responseStatus);
              returnedStatus = HttpStatus.INTERNAL_SERVER_ERROR;
            }
            break;
          }
          default: {
            LOGGER.warn("Received status " + responseStatus + ". Returning Content-Location header with value {} and HTTP NO_CONTENT.", uriString);
            returnedStatus = HttpStatus.NO_CONTENT;
            headers.add("Content-Location", uriString);
          }
        }
      }
    } catch(IOException ex){
      LOGGER.error("Failed to resolve content URI " + uriString + ". Sending HTTP SERVICE_UNAVAILABLE.", ex);
      returnedStatus = HttpStatus.SERVICE_UNAVAILABLE;
      headers.add("Content-Location", uriString);
    }
    return new ResponseEntity<>(null, headers, returnedStatus);
  }

  @Override
  public boolean canProvide(String schema){
    return "http".equals(schema) || "https".equals(schema);
  }

}
