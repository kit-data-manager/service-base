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

import edu.kit.datamanager.security.filter.JwtAuthenticationToken;
import java.util.Arrays;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

/**
 *
 * @author jejkal
 */
public class JwtAuthenticationTokenTest{

  @Test
  public void testEmptyToken(){
    JwtAuthenticationToken token = new JwtAuthenticationToken("test123");
    Assert.assertTrue(token.getAuthorities().isEmpty());
    Assert.assertEquals("test123", token.getToken());
    Assert.assertNull(token.getPrincipal());
    Assert.assertEquals(JwtAuthenticationToken.NOT_AVAILABLE, token.getCredentials());
    Assert.assertFalse(token.isAuthenticated());
  }

  @Test
  public void testFullToken(){
    JwtAuthenticationToken token = JwtAuthenticationToken.createUserToken(Arrays.asList(new SimpleGrantedAuthority("admin")), "tester", "test", "user", "test@mail.org", "USERS", "test123");
    Assert.assertEquals(1, token.getAuthorities().size());
    Assert.assertEquals("tester", token.getPrincipal());
    Assert.assertEquals("test", token.getFirstname());
    Assert.assertEquals("user", token.getLastname());
    Assert.assertEquals("test@mail.org", token.getEmail());
    Assert.assertEquals("USERS", token.getGroupId());
    Assert.assertEquals("admin", ((SimpleGrantedAuthority) token.getAuthorities().toArray()[0]).getAuthority());
    Assert.assertEquals("test123", token.getToken());
    Assert.assertTrue(token.isAuthenticated());
  }

  @Test
  public void testServiceToken(){
    JwtAuthenticationToken token = JwtAuthenticationToken.createServiceToken(Arrays.asList(new SimpleGrantedAuthority("admin")), "servicename", "USERS", "test123");
    Assert.assertEquals("servicename", token.getPrincipal());
    Assert.assertNull(token.getFirstname());
    Assert.assertNull(token.getLastname());
    Assert.assertNull(token.getEmail());
    Assert.assertTrue(token.isAuthenticated());
  }

}
