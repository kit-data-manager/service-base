/*
 * Copyright 2022 Karlsruhe Institute of Technology.
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

import edu.kit.datamanager.validator.SearchIndexValidator;
import java.lang.annotation.Documented;
import static java.lang.annotation.ElementType.FIELD;
import java.lang.annotation.Retention;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import java.lang.annotation.Target;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

/**Annotation for marking search index property for validation.
 *
 * @author jejkal
 */
@Target({FIELD})
@Retention(RUNTIME)
@Constraint(validatedBy = SearchIndexValidator.class)
@Documented
public @interface SearchIndex{

  String message() default "Provided search index invalid. A valid index to a search instance is required. (e.g.: index, index1, index*, index1,index2)";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};

}
