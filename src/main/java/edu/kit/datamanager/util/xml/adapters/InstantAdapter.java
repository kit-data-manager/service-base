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
package edu.kit.datamanager.util.xml.adapters;

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import jakarta.xml.bind.annotation.adapters.XmlAdapter;

/**
 * XmlAdapter implementation for Instant attributes.
 *
 * @author jejkal
 */
public class InstantAdapter extends XmlAdapter<String, Instant> {

    public Instant unmarshal(String v) throws Exception {
        return (Instant) DateTimeFormatter.ISO_DATE_TIME.withZone(ZoneOffset.UTC).parse(v);
    }

    public String marshal(Instant v) throws Exception {
        return DateTimeFormatter.ISO_DATE_TIME.withZone(ZoneOffset.UTC).format(v);
    }

}
