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
package edu.kit.datamanager.service.impl;

import edu.kit.datamanager.entities.ContentElement;
import edu.kit.datamanager.service.IContentCollectionProvider;
import edu.kit.datamanager.service.IVersioningService;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotEmpty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.server.UnsupportedMediaTypeStatusException;

/**
 *
 * @author jejkal
 */
@Component
public class FileArchiveContentCollectionProvider implements IContentCollectionProvider{

  private final static Logger LOGGER = LoggerFactory.getLogger(FileArchiveContentCollectionProvider.class);

  public final static MediaType ZIP_MEDIA_TYPE = MediaType.parseMediaType("application/zip");
  @Autowired
  private IVersioningService[] versioningServices;

  @Override
  public void provide(@NotEmpty List<ContentElement> collection, MediaType mediaType, HttpServletResponse response){
    if(!ZIP_MEDIA_TYPE.toString().equals(mediaType.toString())){
      LOGGER.error("Unsupported media type {} received. Throwing HTTP 415 (UNSUPPORTED_MEDIA_TYPE).", mediaType);
      throw new UnsupportedMediaTypeStatusException(mediaType, Arrays.asList(getSupportedMediaTypes()));
    }

    LOGGER.trace("Setting content type {}.", mediaType);
    response.setContentType(mediaType.toString());
    //response.setHeader("Content-Disposition", "attachment;filename=download.zip");
    LOGGER.trace("Setting response status {}.", HttpServletResponse.SC_OK);
    response.setStatus(HttpServletResponse.SC_OK);
    LOGGER.trace("Starting packaging operation.");
    try(ZipOutputStream zippedOut = new ZipOutputStream(response.getOutputStream())){
      for(ContentElement element : collection){

        //  LOGGER.trace("Opening new file system resource for element URI {}.", element.getContentUri());
        //  FileSystemResource resource = new FileSystemResource(Paths.get(element.getContentUri()));
        LOGGER.trace("Adding new zip entry for element {}.", element.getRelativePath());
        ZipEntry e = new ZipEntry(element.getRelativePath());
        // Configure the zip entry, the properties of the file
        LOGGER.trace("Setting entry size to {}.", element.getContentLength());
        e.setSize(element.getContentLength());
        LOGGER.trace("Setting element time to current timestamp.");
        e.setTime(System.currentTimeMillis());
        LOGGER.trace("Writing element to zip stream.");
        zippedOut.putNextEntry(e);

        for(IVersioningService versioningService : versioningServices){
          if(element.getVersioningService().equals(versioningService.getServiceName())){
            versioningService.configure();

            Map<String, String> options = new HashMap<>();
            options.put("contentUri", element.getContentUri());
            options.put("checksum", element.getChecksum());
            options.put("size", Long.toString(element.getContentLength()));

            versioningService.read(element.getResourceId(), null, element.getRelativePath(), Integer.toString(element.getVersion()), zippedOut, options);
            break;
          }
        }

        //  LOGGER.trace("Starting streaming resource content.");
        //  StreamUtils.copy(resource.getInputStream(), zippedOut);
        LOGGER.trace("Closing entry.");
        zippedOut.closeEntry();

      }
      LOGGER.trace("Finishing zip operation.");
      zippedOut.finish();
      LOGGER.trace("Zip operation successfully finished.");
    } catch(Exception e){
      // Exception handling goes here
      LOGGER.error("Failed to package requested collection.", e);
      //just end as we cannot do anything else here...the status has already been set, so no server error can be returned.
    }
  }

  @Override
  public boolean canProvide(String schema){
    LOGGER.trace("Calling canProvide({}).", schema);
    return "file".equals(schema);
  }

  @Override
  public boolean supportsMediaType(MediaType mediaType){
    LOGGER.trace("Calling supportsMediaType({}).", mediaType);
    if(mediaType == null){
      return false;
    }
    return ZIP_MEDIA_TYPE.toString().equals(mediaType.toString());
  }

  @Override
  public MediaType[] getSupportedMediaTypes(){
    LOGGER.trace("Calling getSupportedMediaTypes().");
    return new MediaType[]{ZIP_MEDIA_TYPE};
  }

}
