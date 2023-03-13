/*
 * Copyright 2021 Karlsruhe Institute of Technology.
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
 *
 * This original version of this code is available at 
 *
 *  https://github.com/akoserwal/keycloak-jwt 
 * 
 * and was modified according to our requirements. 
 */
package edu.kit.datamanager.security.filter;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * ConfigurationProperties holder for Keycloak-based JWT support.
 *
 * There are several configuration properties, where only two of them are
 * mandatory:
 *
 * <ul>
 * <li>jwkUrl - The URL to the Json Web Key file provided by Keycloak containing
 * all public keys used for signing JWTs. Typically, this URL looks as follows:
 * https://keycloak-host:8443/realms/your-realm/protocol/openid-connect/certs
 * (mandatory)</li>
 * <li>resource - The identifier of the configured Keycloak client.
 * (mandatory)</li>
 * <li>jwtClaim - JWT claim containing the unique username, e.g.,
 * preferred_username (default)</li>
 * <li>groupClaim - JWT claim containing a list of groupIds the user is member
 * of, e.g., groups (default)</li>
 * <li>connectTimeoutms - Connect timeout in ms (>= 0) for connecting the jwkUrl
 * (0 = infinite (default))</li>
 * <li>readTimeoutms - Read timeout in ms (>= 0) for reading from jwkUrl (0 =
 * infinite (default))</li>
 * <li>sizeLimit - The read size limit in bytes (>= 0) for reading from jwkUrl
 * (0 = infinite (default))</li>
 * </ul>
 *
 * @author jejkal
 */
@ConfigurationProperties("keycloakjwt")
public class KeycloakJwtProperties {

    /**
     * keycloak certs jwkUrl
     */
    private String jwkUrl;

    /**
     * Client ID configured in keycloak
     */
    private String resource;

    /**
     * defined in keycloak mapper for client id: preferred_username or username
     */
    private String jwtClaim;

    /**
     * defined in keycloak mapper for group ids: default is groups
     */
    private String groupClaim;

    /**
     * The HTTP connects timeout, in milliseconds, zero for infinite. Must not
     * be negative.
     */
    private int connectTimeoutms = 0;

    /**
     * The HTTP read timeout, in milliseconds, zero for infinite. Must not be
     * negative.
     */
    private int readTimeoutms = 0;
    /**
     * The HTTP entity size limit, in bytes, zero for infinite. Must not be
     * negative.
     */
    private int sizeLimit = 0;

    public String getJwkUrl() {
        return jwkUrl;
    }

    public void setJwkUrl(String jwkUrl) {
        this.jwkUrl = jwkUrl;
    }

    public String getResource() {
        return resource;
    }

    public void setResource(String resource) {
        this.resource = resource;
    }

    public String getGroupClaim() {
        return groupClaim;
    }

    public void setGroupClaim(String groupClaim) {
        this.groupClaim = groupClaim;
    }

    public String getJwtClaim() {
        return jwtClaim;
    }

    public void setJwtClaim(String jwtClaim) {
        this.jwtClaim = jwtClaim;
    }

    public int getConnectTimeoutms() {
        return connectTimeoutms;
    }

    public void setConnectTimeoutms(int connectTimeoutms) {
        this.connectTimeoutms = connectTimeoutms;
    }

    public int getReadTimeoutms() {
        return readTimeoutms;
    }

    public void setReadTimeoutms(int readTimeoutms) {
        this.readTimeoutms = readTimeoutms;
    }

    public int getSizeLimit() {
        return sizeLimit;
    }

    public void setSizeLimit(int sizeLimit) {
        this.sizeLimit = sizeLimit;
    }

}
