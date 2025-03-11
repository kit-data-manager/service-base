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
import edu.kit.datamanager.annotations.SearchIndexUrl;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Search configuration used by SearchController.
 *
 * The search configuration covers three properties:
 * 
 * <ul> 
 *  <li>repo.search.enabled - TRUE/FALSE, determined whether search capabilities will be enabled or not. Default: FALSE</li>
 *  <li>repo.search.url - URL to a running Elastic instance used as search backend. The URL will be validated at instantiation time using a connection check. See below for further elaborations on that and potential issues. Default: http://localhost:9200</li>
 *  <li>repo.search.index - One or more indices in the given Elastic index included in the search. The provided value should be in lowercase and may contain multiple enties separated by , (i.e. index1,index2) as well as wildcard character * to select a range of indices (i.e. index*). Default: *</li>
 * </ul>
 *
 * To ensure a proper configuration, <i>repo.search.url</i> is validated using a connection check for the configured elastic instance as soon as ElasticConfiguration is instantiated. This means, that if you use SearchConfiguration
 * in your project and you want to allow to disable search via <i>repo.search.enabled:FALSE</i> you should ensure, that in this case, SearchConfiguration is NOT instantiated as this will cause a validation exception if the connection
 * check to the Elastic instance fails. This can be achieved by conditional use of SearchConfiguration as shown in the following example:
 *
 * <pre>{@code 
 * @Bean
 * @ConfigurationProperties("repo")
 * @ConditionalOnProperty(prefix = "repo.search", name = "enabled", havingValue = "true")
 * public SearchConfiguration searchConfiguration() {
 *  return new SearchConfiguration();
 * }  
 * }</pre>
 * 
 * If using ConditionalOnProperty the bean will only be instantiated if property <i>repo.search.enabled</i> is TRUE, otherwise the bean remains 'null'.
 *
 * @author jejkal
 */
@Configuration
@Data
@Validated
@SuppressWarnings("UnnecessarilyFullyQualified")
public class SearchConfiguration {
    /** 'Default property defining pattern for detecting search endpoint.
     * Default endpoint should be: '/context/api/v1/search' or '/context/api/v1/_search'
     * Pattern: (/[^/]+)?/api/v\d+/_?search$
     */
    public static final String DEFAULT_SEARCH_ENDPOINT_PATTERN = "(/[^/]+)?/api/v\\d+/_?search$";

    /** Default property defining headers to be deduplicated in search response.
     * Default headers: 'Transfer-Encoding'
     */
    public static final String DEDUP_HEADERS = "Transfer-Encoding";

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
    @Value("${repo.search.url:http://localhost:9200}")
    @SearchIndexUrl
    private URL url;

    /**
     * The index search in. This property also supports multi-index search,
     * e.g., while using values like 'index1,index2' or 'index*'. By default,
     * all indices ('*') are queried.
     */
    @Value("${repo.search.index:*}")
    @SearchIndex
    private String index;

    /**
     * Property defining the search endpoint pattern. The pattern is used to
     * detect the search endpoint in the request URL. By default, the pattern is
     * set to '(/[^/]+)?/api/v\d+/_?search$'.
     */
    @Value(("${repo.search.endpointPattern:" + DEFAULT_SEARCH_ENDPOINT_PATTERN + "}"))
    private List<String> searchEndpointPatterns;

    /**
     * Property defining headers to be deduplicated in search response.
     * By default, the header 'Transfer-Encoding' is deduplicated.
     */
    @Value("${repo.search.dedupHeaders:" + DEDUP_HEADERS + "}")
    private List<String> dedupHeaders;

    /** List of deduplicated headers in lowercase. */
    private List<String> dedupHeadersLowerCase = null;

    public List<String> getDedupHeaders(){
        if (dedupHeadersLowerCase == null) {
            dedupHeadersLowerCase = new ArrayList<>();
            for (String header : dedupHeaders) {
                dedupHeadersLowerCase.add(header.toLowerCase());
            }
        }
        return dedupHeadersLowerCase;
    }

}
