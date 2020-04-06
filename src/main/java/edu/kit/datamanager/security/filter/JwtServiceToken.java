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

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.kit.datamanager.exceptions.InvalidAuthenticationException;
import java.io.IOException;
import java.util.Collection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;

/**
 *
 * @author jejkal
 */
public class JwtServiceToken extends JwtAuthenticationToken{

  private final static Logger LOGGER = LoggerFactory.getLogger(JwtServiceToken.class);

  public final static String SELF_SERVICE_NAME = "SELF";

  private String[] sources;

  public JwtServiceToken(String token, Collection<? extends GrantedAuthority> authorities){
    super(token, authorities);
  }

  @Override
  public Class getClassForClaim(String claim){
    return String.class;
  }

  @Override
  public String[] getSupportedClaims(){
    return new String[]{"servicename", "sources", "groupid"};
  }

  @Override
  public void setValueFromClaim(String claim, Object value){
    switch(claim){
      case "servicename":
        setPrincipalName((String) value);
        break;
      case "sources":
        parseSources((String) value);
        break;
      case "groupid":
        setGroupId((String) value);
        break;
      default:
        LOGGER.warn("Invalid claim {} with value {} received. Claim will be ignored.", claim, value);
    }
  }

  private void parseSources(String value){
    if(value == null){
      //no sources found
      return;
    }
    ObjectMapper mapper = new ObjectMapper();
    try{
      sources = mapper.readValue(value, String[].class);
    } catch(IOException ex){
      throw new InvalidAuthenticationException("Failed to read sources from claim value " + value + ".");
    }
  }

  @Override
  public void validate() throws InvalidAuthenticationException{
    if(sources == null && !SELF_SERVICE_NAME.equals(getPrincipal())){
      LOGGER.warn("No sources provided in service token for service '" + getPrincipal() + ". Possible security risk!");
    }
  }

  public String[] getSources(){
    return sources;
  }

  @Override
  public TOKEN_TYPE getTokenType(){
    return TOKEN_TYPE.SERVICE;
  }

}
