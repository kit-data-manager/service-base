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
import edu.kit.datamanager.entities.PERMISSION;
import edu.kit.datamanager.entities.RepoServiceRole;
import edu.kit.datamanager.entities.RepoUserRole;
import edu.kit.datamanager.security.filter.JwtAuthenticationToken;
import edu.kit.datamanager.security.filter.ScopedPermission;
import edu.kit.datamanager.util.AuthenticationHelper;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.mockito.ArgumentMatchers.any;
import org.powermock.api.mockito.PowerMockito;
import static org.powermock.api.mockito.PowerMockito.when;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

/**
 *
 * @author jejkal
 */
@Ignore
@RunWith(PowerMockRunner.class)
@PrepareForTest(AuthenticationHelper.class)
@PowerMockIgnore({"javax.crypto.*" })
public class AuthenticationHelperTest{

  @Test
  public void testJwtUserToken() throws JsonProcessingException{
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

  private void mockJwtUserAuthentication() throws JsonProcessingException{
    JwtAuthenticationToken userToken = edu.kit.datamanager.util.JwtBuilder.
            createUserToken("tester", RepoUserRole.ADMINISTRATOR).
            addSimpleClaim("firstname", "test").
            addSimpleClaim("lastname", "user").
            addSimpleClaim("email", "test@mail.org").
            addSimpleClaim("groupid", "USERS").
            getJwtAuthenticationToken("test123");
    PowerMockito.mockStatic(AuthenticationHelper.class);
    when(AuthenticationHelper.getAuthentication()).thenReturn(userToken);
  }

  private void mockJwtServiceAuthentication() throws JsonProcessingException{
    JwtAuthenticationToken serviceToken = edu.kit.datamanager.util.JwtBuilder.
            createServiceToken("metadata_extractor", RepoServiceRole.SERVICE_READ).
            addSimpleClaim("groupid", "USERS").
            getJwtAuthenticationToken("test123");
    PowerMockito.mockStatic(AuthenticationHelper.class);
    when(AuthenticationHelper.getAuthentication()).thenReturn(serviceToken);
  }

  private void mockJwtTemporaryAuthentication() throws JsonProcessingException{
    ScopedPermission[] perms = new ScopedPermission[]{ScopedPermission.factoryScopedPermission("String", "1", PERMISSION.READ)};

    JwtAuthenticationToken temporaryToken = edu.kit.datamanager.util.JwtBuilder.createTemporaryToken("test@mail.org", perms).
            getJwtAuthenticationToken("test123");
    PowerMockito.mockStatic(AuthenticationHelper.class);
    when(AuthenticationHelper.getAuthentication()).thenReturn(temporaryToken);
  }

}
