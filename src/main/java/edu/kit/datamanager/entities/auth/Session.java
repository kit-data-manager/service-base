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
import edu.kit.datamanager.Constants;
import edu.kit.datamanager.entities.BaseEntity;
import edu.kit.datamanager.entities.dc40.exceptions.InvalidResourceException;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.Objects;
import java.util.UUID;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import edu.kit.datamanager.entities.interfaces.IBaseEntity;
import java.util.Date;

/**
 *
 * @author jejkal
 */
@ApiModel(description = "A session resource.")
public class Session extends BaseEntity implements IBaseEntity {

    private static final Logger LOGGER = LoggerFactory.getLogger(Session.class);

    @JsonIgnore
    public static Session NO_SESSION = Session.createSession(Constants.ANONYMOUS_USER_ID, Constants.ANONYMOUS_GROUP_ID);
    @JsonIgnore
    private String _key;
    @ApiModelProperty(dataType = "String", required = false)
    private String sessionId;
    @ApiModelProperty(value = "Doe, John", dataType = "String", required = true)
    private String userId;
    @ApiModelProperty(value = "SampleGroup", dataType = "String", required = true)
    private String groupId;
    @ApiModelProperty(dataType = "Long", required = false)
    private long expiresAt;

    /**
     * Create a new session object for the provided userId and groupId. The
     * sessionId will be auto-generated, the expiration time will be set to one
     * hour in future.
     *
     * @param userId The session userId.
     * @param groupId The session groupId.
     *
     * @return The created session.
     */
    public static Session createSession(String userId, String groupId) {
        Session result = new Session();
        result.setSessionId(UUID.randomUUID().toString());
        result.setUserId(userId);
        result.setGroupId(groupId);
        result.setExpiresAt(System.currentTimeMillis() + DateUtils.MILLIS_PER_HOUR);
        return result;
    }

    public String getSessionId() {
        return sessionId;

    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
        _key = sessionId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public void setExpiresAtDate(Date date) {
        expiresAt = date.getTime();
    }

    public Date getExpiresAtDate() {
        return new Date(expiresAt);
    }

    public long getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(long expiresAt) {
        this.expiresAt = expiresAt;
    }

//    /**
//     * Get the security context for this session. The context contains userId
//     * and groupId defined by the session entity. There are no further checks at
//     * this point, e.g. if the according user or group are still valid/enabled.
//     *
//     * @return The SecurityContext for this session.
//     */
//    @JsonIgnore
//    public SecurityContext getSecurityContextForSession() {
//        return new SecurityContext(userId, groupId);
//    }
//
//    /**
//     * Get the user principal assiciated with this session.
//     *
//     * @return The UserPrincipal for this session.
//     */
//    @JsonIgnore
//    public UserPrincipal getUserPrincipal() {
//        return new UserPrincipal(userId, groupId);
//    }

//    /**
//     * Check if session user and group are valid. For checking this, the
//     * configured services of type IUserServiceAdapter and IGroupServiceAdapter
//     * are used to obtain user and group entities and to check whether they are
//     * disabled or not. This should be checked using {@link #isValid() } before
//     * calling the method.
//     */
//    @JsonIgnore
//    public final void validateSession() {
//        LOGGER.trace("Validating session.");
//
//        boolean valid = getUserId() != null && getGroupId() != null;
//        if (valid) {
//            User sessionUser = null;
//            try {
//                LOGGER.trace("Checking session user with identifier '{}'.", getUserId());
//                //internally we should have configured the local service, thus no configuration takes place for now.
//                sessionUser = ServiceUtil.getService(IUserServiceAdapter.class).read(getUserId());
//            } catch (ServiceException ex) {
//                LOGGER.error("Failed to obtain session user for identifier '" + getUserId() + "'. Returning HTTP INTERNAL SERVER ERROR (500).", ex);
//                ex.throwWebApplicationException();
//            }
//
//            if (sessionUser != null) {
//                if (sessionUser.isDisabled()) {
//                    LOGGER.error("Failed to validate session due to invalid session user. User " + sessionUser + " is  disabled.");
//                    throw new WebApplicationException(Response.status(Response.Status.FORBIDDEN).entity("Unable to create session for provided user.").type(MediaType.TEXT_PLAIN).build());
//                } else {
//                    Group sessionGroup = null;
//                    try {
//                        LOGGER.trace("Checking session group with identifier '{}'.", getGroupId());
//                        //internally we should have configured the local service, thus no configuration takes place for now.
//                        sessionGroup = ServiceUtil.getService(IGroupServiceAdapter.class).read(getGroupId());
//                    } catch (ServiceException ex) {
//                        LOGGER.error("Failed to obtain session group for identifier '" + getGroupId() + "'. Returning HTTP INTERNAL SERVER ERROR (500).", ex);
//                        ex.throwWebApplicationException();
//                    }
//                    LOGGER.trace("Checking group and memberships status.");
//                    valid = sessionGroup != null && !sessionGroup.isDisabled() && sessionGroup.getMemberIdentifiers().contains(sessionUser.getIdentifier());
//                    if (!valid) {
//                        LOGGER.error("Failed to validate session due to invalid group. Group " + sessionGroup + " is either invalid or user " + sessionUser.getIdentifier() + " is no member.");
//                        throw new WebApplicationException(Response.status(Response.Status.FORBIDDEN).entity("Unable to create session for provided group.").type(MediaType.TEXT_PLAIN).build());
//                    }
//                }
//            } else {
//                throw new WebApplicationException(Response.status(Response.Status.BAD_REQUEST).entity("Received 'null' session.").type(MediaType.TEXT_PLAIN).build());
//            }
//        } else {
//            LOGGER.trace("Session user or group are null.");
//            throw new WebApplicationException(Response.status(Response.Status.BAD_REQUEST).entity("Session user and session group must not be empty.").type(MediaType.TEXT_PLAIN).build());
//        }
//
//        LOGGER.trace("Session is '{}'.", ((valid) ? "VALID" : "INVALID"));
//    }

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
        final Session other = (Session) obj;
        if (this.expiresAt != other.expiresAt) {
            return false;
        }
        if (!Objects.equals(this.sessionId, other.sessionId)) {
            return false;
        }
        if (!Objects.equals(this.userId, other.userId)) {
            return false;
        }
        return Objects.equals(this.groupId, other.groupId);
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 97 * hash + Objects.hashCode(this.sessionId);
        hash = 97 * hash + Objects.hashCode(this.userId);
        hash = 97 * hash + Objects.hashCode(this.groupId);
        hash = 97 * hash + (int) (this.expiresAt ^ (this.expiresAt >>> 32));
        return hash;
    }

    @Override
    public String getId() {
        return _key;
    }

    @Override
    public void validate() throws InvalidResourceException {
    }

}
