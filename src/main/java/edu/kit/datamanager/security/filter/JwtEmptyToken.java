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

/**
 *
 * @author jejkal
 */
public class JwtEmptyToken extends JwtAuthenticationToken{

  public JwtEmptyToken(String token){
    super(token);
  }

  @Override
  public String[] getSupportedClaims(){
    return new String[0];
  }

  @Override
  public void setValueFromClaim(String claim, Object value){
  }

  @Override
  public Class getClassForClaim(String claim){
    return Object.class;
  }

  @Override
  public void validate() throws InvalidAuthenticationException{
  }

  @Override
  public TOKEN_TYPE getTokenType(){
    return TOKEN_TYPE.USER;
  }

}
