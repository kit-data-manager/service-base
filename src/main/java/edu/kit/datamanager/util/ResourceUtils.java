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

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import javax.xml.bind.DatatypeConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author jejkal
 */
public class ResourceUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(ResourceUtils.class);

    public static String hashElements(String... values) {
        if (values == null || values.length == 0) {
            return "";
        }

        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-1");
            for (String value : values) {
                if (value != null) {
                    messageDigest.update(value.getBytes());
                }
            }
            return DatatypeConverter.printHexBinary(messageDigest.digest());
        } catch (NoSuchAlgorithmException ex) {
            LOGGER.warn("Hashing algorithm SHA-1 not supported. Creating pseudo-hash using append.");
            StringBuilder result = new StringBuilder();
            for (String value : values) {
                result.append(value);
            }
            return result.toString();
        }
    }

}
