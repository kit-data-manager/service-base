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
package edu.kit.datamanager.configuration;

import edu.kit.datamanager.annotations.SearchIndex;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import edu.kit.datamanager.annotations.SearchIndexUrl;

/**
 * Search configuration used by SearchController.
 *
 * @author jejkal
 */
@Configuration
@Data
public class SearchConfiguration {

    /**
     * Property defining whether the search endpoint of the SearchController is
     * available or not. Furthermore, this property is used to decide whether to
     * perform indexing or not in a particular service. By default, search is
     * disabled.
     */
    @Value("${repo.search.enabled:FALSE}")
    private boolean searchEnabled;

    /**
     * Property for defining the search endpoint's base URL. It may only
     * contains protocol, hostname, and port and must not end with a slash. All
     * other parts, i.e., index and search endpoint, are added by the
     * SearchController. by default, an installation at 'http://localhost:9200'
     * is adressed.
     */
    @Value("${repo.search.url:http\\://localhost:9200}")
    @SearchIndexUrl
    private String url;

    /**
     * The index search in. This property also supports multi-index search,
     * e.g., while using values like 'index1,index2' or 'index*'. By default,
     * all indices ('*') are queried.
     */
    @Value("${repo.search.index:*}")
    @SearchIndex
    private String index;

}
