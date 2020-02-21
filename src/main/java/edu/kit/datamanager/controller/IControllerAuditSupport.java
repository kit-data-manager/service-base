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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import javax.servlet.http.HttpServletResponse;
import org.springdoc.core.converters.PageableAsQueryParam;
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

  @Operation(summary = "Access audit information for a single resource.",
          description = "List audit information for a resource in a paginated form. Sorting can be supported but is optional. If no sorting is supported it is recommended to return audit "
          + "information sorted by version number in descending order. This endpoint is addressed if the caller provides content type "
          + "'application/vnd.datamanager.audit+json' within the 'Accept' header. If no audit support is enabled or no audit information are available for a certain resource, "
          + "an empty result should be returned. ")
  @RequestMapping(path = "/{id}", method = RequestMethod.GET, produces = "application/vnd.datamanager.audit+json")
  @ResponseBody
  @PageableAsQueryParam
  public ResponseEntity getAuditInformation(
          @Parameter(description = "The resource identifier.", required = true) @PathVariable(value = "id") final String id,
          @Parameter(required = false) final Pageable pgbl,
          final WebRequest request,
          final HttpServletResponse response,
          final UriComponentsBuilder uriBuilder);

}
