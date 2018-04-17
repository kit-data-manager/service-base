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

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * Custom reflection to string builder that can be used to change the formatting
 * of certain fields, e.g. expiration timestamps from long into a proper, human
 * readable date representation.
 *
 * @author jejkal
 */
public class CustomReflectionToStringBuilder extends ReflectionToStringBuilder {

    public CustomReflectionToStringBuilder(Object object) {
        super(object, ToStringStyle.MULTI_LINE_STYLE);
    }

    @Override
    protected Object getValue(Field field) throws IllegalArgumentException, IllegalAccessException {
        if (field.getName().equals("expiresAt") && field.getType().isPrimitive() && field.getType().getName().equals("long")) {
            return new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").format(new Date(field.getLong(getObject())));
        }//more may follow here

        return super.getValue(field);
    }

}
