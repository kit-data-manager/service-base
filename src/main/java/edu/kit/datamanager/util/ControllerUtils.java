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
package edu.kit.datamanager.util;

import edu.kit.datamanager.entities.EtagSupport;
import edu.kit.datamanager.entities.RepoUserRole;
import edu.kit.datamanager.exceptions.AccessForbiddenException;
import edu.kit.datamanager.exceptions.BadArgumentException;
import edu.kit.datamanager.exceptions.EtagMismatchException;
import edu.kit.datamanager.exceptions.EtagMissingException;
import edu.kit.datamanager.exceptions.UnauthorizedAccessException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.context.request.WebRequest;
/**
 *
 * @author jejkal
 */
public class ControllerUtils{

  private static final Logger LOGGER = LoggerFactory.getLogger(ControllerUtils.class);

  /**
   * Hidden constructor.
   */
  private ControllerUtils(){
  }

  /**
   * Check the provided pagination information. This method can be used to e.g.
   * limit the maximum page size.
   *
   * @param pgbl The pageable object comming from the controller.
   *
   * @return The validated (and fixed) page request.
   */
  public static PageRequest checkPaginationInformation(Pageable pgbl){
    return checkPaginationInformation(pgbl, pgbl.getSort());
  }

  /**
   * Check the provided pagination information. This method can be used to e.g.
   * limit the maximum page size. In addition, pre-defined sort criteria can be
   * added to the returned page request.
   *
   * @param pgbl The pageable object comming from the controller.
   * @param sort The sort criteria applied to the page request. If 'null',
   * Sort.unsorted() is applied.
   *
   * @return The validated (and fixed) page request.
   */
  public static PageRequest checkPaginationInformation(Pageable pgbl, Sort sort){
    int pageSize = pgbl.getPageSize();
    if(pageSize > 100){
      LOGGER.debug("Restricting user-provided page size {} to max. page size 100.", pageSize);
      pageSize = 100;
    }
    LOGGER.trace("Rebuilding page request for page {}, size {} and sort {}.", pgbl.getPageNumber(), pageSize, pgbl.getSort());
    return PageRequest.of(pgbl.getPageNumber(), pageSize, (sort != null) ? sort : Sort.unsorted());
  }

  /**
   * Check for anonymous access using {@link AuthenticationHelper#isAnonymous()
   * }. If anonymous access was detected, an UnauthorizedAccessException is
   * thrown.
   *
   * @throws UnauthorizedAccessException if anonyous access was detected.
   */
  public static void checkAnonymousAccess() throws UnauthorizedAccessException{
    if(AuthenticationHelper.isAnonymous()){
      throw new UnauthorizedAccessException("Please login in order to be able to perform this operation.");
    }
  }

  /**
   * Check for administrator access using {@link AuthenticationHelper#hasAuthority(java.lang.String)
   * }. If no administrator access was detected, an AccessForbiddenException is
   * thrown.
   *
   * @throws AccessForbiddenException if the caller does not own
   * ROLE_ADMINISTRATOR.
   */
  public static void checkAdministratorAccess(){
    if(!AuthenticationHelper.hasAuthority(RepoUserRole.ADMINISTRATOR.getValue())){
      LOGGER.warn("Caller is not allowed to perform the requested operation, ROLE_ADMINISTRATOR is required. Throwing AccessForbiddenException.");
      throw new AccessForbiddenException("Insufficient role. ROLE_ADMINISTRATOR required.");
    }
  }

  /**
   * Check the ETag provided by the caller against the current ETag provided by
   * a resource. If both ETags are not matching, an EtagMismatchException is
   * thrown.
   *
   * @param request The WebRequest containing all headers, e.g. the ETag.
   * @param resource A resource capable of providing its own ETag.
   *
   * @throws EtagMismatchException if the provided ETag is not matching the
   * current ETag.
   */
  public static void checkEtag(WebRequest request, EtagSupport resource) throws EtagMismatchException{
    String etag = resource.getEtag();
    LOGGER.trace("Checking ETag for resource with ETag {}.", etag);
    String etagValue = request.getHeader("If-Match");
    LOGGER.trace("Received ETag: {}", etagValue);

    if(etagValue == null){
      throw new EtagMissingException("If-Match header with valid etag is missing.");
    }

    if(!etagValue.equals("\"" + etag + "\"")){
      throw new EtagMismatchException("ETag not matching or not provided.");
    }
  }

  /**
   * Get the local hostname. If it's not possible to determine the fully
   * qualified local hostname, the default 'localhost' is returned.
   *
   * @return The fully qualified local hostname or localhost as default.
   */
  public static String getLocalHostname(){
    String hostname = "localhost";
    try{
      InetAddress inetAddress = InetAddress.getLocalHost();
      hostname = inetAddress.getHostName();
    } catch(UnknownHostException ex){
      LOGGER.warn("Unable to determine local host address. Returning default hostname 'localhost'.", ex);
    }
    return hostname;
  }

  /**
   * Helper to parse a provided String identifier into Long. If parsing fails, a
   * BadArgumentException is thrown.
   *
   * @param id The String representation of a Long id.
   *
   * @return The Long representation of 'id'.
   */
  public static Long parseIdToLong(String id){
    try{
      return Long.parseLong(id);
    } catch(NumberFormatException ex){
      throw new BadArgumentException("Provided id must be numeric.");
    }
  }
}
