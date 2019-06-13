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
package edu.kit.datamanager.controller;

import com.github.fge.jsonpatch.JsonPatch;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import java.time.Instant;
import java.util.List;
import javax.servlet.http.HttpServletResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * Generic resource controller providing basic CRUD and search operations.
 *
 * @author jejkal
 * @param <C> Resource class
 */
public interface IGenericResourceController<C>{

  @ApiOperation(value = "Create a new resource.",
          notes = "Create a new resource and return it to the caller in case of success. "
          + "Creating new resources may or may not be restricted to users possessing specific roles.")
  @RequestMapping(value = "/", method = RequestMethod.POST)
  @ResponseBody
  public ResponseEntity<C> create(@ApiParam(value = "Json representation of the resource to create.", required = true) @RequestBody C resource,
          final WebRequest request,
          final HttpServletResponse response);

  @ApiOperation(value = "Get a resource by id.",
          notes = "Obtain is single resource by its identifier. Depending on a user's role, accessing a specific resource may be allowed or forbidden.")
  @RequestMapping(value = "/{id}", method = RequestMethod.GET)
  @ResponseBody
  public ResponseEntity<C> getById(@ApiParam(value = "The resource identifier.", required = true) @PathVariable("id") final String id,
          @ApiParam(value = "The version of the resource, if supported.", required = false) @RequestParam("version") final Long version,
          final WebRequest request,
          final HttpServletResponse response);

  @ApiOperation(value = "List all resources.",
          notes = "List all resources in a paginated and/or sorted form. Possible queries are: listing with default values (X elements on first page sorted by database), "
          + "listing page wise, sorted query page wise, and combinations of the options above. "
          + "The total number of resources may differ between callers if single resources have access restrictions. "
          + "Furthermore, anonymous listing of resources may or may not be supported.")
  @ApiImplicitParams({
    @ApiImplicitParam(name = "page", dataType = "integer", paramType = "query",
            value = "Results page you want to retrieve (0..N)"),
    @ApiImplicitParam(name = "size", dataType = "integer", paramType = "query",
            value = "Number of resources per page."),
    @ApiImplicitParam(name = "sort", allowMultiple = true, dataType = "string", paramType = "query",
            value = "Sorting criteria in the format: property(,asc|desc). "
            + "Default sort order is ascending. "
            + "Multiple sort criteria are supported.")
  })
  @RequestMapping(value = "/", method = RequestMethod.GET)
  @ResponseBody
  public ResponseEntity<List<C>> findAll(@RequestParam(value = "The UTC time of the earliest update of a returned resource.", name = "from", required = false) final Instant lastUpdateFrom,
          @RequestParam(value = "The UTC time of the latest update of a returned resource.", name = "until", required = false) final Instant lastUpdateUntil,
          final Pageable pgbl,
          final WebRequest request,
          final HttpServletResponse response,
          final UriComponentsBuilder uriBuilder);

  @ApiOperation(value = "List resources by example.",
          notes = "List all resources in a paginated and/or sorted form by example using an example document provided in the request body. "
          + "The example is a normal instance of the resource. However, search-relevant top level primitives are marked as 'Searchable' within the implementation. "
          + "For string values, '%' can be used as wildcard character. "
          + "If the example document is omitted, the response is identical to listing all resources with the same pagination parameters. "
          + "As well as listing of all resources, the number of total results might be affected by the caller's role.")
  @ApiImplicitParams({
    @ApiImplicitParam(name = "page", dataType = "integer", paramType = "query",
            value = "Results page you want to retrieve (0..N)"),
    @ApiImplicitParam(name = "size", dataType = "integer", paramType = "query",
            value = "Number of records per page."),
    @ApiImplicitParam(name = "sort", allowMultiple = true, dataType = "string", paramType = "query",
            value = "Sorting criteria in the format: property(,asc|desc). "
            + "Default sort order is ascending. "
            + "Multiple sort criteria are supported.")
  })
  @RequestMapping(value = "/search", method = RequestMethod.POST)
  @ResponseBody
  public abstract ResponseEntity<List<C>> findByExample(@ApiParam(value = "Json representation of the resource serving as example for the search operation. Typically, only first level primitive attributes are evaluated while building queries from examples.", required = true) @RequestBody C example,
          @RequestParam(value = "The UTC time of the earliest update of a returned resource.", name = "from", required = false) final Instant lastUpdateFrom,
          @RequestParam(value = "The UTC time of the latest update of a returned resource.", name = "until", required = false) final Instant lastUpdateUntil,
          final Pageable pgbl,
          final WebRequest request,
          final HttpServletResponse response,
          final UriComponentsBuilder uriBuilder);

  @ApiOperation(value = "Patch a resource by id.",
          notes = "Patch a single or multiple fields of a resource. Patching information are provided in JSON Patch format using Content-Type 'application/json-patch+json'. "
          + "Patching a resource requires privileged access to the resource to patch or ADMIN permissions of the caller. "
          + "Depending on the resource, single fields might be protected and cannot be changed, e.g. the unique identifier. "
          + "If the patch tries to modify a protected field, HTTP BAD_REQUEST will be returned before persisting the result.")
  @RequestMapping(value = "/{id}", method = RequestMethod.PATCH, consumes = "application/json-patch+json")
  @ResponseBody
  public ResponseEntity patch(@ApiParam(value = "The resource identifier.", required = true) @PathVariable("id") final String id,
          @ApiParam(value = "Json representation of a json patch document. The document must comply with RFC 6902 specified by the IETF.", required = true) @RequestBody JsonPatch patch,
          final WebRequest request,
          final HttpServletResponse response);

  @ApiOperation(value = "Replace a resource.",
          notes = "Replace a resource by a new resource provided by the user."
          + "Putting a resource requires privileged access to the resource to patch or ADMIN permissions of the caller. "
          + "Some resource fields might be protected and cannot be changed, e.g. the unique identifier. "
          + "If at least one protected field in the new resource does not match with the current value, HTTP BAD_REQUEST will be returned before persisting the result."
          + "Attention: Due to the availability of PATCH, PUT support is optional! If a resource won't provide PUT support, HTTP NOT_IMPLEMENTED is returned.")
  @RequestMapping(value = "/{id}", method = RequestMethod.PUT, consumes = "application/json")
  @ResponseBody
  public ResponseEntity put(@ApiParam(value = "The resource identifier.", required = true) @PathVariable("id") final String id,
          @ApiParam(value = "Json representation of the new representation of the resource.", required = true) @RequestBody C resource,
          final WebRequest request,
          final HttpServletResponse response);

  @ApiOperation(value = "Delete a resource by id.",
          notes = "Delete a single resource. Deleting a resource typically requires the caller to have ADMIN permissions. "
          + "In some cases, deleting a resource can also be available for the owner or other privileged users or can be forbidden. "
          + "For resources whose deletion may affect other resources or internal workflows, physical deletion might not be possible at all. "
          + "In those cases, the resource might be disabled/hidden but not removed from the database. This can then happen optionally at "
          + "a later point in time, either automatically or manually.")
  @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
  @ResponseBody
  public ResponseEntity delete(@ApiParam(value = "The resource identifier.", required = true) @PathVariable("id") final String id,
          final WebRequest request,
          final HttpServletResponse response);
}
