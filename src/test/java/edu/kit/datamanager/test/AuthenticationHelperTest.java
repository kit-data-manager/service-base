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
package edu.kit.datamanager.test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.kit.datamanager.entities.PERMISSION;
import edu.kit.datamanager.entities.RepoServiceRole;
import edu.kit.datamanager.entities.RepoUserRole;
import edu.kit.datamanager.security.filter.JwtAuthenticationToken;
import edu.kit.datamanager.security.filter.ScopedPermission;
import edu.kit.datamanager.util.AuthenticationHelper;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.mockito.ArgumentMatchers.any;
import org.powermock.api.mockito.PowerMockito;
import static org.powermock.api.mockito.PowerMockito.when;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

/**
 *
 * @author jejkal
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(AuthenticationHelper.class)
public class AuthenticationHelperTest{

  @Test
  public void testJwtUserToken(){
    mockJwtUserAuthentication();
    when(AuthenticationHelper.hasAuthority(RepoUserRole.ADMINISTRATOR.getValue())).thenCallRealMethod();
    when(AuthenticationHelper.getFirstname()).thenCallRealMethod();
    when(AuthenticationHelper.getLastname()).thenCallRealMethod();
    when(AuthenticationHelper.getPrincipal()).thenCallRealMethod();
    when(AuthenticationHelper.getAuthorizationIdentities()).thenCallRealMethod();
    when(AuthenticationHelper.getScopedPermission(any(String.class), any(String.class))).thenCallRealMethod();
    Assert.assertTrue(AuthenticationHelper.hasAuthority(RepoUserRole.ADMINISTRATOR.getValue()));
    Assert.assertEquals("test", AuthenticationHelper.getFirstname());
    Assert.assertEquals("user", AuthenticationHelper.getLastname());
    Assert.assertEquals("tester", AuthenticationHelper.getAuthorizationIdentities().get(0));
    Assert.assertEquals("USERS", AuthenticationHelper.getAuthorizationIdentities().get(1));
    Assert.assertEquals(PERMISSION.NONE, AuthenticationHelper.getScopedPermission(String.class.getSimpleName(), "1"));
    PowerMockito.verifyStatic(AuthenticationHelper.class);
  }

  @Test
  public void testJwtServiceToken() throws JsonProcessingException{
    mockJwtServiceAuthentication();
    when(AuthenticationHelper.getPrincipal()).thenCallRealMethod();
    when(AuthenticationHelper.getAuthorizationIdentities()).thenCallRealMethod();
    when(AuthenticationHelper.hasAuthority(any(String.class))).thenCallRealMethod();
    when(AuthenticationHelper.hasIdentity(any(String.class))).thenCallRealMethod();
    Assert.assertEquals("metadata_extractor", AuthenticationHelper.getPrincipal());
    Assert.assertTrue(AuthenticationHelper.hasIdentity("metadata_extractor"));
    Assert.assertTrue(AuthenticationHelper.hasAuthority(RepoServiceRole.SERVICE_READ.getValue()));
    PowerMockito.verifyStatic(AuthenticationHelper.class);
  }

  @Test
  public void testJwtTemporaryToken() throws JsonProcessingException{
    mockJwtTemporaryAuthentication();
    when(AuthenticationHelper.getPrincipal()).thenCallRealMethod();
    when(AuthenticationHelper.getAuthorizationIdentities()).thenCallRealMethod();
    when(AuthenticationHelper.getScopedPermission("String", "1")).thenCallRealMethod();
    when(AuthenticationHelper.hasIdentity(any(String.class))).thenCallRealMethod();
    Assert.assertEquals("test@mail.org", AuthenticationHelper.getPrincipal());
    Assert.assertTrue(AuthenticationHelper.hasIdentity("test@mail.org"));
    Assert.assertEquals(PERMISSION.READ, AuthenticationHelper.getScopedPermission(String.class.getSimpleName(), "1"));
    PowerMockito.verifyStatic(AuthenticationHelper.class);
  }

  private void mockJwtUserAuthentication(){
    Map<String, Object> claimMap = new HashMap<>();
    claimMap.put("tokenType", JwtAuthenticationToken.TOKEN_TYPE.USER.toString());
    claimMap.put("username", "tester");
    claimMap.put("firstname", "test");
    claimMap.put("lastname", "user");
    claimMap.put("email", "test@mail.org");
    claimMap.put("groupid", "USERS");
    claimMap.put("roles", Arrays.asList(RepoUserRole.ADMINISTRATOR.getValue()));
    JwtAuthenticationToken userToken = JwtAuthenticationToken.factoryToken("test123", claimMap);
    PowerMockito.mockStatic(AuthenticationHelper.class);
    when(AuthenticationHelper.getAuthentication()).thenReturn(userToken);
  }

  private void mockJwtServiceAuthentication() throws JsonProcessingException{
    Map<String, Object> claimMap = new HashMap<>();
    claimMap.put("tokenType", JwtAuthenticationToken.TOKEN_TYPE.SERVICE.toString());
    claimMap.put("servicename", "metadata_extractor");
    claimMap.put("roles", Arrays.asList(RepoServiceRole.SERVICE_READ.getValue()));
    claimMap.put("groupid", "USERS");
    JwtAuthenticationToken serviceToken = JwtAuthenticationToken.factoryToken("test123", claimMap);
    PowerMockito.mockStatic(AuthenticationHelper.class);
    when(AuthenticationHelper.getAuthentication()).thenReturn(serviceToken);
  }

  private void mockJwtTemporaryAuthentication() throws JsonProcessingException{
    Map<String, Object> claimMap = new HashMap<>();
    claimMap.put("tokenType", JwtAuthenticationToken.TOKEN_TYPE.TEMPORARY.toString());
    claimMap.put("principalname", "test@mail.org");
    ScopedPermission[] perms = new ScopedPermission[]{ScopedPermission.factoryScopedPermission("String", "1", PERMISSION.READ)};
    claimMap.put("permissions", new ObjectMapper().writeValueAsString(perms));
    JwtAuthenticationToken temporaryToken = JwtAuthenticationToken.factoryToken("test123", claimMap);
    PowerMockito.mockStatic(AuthenticationHelper.class);
    when(AuthenticationHelper.getAuthentication()).thenReturn(temporaryToken);
  }

}
