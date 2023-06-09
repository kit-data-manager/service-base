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
package edu.kit.datamanager.security.filter;

import edu.kit.datamanager.exceptions.InvalidAuthenticationException;
import java.util.Collection;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;

/**
 *
 * @author jejkal
 */
public class JwtUserToken extends JwtAuthenticationToken {

    private static final Logger LOGGER = LoggerFactory.getLogger(JwtUserToken.class);

    private String firstname = null;
    private String lastname = null;
    private String email = null;

    public JwtUserToken(String token, Collection<? extends GrantedAuthority> authorities) {
        super(token, authorities);
    }

    @Override
    public String[] getSupportedClaims() {
        return new String[]{USERNAME_CLAIM, FIRSTNAME_CLAIM, LASTNAME_CLAIM, EMAIL_CLAIM, GROUPS_CLAIM};
    }

    @Override
    public Class getClassForClaim(String claim) {
        if (GROUPS_CLAIM.equals(claim)) {
            return List.class;
        }
        return String.class;
    }

    @Override
    public void setValueFromClaim(String claim, Object value) {
        switch (claim) {
            case USERNAME_CLAIM:
                setPrincipalName((String) value);
                break;
            case FIRSTNAME_CLAIM:
                firstname = (String) value;
                break;
            case LASTNAME_CLAIM:
                lastname = (String) value;
                break;
            case EMAIL_CLAIM:
                email = (String) value;
                break;
            case GROUPS_CLAIM:
                setGroups((List<String>) value);
                break;
            default:
                LOGGER.warn("Invalid claim {} with value {} received. Claim will be ignored.", claim, value);
        }
    }

    @Override
    public void validate() throws InvalidAuthenticationException {
        //do nothing, there are no mandatory attributes
    }

    public String getEmail() {
        return email;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }

    @Override
    public TOKEN_TYPE getTokenType() {
        return TOKEN_TYPE.USER;
    }
}
