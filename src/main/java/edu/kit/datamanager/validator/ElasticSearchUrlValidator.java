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
package edu.kit.datamanager.validator;

import edu.kit.datamanager.util.ElasticSearchUtil;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import edu.kit.datamanager.annotations.SearchIndexUrl;
import java.net.URL;

/**
 * Validates an elastic base url, e.g., http://localhost:9200.
 *
 * @author jejkal
 */
@SuppressWarnings("UnnecessarilyFullyQualified")
public class ElasticSearchUrlValidator implements ConstraintValidator<SearchIndexUrl, URL> {

  private static final Logger LOGGER = LoggerFactory.getLogger(ElasticSearchUrlValidator.class);

  @Override
  public boolean isValid(URL value, ConstraintValidatorContext context) {
    boolean validElasticsearchUrl = false;
    if (value == null) {
      LOGGER.error("Provided value is null.");
      return validElasticsearchUrl;
    }
    validElasticsearchUrl = ElasticSearchUtil.testForElasticsearch(value);

    return validElasticsearchUrl;
  }
}
