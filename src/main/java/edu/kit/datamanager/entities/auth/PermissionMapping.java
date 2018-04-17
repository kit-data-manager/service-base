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
package edu.kit.datamanager.entities.auth;

import edu.kit.datamanager.entities.BaseEntity;
import edu.kit.datamanager.entities.Permission;
import edu.kit.datamanager.entities.dc40.exceptions.InvalidResourceException;
import java.util.Objects;

/**
 *
 * @author jejkal
 */
public class PermissionMapping extends BaseEntity {

    String mappingId = null;
    Permission permission = Permission.NONE;

    public PermissionMapping() {
    }

    public PermissionMapping(String mappingId, Permission permission) {
        this.mappingId = mappingId;
        this.permission = permission;
    }

    @Override
    public String getId() {
        return mappingId;
    }

    public String getMappingId() {
        return mappingId;
    }

    public void setMappingId(String mappingId) {
        this.mappingId = mappingId;
    }

    public Permission getPermission() {
        return permission;
    }

    public void setPermission(Permission permission) {
        this.permission = permission;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final PermissionMapping other = (PermissionMapping) obj;
        if (!Objects.equals(this.mappingId, other.mappingId)) {
            return false;
        }
        return this.permission == other.permission;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 43 * hash + Objects.hashCode(this.mappingId);
        hash = 43 * hash + Objects.hashCode(this.permission);
        return hash;
    }

    @Override
    public void validate() throws InvalidResourceException {

    }
}
