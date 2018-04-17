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
package edu.kit.datamanager.entities;

import edu.kit.datamanager.entities.interfaces.IBaseEntity;
import edu.kit.datamanager.util.CustomReflectionToStringBuilder;
import java.io.Serializable;

/**
 *
 * @author jejkal
 */
public abstract class BaseEntity implements IBaseEntity, Serializable {

    private static final long serialVersionUID = 1L;

    @Override
    public abstract int hashCode();

    @Override
    public abstract boolean equals(Object obj);

    @Override
    public String toString() {
        return new CustomReflectionToStringBuilder(this).toString();
    }

}
