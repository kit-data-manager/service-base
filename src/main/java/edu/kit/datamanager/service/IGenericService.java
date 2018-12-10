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

import com.github.fge.jsonpatch.JsonPatch;
import edu.kit.datamanager.exceptions.PatchApplicationException;
import edu.kit.datamanager.exceptions.ResourceNotFoundException;
import edu.kit.datamanager.exceptions.UpdateForbiddenException;
import java.util.Collection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.GrantedAuthority;

/**
 *
 * @author jejkal
 */
public interface IGenericService<C>{

  /**
   * Find a user by its numeric id. This method is intended to return either the
   * user with the provided id or throw a ResourceNotFoundException in case no
   * user with the provided id exists. It should NOT return 'null'.
   *
   * @param id The id of the user.
   *
   * @return The user with the provided id.
   *
   * @throws ResourceNotFoundException if no user with the provided id exists.
   */
  C findById(final String id) throws ResourceNotFoundException;

  /**
   * Basic find by example method. An implementation of this method is not
   * intended to imply any specific context or authentication information. It is
   * expected to use the provided information in order to create a query to the
   * data backend and to return appropriate results.
   *
   * The example is used to create a query to the data backend. It depends on
   * the implementation which fields of the example are evaluated, at least
   * simple fields should be evaluated.
   *
   * The result can be requested in a paginated form using the pgbl argument.
   *
   * @param example The example user used to build the query for assigned
   * values.
   * @param pgbl The pageable object containing pagination information.
   *
   * @return A page object containing all matching users on the current page.
   * The list of results might be empty, but the result should NOT be 'null'.
   */
  Page<C> findAll(C example, Pageable pgbl);

  /**
   * Apply the provided patch to the provided resource. As all checks, e.g. if
   * resource exists and is writable, must be performed outside, the patch can
   * be applied directly if possible and the resource should be persisted.
   *
   * @param resource The resource to patch.
   * @param patch The JsonPatch to apply.
   * @param userGrants The grants of the caller.
   *
   * @throws PatchApplicationException if the patch cannot be applied for
   * unknown reasons.
   * @throws UpdateForbiddenException if the patch cannot be applied because a
   * field is affected that is not allowed to be changed, e.g. the numeric id.
   */
  void patch(C resource, JsonPatch patch, Collection<? extends GrantedAuthority> userGrants) throws PatchApplicationException, UpdateForbiddenException;

  /**
   * Delete the provided user. In order to 'delete' a user it is recommended, to
   * disable to user instead of physical removal. This has the advantage, there
   * are no side effects expected when deleting a user, and disabling a user is
   * revertible.
   *
   * If required, physical deletion can be performed as separate process in the
   * background or if 'delete' was called again on an already disabled user.
   *
   * @param entity The user to delete.
   */
  void delete(final C entity);
}
