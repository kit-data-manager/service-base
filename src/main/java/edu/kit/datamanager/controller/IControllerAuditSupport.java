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
package edu.kit.datamanager.controller;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import javax.servlet.http.HttpServletResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.util.UriComponentsBuilder;

/**
 *
 * @author jejkal
 */
public interface IControllerAuditSupport{

  @ApiOperation(value = "Access content information for single or multiple data elements.",
          notes = "List metadata of one or more content elements associated with a data resource in a paginated and/or sorted form. This endpoint is addressed if the caller provides content type "
          + "'application/vnd.datamanager.content-information+json' within the 'Accept' header. If this content type is not present, the content element is downloaded instead."
          + "The content path, defining whether one or more content element(s) is/are returned, is provided within the request URL. Everything after 'data/' is expected to be either a virtual folder or single content element. "
          + "If the provided content path ends with a slash, it is expected to represent a virtual collection which should be listed. If the content path does not end with a slash, it is expected to refer to a single element. "
          + "If not element with the exact content path exists, HTTP NOT_FOUND is returned. The user may provide custom sort criteria for ordering the returned elements. If no sort criteria is provided, the default sorting is "
          + "applied which returning all matching elements in ascending order by hierarchy depth and alphabetically by their relative path.")
  @ApiImplicitParams(value = {
    @ApiImplicitParam(name = "page", dataType = "integer", paramType = "query", value = "Results page you want to retrieve (0..N)"),
    @ApiImplicitParam(name = "size", dataType = "integer", paramType = "query", value = "Number of records per page."),
    @ApiImplicitParam(name = "sort", allowMultiple = true, dataType = "string", paramType = "query", value = "Sorting criteria in the format: property(,asc|desc). Default sort order is ascending. Multiple sort criteria are supported.")
  })
  @RequestMapping(path = "/{id}", method = RequestMethod.GET, produces = "application/vnd.datamanager.audit+json")
  @ResponseBody
  public ResponseEntity getAuditInformation(@ApiParam(value = "The resource identifier.", required = true) @PathVariable(value = "id") final String id,
          final Pageable pgbl,
          final WebRequest request,
          final HttpServletResponse response,
          final UriComponentsBuilder uriBuilder);

}
