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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author jejkal
 */
public class FilenameUtils {

    //FAT32 reserved characters
    private static final Pattern PATTERN = Pattern.compile("[%\"\\*/:<>\\?\\\\\\|\\+,;=\\[\\]]");
    private static final int MAX_LENGTH = 255;

    public static String escapeStringAsFilename(String in) {

        StringBuffer sb = new StringBuffer();

        // Apply the regex.
        Matcher m = PATTERN.matcher(in);

        while (m.find()) {
            // Convert matched character to percent-encoded.
            String replacement = "%" + Integer.toHexString(m.group().charAt(0)).toUpperCase();
            m.appendReplacement(sb, replacement);
        }
        m.appendTail(sb);

        String encoded = sb.toString();

        // Truncate the string.
        int end = Math.min(encoded.length(), MAX_LENGTH);
        String substring = encoded.substring(0, end);
        String regex = "\\.(?=.*\\.)";
        return substring.replaceAll(regex, "%2E");
    }
}
