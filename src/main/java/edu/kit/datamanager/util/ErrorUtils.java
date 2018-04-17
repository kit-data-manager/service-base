/*
 * Copyright 2017 Karlsruhe Institute of Technology.
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

import java.text.MessageFormat;

/**
 *
 * @author jejkal
 */
public class ErrorUtils {

    public final static String NO_RESOURCE_PROVIDED_ERROR = "No resource provided.";
    private final static String INVALID_RESOURCE_PROVIDED_ERROR = "Invalid resource provided. Cause: {0}";
    private final static String INTERNAL_SERVER_ERROR = "An internal server error occured. Cause: {0}";
    public final static String RESOURCE_REVOKED_ERROR = "The resource has been revoked.";
    public final static String RESOURCE_FIXED_ERROR = "Resource has been fixed. Further modifications not allowed.";
    private final static String RESOURCE_ETAG_NOT_MATCHING_ERROR = "Etag not matching. Current Etag is: {0}";
    public final static String NO_DATA_ERROR = "No data stream found.";
    public final static String CONTENT_DISPOSITION_HEADER_WITHOUT_FILENAME_ERROR = "File name is missing in content disposition header.";
    public final static String INVALID_UPLOAD_DESTINATION_ERROR = "Upload points to an existing folder.";
    public final static String DATA_STREAM_READ_ERROR = "Failed to read data stream.";
    public final static String DATA_STREAM_WRITE_ERROR = "Failed to write data stream.";
    public final static String INVALID_CONTENT_METADATA_ERROR = "Invalid content metadata document.";
    public final static String NO_CONTENT_METADATA_ERROR = "No content metadata document found.";

    public static String getInvalidResourceError(String cause) {
        return MessageFormat.format(INVALID_RESOURCE_PROVIDED_ERROR, cause);
    }

    public static String getInternalServerError(String cause) {
        return MessageFormat.format(INTERNAL_SERVER_ERROR, (cause == null) ? "Unknown" : cause);
    }

    public static String getResourceEtagNotMatchingError(String expectedEtag) {
        return MessageFormat.format(RESOURCE_ETAG_NOT_MATCHING_ERROR, expectedEtag);
    }
}
