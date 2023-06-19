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
package edu.kit.datamanager.annotations;

import edu.kit.datamanager.validator.LocalFolderValidator;
import java.lang.annotation.Documented;
import static java.lang.annotation.ElementType.FIELD;
import java.lang.annotation.Retention;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import java.lang.annotation.Target;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

/**
 * Test for local folder.
 *
 * @author jejkal
 */
@Target({FIELD})
@Retention(RUNTIME)
@Constraint(validatedBy = LocalFolderValidator.class)
@Documented
public @interface LocalFolderURL{

  /**
   * Get info message.
   * @return Human readable message.
   */
  String message() default "Provided folder URL invalid. A valid URL to a writable local folder is required.";

  /**
   * Restrict the set of constraints to groups.
   * @return Allowed groups.
   */
  Class<?>[] groups() default {};

  /**
   * Assign custom payload objects to a constraint
   * @return Custom payload.
   */
  Class<? extends Payload>[] payload() default {};

}
