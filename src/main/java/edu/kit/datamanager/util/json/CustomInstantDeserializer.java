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
package edu.kit.datamanager.util.json;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import java.io.IOException;
import java.time.DateTimeException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;

/**
 *
 * @author jejkal
 */
public class CustomInstantDeserializer extends JsonDeserializer<Instant> {

    private final DateTimeFormatter isoFormat = DateTimeFormatter.ISO_DATE_TIME.withZone(ZoneOffset.UTC);
    //additional date patterns according to DataCite JSON spec ordered by probability of use
    private final DateTimeFormatter[] additionalFormats = new DateTimeFormatter[]{
        new DateTimeFormatterBuilder()
        .appendPattern("yyyy")
        .parseDefaulting(ChronoField.MONTH_OF_YEAR, 1)
        .parseDefaulting(ChronoField.DAY_OF_MONTH, 1)
        .toFormatter(),
        DateTimeFormatter.ofPattern("yyyy-MM-dd"),
        new DateTimeFormatterBuilder()
        .appendPattern("yyyy-MM")
        .parseDefaulting(ChronoField.DAY_OF_MONTH, 1)
        .toFormatter()
    };

    @Override
    public Instant deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        if (p.getText() == null || p.getText().length() == 0) {
            return null;
        }

        try {
            return Instant.from(isoFormat.parse(p.getText())).truncatedTo(ChronoUnit.MILLIS);
        } catch (DateTimeParseException ex) {
            //no iso format...continue with other formats
        }
        for (DateTimeFormatter formatter : additionalFormats) {
            try {
                LocalDate date = LocalDate.from(formatter.parse(p.getText()));
                return Instant.from(date.atStartOfDay(ZoneOffset.UTC).toInstant());
            } catch (DateTimeParseException ex) {
                //no valid date according to the current format, continue if possible
            } catch (DateTimeException ex) {
                ex.printStackTrace();
            }
        }
        throw new DateTimeParseException("Invalid date string. Supported format patterns are: yyyy-MM-dd'T'HH:mm:ss'Z', yyyy, yyyy-MM-dd and yyyy-MM", p.getText(), 0);
    }
}
