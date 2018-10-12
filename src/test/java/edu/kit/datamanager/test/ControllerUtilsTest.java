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

import edu.kit.datamanager.entities.RepoUserRole;
import edu.kit.datamanager.exceptions.AccessForbiddenException;
import edu.kit.datamanager.exceptions.EtagMismatchException;
import edu.kit.datamanager.exceptions.UnauthorizedAccessException;
import edu.kit.datamanager.util.AuthenticationHelper;
import edu.kit.datamanager.util.ControllerUtils;
import java.security.Principal;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import static org.powermock.api.mockito.PowerMockito.when;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.context.request.WebRequest;

/**
 *
 * @author jejkal
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(AuthenticationHelper.class)
@PowerMockIgnore({"javax.crypto.*"})
public class ControllerUtilsTest{

  @Test
  public void testCheckPaginationInformation(){
    PageRequest request = PageRequest.of(0, 10, Sort.unsorted());
    request = ControllerUtils.checkPaginationInformation(request);

    Assert.assertEquals(0, request.getPageNumber());
    Assert.assertEquals(10, request.getPageSize());
    Assert.assertEquals(Sort.unsorted(), request.getSort());
  }

  @Test
  public void testCheckPaginationInformationWithTooLargePage(){
    PageRequest request = PageRequest.of(0, 101, Sort.unsorted());
    request = ControllerUtils.checkPaginationInformation(request);

    Assert.assertEquals(0, request.getPageNumber());
    Assert.assertEquals(100, request.getPageSize());
    Assert.assertEquals(Sort.unsorted(), request.getSort());
  }

  @Test
  public void testCheckPaginationInformationWithMaxPageSize(){
    PageRequest request = PageRequest.of(0, 100, Sort.unsorted());
    request = ControllerUtils.checkPaginationInformation(request);

    Assert.assertEquals(0, request.getPageNumber());
    Assert.assertEquals(100, request.getPageSize());
    Assert.assertEquals(Sort.unsorted(), request.getSort());
  }

  @Test
  public void testCheckPaginationInformationWithAlmostMaxPageSize(){
    PageRequest request = PageRequest.of(0, 99, Sort.unsorted());
    request = ControllerUtils.checkPaginationInformation(request);

    Assert.assertEquals(0, request.getPageNumber());
    Assert.assertEquals(99, request.getPageSize());
    Assert.assertEquals(Sort.unsorted(), request.getSort());
  }

  @Test
  public void testCheckPaginationInformationWithTooLargePage1(){
    PageRequest request = PageRequest.of(0, 10, Sort.by("id"));
    request = ControllerUtils.checkPaginationInformation(request);

    Assert.assertEquals(0, request.getPageNumber());
    Assert.assertEquals(10, request.getPageSize());
    Assert.assertEquals(Sort.by("id"), request.getSort());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCheckPaginationInformationWithInvalidPageable(){
    PageRequest request = ControllerUtils.checkPaginationInformation(new Pageable(){
      @Override
      public int getPageNumber(){
        return -1;
      }

      @Override
      public int getPageSize(){
        return 10;
      }

      @Override
      public long getOffset(){
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
      }

      @Override
      public Sort getSort(){
        return Sort.unsorted();
      }

      @Override
      public Pageable next(){
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
      }

      @Override
      public Pageable previousOrFirst(){
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
      }

      @Override
      public Pageable first(){
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
      }

      @Override
      public boolean hasPrevious(){
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
      }
    });

    Assert.fail("Test should have already failed but created page request " + request);
  }

  @Test(expected = UnauthorizedAccessException.class)
  public void testCheckAnonymousTrue(){
    PowerMockito.mockStatic(AuthenticationHelper.class);
    when(AuthenticationHelper.isAnonymous()).thenReturn(Boolean.TRUE);
    ControllerUtils.checkAnonymousAccess();
  }

  @Test
  public void testCheckAnonymousFalse(){
    PowerMockito.mockStatic(AuthenticationHelper.class);
    when(AuthenticationHelper.isAnonymous()).thenReturn(Boolean.FALSE);
    ControllerUtils.checkAnonymousAccess();
  }

  @Test
  public void testCheckAdministratorAccessTrue(){
    PowerMockito.mockStatic(AuthenticationHelper.class);
    when(AuthenticationHelper.hasAuthority(RepoUserRole.ADMINISTRATOR.getValue())).thenReturn(Boolean.TRUE);
    ControllerUtils.checkAdministratorAccess();
  }

  @Test(expected = AccessForbiddenException.class)
  public void testCheckAdministratorAccessFalse(){
    PowerMockito.mockStatic(AuthenticationHelper.class);
    when(AuthenticationHelper.hasAuthority(RepoUserRole.ADMINISTRATOR.getValue())).thenReturn(Boolean.FALSE);
    ControllerUtils.checkAdministratorAccess();
  }

  @Test
  public void testCheckETagTrue(){
    ControllerUtils.checkEtag(createDummyWebRequest(true), () -> "1234");
  }

  @Test(expected = EtagMismatchException.class)
  public void testCheckETagFalse(){
    ControllerUtils.checkEtag(createDummyWebRequest(false), () -> "1234");
  }

  private WebRequest createDummyWebRequest(final boolean notModified){
    return new WebRequest(){
      @Override
      public String getHeader(String headerName){
        return null;
      }

      @Override
      public String[] getHeaderValues(String headerName){
        return null;
      }

      @Override
      public Iterator<String> getHeaderNames(){
        return null;
      }

      @Override
      public String getParameter(String paramName){
        return null;
      }

      @Override
      public String[] getParameterValues(String paramName){
        return null;
      }

      @Override
      public Iterator<String> getParameterNames(){
        return null;
      }

      @Override
      public Map<String, String[]> getParameterMap(){
        return null;
      }

      @Override
      public Locale getLocale(){
        return null;
      }

      @Override
      public String getContextPath(){
        return null;
      }

      @Override
      public String getRemoteUser(){
        return null;
      }

      @Override
      public Principal getUserPrincipal(){
        return null;
      }

      @Override
      public boolean isUserInRole(String role){
        return false;
      }

      @Override
      public boolean isSecure(){
        return false;
      }

      @Override
      public boolean checkNotModified(long lastModifiedTimestamp){
        return notModified;
      }

      @Override
      public boolean checkNotModified(String etag){
        return notModified;
      }

      @Override
      public boolean checkNotModified(String etag, long lastModifiedTimestamp){
        return notModified;
      }

      @Override
      public String getDescription(boolean includeClientInfo){
        return null;
      }

      @Override
      public Object getAttribute(String name, int scope){
        return null;
      }

      @Override
      public void setAttribute(String name, Object value, int scope){
      }

      @Override
      public void removeAttribute(String name, int scope){
      }

      @Override
      public String[] getAttributeNames(int scope){
        return null;
      }

      @Override
      public void registerDestructionCallback(String name, Runnable callback, int scope){
      }

      @Override
      public Object resolveReference(String key){
        return null;
      }

      @Override
      public String getSessionId(){
        return null;
      }

      @Override
      public Object getSessionMutex(){
        return null;
      }
    };
  }
}
