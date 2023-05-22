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
package edu.kit.datamanager.validator;

import java.net.URISyntaxException;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import edu.kit.datamanager.annotations.LocalFolderURL;

/**
 * Validator checking a property to be a local folder.
 *
 * @author jejkal
 */
public class LocalFolderValidator implements ConstraintValidator<LocalFolderURL, java.net.URL> {

    private static final Logger LOGGER = LoggerFactory.getLogger(LocalFolderValidator.class);

    @Override
    public boolean isValid(java.net.URL value, ConstraintValidatorContext context) {
        boolean folderValid = false;
        if (value == null) {
            LOGGER.error("Provided value is null.");
            return folderValid;
        }
        try {
            LOGGER.trace("Successfully validated folder URL {}. Checking local path.", value.toURI().toString());
            Path basePath = Paths.get(value.toURI());

            if (!Files.exists(basePath)) {
                LOGGER.trace("Folder at {} does not exist. Try creating it.", basePath);
                Path basePathCreated = Files.createDirectories(basePath);
                LOGGER.info("Successfully created folder from URL {} at {}.", value, basePathCreated);
                folderValid = true;
            } else {
                if (!Files.isWritable(basePath)) {
                    LOGGER.error("Folder at {} exists, but is not writable.", basePath);
                } else {
                    LOGGER.trace("Folder at {} exists and is writable.", basePath);
                    folderValid = true;
                }
            }
        } catch (URISyntaxException ex) {
            LOGGER.error("Failed to validate folder property with value " + value + ".", ex);
        } catch (IOException ex) {
            LOGGER.error("Failed to create folder for URL " + value + " at local filesystem.", ex);
        }
        return folderValid;
    }
}
