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
import edu.kit.datamanager.exceptions.RangeNotSatisfyableException;
import edu.kit.datamanager.exceptions.UnauthorizedAccessException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.context.request.WebRequest;

/**
 * Utility class for controllers.
 *
 * @author jejkal
 */
@SuppressWarnings("UnnecessarilyFullyQualified")
public class ControllerUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(ControllerUtils.class);

    private static final Pattern CONTENT_RANGE_PATTERN = Pattern.compile("([\\d]+)[-]([\\d]+)[/]([\\d]+)");

    /**
     * Hidden constructor.
     */
    ControllerUtils() {
    }

    /**
     * Check the provided pagination information. This method can be used to
     * e.g. limit the maximum page size.
     *
     * @param pgbl The pageable object comming from the controller.
     *
     * @return The validated (and fixed) page request.
     */
    public static PageRequest checkPaginationInformation(Pageable pgbl) {
        return checkPaginationInformation(pgbl, pgbl.getSort());
    }

    /**
     * Check the provided pagination information. This method can be used to
     * e.g. limit the maximum page size. In addition, pre-defined sort criteria
     * can be added to the returned page request.
     *
     * @param pgbl The pageable object comming from the controller.
     * @param sort The sort criteria applied to the page request. If 'null',
     * Sort.unsorted() is applied.
     *
     * @return The validated (and fixed) page request. If pgbl is null, a page
     * request of page 0 with a size of 20 elements is returned.
     */
    public static PageRequest checkPaginationInformation(Pageable pgbl, Sort sort) {
        if (pgbl == null) {
            return PageRequest.of(0, 20);
        }
        int pageSize = pgbl.getPageSize();
        if (pageSize > 100) {
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
    public static void checkAnonymousAccess() throws UnauthorizedAccessException {
        if (AuthenticationHelper.isAnonymous()) {
            String message = "Please login in order to be able to perform this operation.";
            LOGGER.info(message);
            throw new UnauthorizedAccessException(message);
        }
    }

    /**
     * Check for administrator access using {@link AuthenticationHelper#hasAuthority(java.lang.String)
     * }. If no administrator access was detected, an AccessForbiddenException
     * is thrown.
     *
     * @throws AccessForbiddenException if the caller does not own
     * ROLE_ADMINISTRATOR.
     */
    public static void checkAdministratorAccess() {
        if (!AuthenticationHelper.hasAuthority(RepoUserRole.ADMINISTRATOR.getValue())) {
            LOGGER.warn("Caller is not allowed to perform the requested operation, ROLE_ADMINISTRATOR is required. Throwing AccessForbiddenException.");
            throw new AccessForbiddenException("Insufficient role. ROLE_ADMINISTRATOR required.");
        }
    }

    /**
     * Check the ETag provided by the caller against the current ETag provided
     * by a resource. If both ETags are not matching, an EtagMismatchException
     * is thrown.
     *
     * @param request The WebRequest containing all headers, e.g. the ETag.
     * @param resource A resource capable of providing its own ETag.
     *
     * @throws EtagMismatchException if the provided ETag is not matching the
     * current ETag.
     */
    public static void checkEtag(WebRequest request, EtagSupport resource) throws EtagMismatchException {
        String etagValue = getEtagFromHeader(request);

        checkEtag(etagValue, resource);
    }

    /**
     * Get the ETag from request header.
     *
     * @param request The WebRequest containing all headers, e.g. the ETag.
     *
     * return current ETag.
     */
    public static String getEtagFromHeader(WebRequest request) {
        String etagValue = request.getHeader("If-Match");
        LOGGER.trace("Received ETag: {}", etagValue);

        if (etagValue == null) {
            String message = "If-Match header with valid etag is missing.";
            LOGGER.trace(message);
            throw new EtagMissingException(message);
        }
        return etagValue;
    }

    /**
     * Check the ETag provided by the caller against the current ETag provided
     * by a resource. If both ETags are not matching, an EtagMismatchException
     * is thrown.
     *
     * @param etagValue eTag.
     * @param resource A resource capable of providing its own ETag.
     *
     * @throws EtagMismatchException if the provided ETag is not matching the
     * current ETag.
     */
    public static void checkEtag(String etagValue, EtagSupport resource) throws EtagMismatchException {
        String etag = resource.getEtag();
        LOGGER.trace("Checking ETag for resource with ETag {}.", etag);

        if (!etagValue.equals("\"" + etag + "\"")) {
            String message = String.format("ETag not matching or not provided. (provided: '%s' <-> resource: '%s')", etagValue, etag);
            LOGGER.trace(message);
            throw new EtagMismatchException(message);
        }
    }

    /**
     * Get the local hostname. If it's not possible to determine the fully
     * qualified local hostname, the default 'localhost' is returned.
     *
     * @return The fully qualified local hostname or localhost as default.
     */
    public static String getLocalHostname() {
        String hostname = "localhost";
        try {
            InetAddress inetAddress = InetAddress.getLocalHost();
            hostname = inetAddress.getHostName();
            LOGGER.trace("get local hostname: {} -> {}", inetAddress, hostname);
        } catch (UnknownHostException ex) {
            LOGGER.warn("Unable to determine local host address. Returning default hostname 'localhost'.", ex);
        }
        return hostname;
    }

    /**
     * Helper to parse a provided String identifier into Long. If parsing fails,
     * a BadArgumentException is thrown.
     *
     * @param id The String representation of a Long id.
     *
     * @return The Long representation of 'id'.
     */
    public static Long parseIdToLong(String id) {
        try {
            return Long.parseLong(id);
        } catch (NumberFormatException ex) {
            String message = "Provided id must be numeric.";
            LOGGER.trace(message);
            throw new BadArgumentException(message);
        }
    }

    /**Builds a Content-Range header value based on the provided arguments. 
     * The header format is {startIndex}-{endIndex}/totalElements for a valid
     * range. If the range exceeds totalElements, e.g., currentPage * pageSize
     * is larger than totalElements - 1, a RangeNotSatisfyableException is
     * thrown. If totalElements is 0, i.e., no results will be returned,
     * the Content-Range value will be &#42;/0.
     * 
     * @param currentPage The current page starting with 0.
     * @param pageSize The number of elements per page.
     * @param totalElement The total number of results.
     * 
     * @return A formatted string that can be used as Content-Range header.
     */
    public static String getContentRangeHeader(int currentPage, int pageSize, long totalElements) {
        if (totalElements > 0) {
            int indexStart = currentPage * pageSize;

            if (indexStart > totalElements - 1) {
                String message = "Current page '" + currentPage + "' is out of range!\n"
                        + "(Pagesize: '" + pageSize + "', total no of elements: '" + totalElements + "'";
                LOGGER.trace(message);
                throw new RangeNotSatisfyableException(message);
            }
            int indexEnd = (int) Math.min(indexStart + pageSize - 1, Math.max(0, totalElements - 1));
            return indexStart + "-" + indexEnd + "/" + totalElements;
        }

        //no elements in result
        return "*/0";
    }

    public static ContentRange parseContentRangeHeader(String headerValue) {
        ContentRange range = new ContentRange();
        if (headerValue == null || headerValue.startsWith("*")) {
            //cover case where no content-range is present or value is */0 for empty result
            range.indexStart = 0;
            range.indexEnd = 0;
            range.totalElements = 0;
        } else {
            Matcher m = CONTENT_RANGE_PATTERN.matcher(headerValue);

            if (m.find()) {
                range.indexStart = Integer.parseInt(m.group(1));
                range.indexEnd = Integer.parseInt(m.group(2));
                range.totalElements = Long.parseLong(m.group(3));
            }
        }
        return range;
    }

    @Data
    public static class ContentRange {

        public static ContentRange empty() {
            return new ContentRange();
        }

        private int indexStart = 0;
        private int indexEnd = 0;
        private long totalElements = 0l;

    }
}
