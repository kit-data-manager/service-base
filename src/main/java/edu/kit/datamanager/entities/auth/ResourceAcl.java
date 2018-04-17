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

import com.fasterxml.jackson.annotation.JsonIgnore;
import edu.kit.datamanager.entities.BaseEntity;
import edu.kit.datamanager.entities.Permission;
import edu.kit.datamanager.entities.dc40.exceptions.InvalidResourceException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import edu.kit.datamanager.entities.interfaces.IBaseEntity;
import java.util.Collections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author jejkal
 */
public class ResourceAcl extends BaseEntity implements IBaseEntity {

    private static final Logger LOGGER = LoggerFactory.getLogger(ResourceAcl.class);

    @JsonIgnore
    private String _key;

    //permissions inspired by https://www.w3.org/wiki/WebAccessControl
    private String resourceId;

    /**
     * List of groupAcl mappings. The list approach was chosen (in favor to a
     * map) due to better JSON accessibility. If this causes performance issues
     * while obtaining single permissions, a local caching mechanism could be
     * introduced.
     */
    private List<PermissionMapping> groupAcl = new ArrayList<>();
    /**
     * List of groupAcl mappings.
     */
    private List<PermissionMapping> userAcl = new ArrayList<>();

    /**
     * Fixity flag stating that the access control list is modifyable (fixed =
     * false) or not (fixed = true).
     */
    private boolean fixed = false;

    public ResourceAcl() {
    }

    /**
     * Create an ACL that allows reading the associated resource for all users.
     * Basically, the ACL contains just a user mapping with mappingId '.*' and
     * permission Permission.READ.
     *
     * The returned ACL is not fixed and can be updated by adding group mappings
     * or user mappings overwriting the default permissions.
     *
     * @param resourceId The resource identifier this ACL will be related to.
     *
     * @return The resource ACL.
     */
    public static ResourceAcl createPublicReadAcl(String resourceId) {
        ResourceAcl result = new ResourceAcl();
        result.setResourceId(resourceId);
        List<PermissionMapping> userAcl = new ArrayList<>();
        userAcl.add(new PermissionMapping(".*", Permission.READ));
        result.userAcl = userAcl;
        return result;
    }

    /**
     * Create an ACL that denys all access to the associated resource for all
     * users. Basically, the ACL contains just a user mapping with mappingId
     * '.*' and permission Permission.NONE. Furthermore, the ACL is fixed and
     * cannot be modified.
     *
     * @param resourceId The resource identifier this ACL will be related to.
     *
     * @return The resource ACL.
     */
    public static ResourceAcl createNoAccessAcl(String resourceId) {
        ResourceAcl result = new ResourceAcl();
        result.setResourceId(resourceId);
        List<PermissionMapping> userAcl = new ArrayList<>();
        userAcl.add(new PermissionMapping(".*", Permission.NONE));
        result.userAcl = userAcl;
        result.fixed = true;
        return result;
    }

    public String getResourceId() {
        return resourceId;
    }

    public void setResourceId(String resourceId) {
        if (this.resourceId == null) {
            this.resourceId = resourceId;
            _key = resourceId;
        }
    }

    public List<PermissionMapping> getGroupAcl() {
        if (groupAcl == null) {
            groupAcl = new ArrayList<>();
        }
        if (fixed) {
            return (List<PermissionMapping>) Collections.unmodifiableList(groupAcl);
        }
        return groupAcl;
    }

    public void setGroupAcl(List<PermissionMapping> groupAcl) {
        if (fixed && this.groupAcl != null && this.groupAcl.isEmpty()) {
            LOGGER.warn("Access control list for resource {} is fixed. Modification not allowed.");
            return;
        }
        this.groupAcl = groupAcl;
    }

    public List<PermissionMapping> getUserAcl() {
        if (userAcl == null) {
            userAcl = new ArrayList<>();
        }
        if (fixed) {
            return (List<PermissionMapping>) Collections.unmodifiableList(userAcl);
        }
        return userAcl;
    }

    public void setUserAcl(List<PermissionMapping> userAcl) {
        if (fixed && this.userAcl != null && this.userAcl.isEmpty()) {
            LOGGER.warn("Access control list for resource {} is fixed. Modification not allowed.");
            return;
        }
        this.userAcl = userAcl;
    }

    public void setFixed(boolean fixed) {
        this.fixed = fixed;
    }

    public boolean isFixed() {
        return fixed;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 71 * hash + Objects.hashCode(this.resourceId);
        hash = 71 * hash + Objects.hashCode(this.fixed);

        if (this.groupAcl != null && !this.groupAcl.isEmpty()) {
            for (PermissionMapping mapping : this.groupAcl) {
                hash = 71 * hash + mapping.hashCode();
            }
        }
        if (this.userAcl != null && !this.userAcl.isEmpty()) {
            for (PermissionMapping mapping : this.userAcl) {
                hash = 71 * hash + mapping.hashCode();
            }
        }

        return hash;
    }

    @Override
    public boolean equals(Object obj
    ) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ResourceAcl other = (ResourceAcl) obj;
        if (!Objects.equals(this.resourceId, other.resourceId)) {
            return false;
        }
        if (!Objects.equals(this.fixed, other.fixed)) {
            return false;
        }
        if (!Objects.equals(this.groupAcl, other.groupAcl)) {
            return false;
        }
        return Objects.equals(this.userAcl, other.userAcl);
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }

    @Override
    public String getId() {
        return _key;
    }

    @Override
    public void validate() throws InvalidResourceException {
        if (getResourceId() == null) {
            throw new InvalidResourceException(InvalidResourceException.ERROR_TYPE.NO_IDENTIFIER);
        }
        if (getUserAcl().isEmpty() && getGroupAcl().isEmpty()) {
            throw new InvalidResourceException(InvalidResourceException.ERROR_TYPE.NO_CONTENT);
        }
    }

}
