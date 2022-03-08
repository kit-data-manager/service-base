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
     * The HTTP connects timeout, in milliseconds, zero for infinite. Must not
     * be negative. The HTTP read timeout, in milliseconds, zero for infinite.
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
