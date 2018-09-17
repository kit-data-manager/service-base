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

import edu.kit.datamanager.util.AuthenticationHelper;
import java.util.ArrayList;
import java.util.Collection;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import static org.powermock.api.mockito.PowerMockito.when;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

/**
 *
 * @author jejkal
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(AuthenticationHelper.class)
@Ignore
public class AuthenticationHelperTest{

  @Before
  public void setup(){
    PowerMockito.mockStatic(AuthenticationHelper.class);
    when(AuthenticationHelper.getAuthentication()).thenReturn(new Authentication(){
      @Override
      public Collection<? extends GrantedAuthority> getAuthorities(){
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("admin"));
        return authorities;
      }

      @Override
      public Object getCredentials(){
        return "none";
      }

      @Override
      public Object getDetails(){
        return null;
      }

      @Override
      public Object getPrincipal(){
        return "tester";
      }

      @Override
      public boolean isAuthenticated(){
        return true;
      }

      @Override
      public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException{

      }

      @Override
      public String getName(){
        return "tester";
      }
    });

    when(AuthenticationHelper.hasAuthority("admin")).thenCallRealMethod();
  }

  @Test
  public void testHasAuthority(){
    Assert.assertTrue(AuthenticationHelper.hasAuthority("admin"));
    PowerMockito.verifyStatic(AuthenticationHelper.class);
  }

  @Test
  public void testGetAuthentication(){

    System.out.println(AuthenticationHelper.getAuthentication());
    Assert.assertNotNull(AuthenticationHelper.getAuthentication());
    PowerMockito.verifyStatic(AuthenticationHelper.class);
  }
}
