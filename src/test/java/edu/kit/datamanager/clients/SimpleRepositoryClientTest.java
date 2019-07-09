/*
 * Copyright 2019 Karlsruhe Institute of Technology.
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
package edu.kit.datamanager.clients;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.kit.datamanager.SpringTestConfig;
import edu.kit.datamanager.clients.MultiResourceAccessClient;
import edu.kit.datamanager.clients.SimpleRepositoryClient;
import edu.kit.datamanager.entities.repo.ContentInformation;
import edu.kit.datamanager.entities.repo.DataResource;
import java.io.File;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.junit.Assert;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import static org.springframework.test.web.client.ExpectedCount.once;
import org.springframework.test.web.client.MockRestServiceServer;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import org.springframework.test.web.client.response.MockRestResponseCreators;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.support.RestGatewaySupport;

/**
 *
 * @author jejkal
 */
@RunWith(SpringRunner.class)
//@PowerMockIgnore("javax.net.ssl.*")
//@PrepareForTest({RestTemplate.class})
@SpringBootTest
@ContextConfiguration(classes = SpringTestConfig.class)
public class SimpleRepositoryClientTest{

  @Autowired
  private RestTemplate restTemplate;
  private MockRestServiceServer mockServer;
  private ObjectMapper mapper = new ObjectMapper();

  @Before
  public void init(){
    RestGatewaySupport gateway = new RestGatewaySupport();
    gateway.setRestTemplate(restTemplate);
    mockServer = MockRestServiceServer.createServer(gateway);
  }

  @Test
  public void testGetResources() throws Exception{
    DataResource res = new DataResource();
    res.setId("test123");

    SimpleRepositoryClient client = SimpleRepositoryClient.createClient("http://localhost:8080/api/v1/dataresources/");
    MultiResourceAccessClient multiClient = client.page(0);
    multiClient.setRestTemplate(restTemplate);

    mockServer.expect(once(), requestTo("http://localhost:8080/api/v1/dataresources/?page=0&size=20"))
            .andRespond(withSuccess(mapper.writeValueAsString(new DataResource[]{res}), MediaType.APPLICATION_JSON));

    DataResource[] result = multiClient.getResources();

    mockServer.verify();
    assertEquals(1, result.length);
    assertEquals("test123", result[0].getId());
  }

  @Test
  public void testGetResourcesWith100Elements() throws Exception{
    DataResource res = new DataResource();
    res.setId("test123");

    SimpleRepositoryClient client = SimpleRepositoryClient.createClient("http://localhost:8080/api/v1/dataresources/");
    MultiResourceAccessClient multiClient = client.page(0).withElementsPerPage(100);
    multiClient.setRestTemplate(restTemplate);

    mockServer.expect(once(), requestTo("http://localhost:8080/api/v1/dataresources/?page=0&size=100"))
            .andRespond(withSuccess(mapper.writeValueAsString(new DataResource[]{res}), MediaType.APPLICATION_JSON));

    DataResource[] result = multiClient.getResources();
    System.out.println("testGetRootResourceOnce: " + result);

    mockServer.verify();
    assertEquals(1, result.length);
    assertEquals("test123", result[0].getId());
  }

  @Test
  public void testGetResourcesWithPage10() throws Exception{
    DataResource res = new DataResource();
    res.setId("test123");

    SimpleRepositoryClient client = SimpleRepositoryClient.createClient("http://localhost:8080/api/v1/dataresources/");
    MultiResourceAccessClient multiClient = client.page(10);
    multiClient.setRestTemplate(restTemplate);

    mockServer.expect(once(), requestTo("http://localhost:8080/api/v1/dataresources/?page=10&size=20"))
            .andRespond(withSuccess(mapper.writeValueAsString(new DataResource[]{res}), MediaType.APPLICATION_JSON));

    DataResource[] result = multiClient.getResources();
    System.out.println("testGetRootResourceOnce: " + result);

    mockServer.verify();
    assertEquals(1, result.length);
    assertEquals("test123", result[0].getId());
  }

  @Test
  public void testGetResource() throws Exception{
    DataResource res = new DataResource();
    res.setId("test123");

    SimpleRepositoryClient client = SimpleRepositoryClient.createClient("http://localhost:8080/api/v1/dataresources/");
    SingleResourceAccessClient singleClient = client.withResourceId("test123");
    singleClient.setRestTemplate(restTemplate);

    mockServer.expect(once(), requestTo("http://localhost:8080/api/v1/dataresources/test123"))
            .andRespond(withSuccess(mapper.writeValueAsString(res), MediaType.APPLICATION_JSON));

    DataResource result = singleClient.getResource();

    mockServer.verify();
    assertEquals("test123", result.getId());
  }

  @Test
  public void testGetResourceAsString() throws Exception{
    DataResource res = new DataResource();
    res.setId("test123");

    SimpleRepositoryClient client = SimpleRepositoryClient.createClient("http://localhost:8080/api/v1/dataresources/");
    SingleResourceAccessClient singleClient = client.withResourceId("test123");
    singleClient.setRestTemplate(restTemplate);

    mockServer.expect(once(), requestTo("http://localhost:8080/api/v1/dataresources/test123"))
            .andRespond(withSuccess(mapper.writeValueAsString(res), MediaType.APPLICATION_JSON));

    String result = singleClient.getResourceAsString();

    mockServer.verify();
    res = mapper.readValue(result, DataResource.class);
    Assert.assertNotNull(res);
  }

  @Test
  public void testListContentInformation() throws Exception{
    ContentInformation res = new ContentInformation();
    res.setRelativePath("myFile.txt");

    SimpleRepositoryClient client = SimpleRepositoryClient.createClient("http://localhost:8080/api/v1/dataresources/");
    SingleResourceAccessClient singleClient = client.withResourceId("test123");
    singleClient.setRestTemplate(restTemplate);

    mockServer.expect(once(), requestTo("http://localhost:8080/api/v1/dataresources/test123/data/"))
            .andRespond(withSuccess(mapper.writeValueAsString(new ContentInformation[]{res}), MediaType.APPLICATION_JSON));

    ContentInformation[] result = singleClient.getContentInformation("/");

    mockServer.verify();
    assertEquals(1, result.length);
    assertEquals("myFile.txt", result[0].getRelativePath());
  }

  @Test
  public void testSingleContentInformation() throws Exception{
    ContentInformation res = new ContentInformation();
    res.setRelativePath("myFile.txt");

    SimpleRepositoryClient client = SimpleRepositoryClient.createClient("http://localhost:8080/api/v1/dataresources/");
    SingleResourceAccessClient singleClient = client.withResourceId("test123");
    singleClient.setRestTemplate(restTemplate);

    mockServer.expect(once(), requestTo("http://localhost:8080/api/v1/dataresources/test123/data/myFile.txt"))
            .andRespond(withSuccess(mapper.writeValueAsString(res), MediaType.APPLICATION_JSON));

    ContentInformation[] result = singleClient.getContentInformation("myFile.txt");

    mockServer.verify();
    assertEquals(1, result.length);
    assertEquals("myFile.txt", result[0].getRelativePath());
  }

  @Test
  public void testUploadContent() throws Exception{
    SimpleRepositoryClient client = SimpleRepositoryClient.createClient("http://localhost:8080/api/v1/dataresources/");
    Path tmp = Paths.get("testFile.txt");
    try{

      Files.write(tmp, "This is a test".getBytes());

      UploadClient uploadClient = client.withResourceId("test123").withFile(tmp.toFile());

      uploadClient.setRestTemplate(restTemplate);

      mockServer.expect(once(), requestTo("http://localhost:8080/api/v1/dataresources/test123/data/testFile.txt?force=false"))
              .andRespond(MockRestResponseCreators.withCreatedEntity(URI.create("http://localhost:8080/api/v1/dataresources/test123/data/testFile.txt")));

      int status = uploadClient.upload("testFile.txt");

      mockServer.verify();
      assertEquals(201, status);
    } finally{
      try{
        Files.delete(tmp);
      } catch(Exception e){
      }
    }
  }

  @Test(expected = IllegalArgumentException.class)
  public void testUploadWithNullFile() throws Exception{
    SimpleRepositoryClient client = SimpleRepositoryClient.createClient("http://localhost:8080/api/v1/dataresources/");

    UploadClient uploadClient = client.withResourceId("test123").withFile(null);

    uploadClient.upload("testShouldFail");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testUploadWithNullFileAndNoContentUri() throws Exception{
    SimpleRepositoryClient client = SimpleRepositoryClient.createClient("http://localhost:8080/api/v1/dataresources/");
    ContentInformation res = new ContentInformation();
    res.getMetadata().put("test", "OK");

    UploadClient uploadClient = client.withResourceId("test123").withFile(null).withMetadata(res);

    uploadClient.upload("testShouldFail");
  }

  @Test
  public void testUploadContentWithForce() throws Exception{
    SimpleRepositoryClient client = SimpleRepositoryClient.createClient("http://localhost:8080/api/v1/dataresources/");
    Path tmp = Paths.get("testFile.txt");
    try{

      Files.write(tmp, "This is a test".getBytes());

      UploadClient uploadClient = client.withResourceId("test123").withFile(tmp.toFile()).withOverwrite(true);

      uploadClient.setRestTemplate(restTemplate);

      mockServer.expect(once(), requestTo("http://localhost:8080/api/v1/dataresources/test123/data/testFile.txt?force=true"))
              .andRespond(MockRestResponseCreators.withCreatedEntity(URI.create("http://localhost:8080/api/v1/dataresources/test123/data/testFile.txt")));

      int status = uploadClient.upload("testFile.txt");

      mockServer.verify();
      assertEquals(201, status);
    } finally{
      try{
        Files.delete(tmp);
      } catch(Exception e){
      }
    }
  }

  @Test
  public void testUploadContentWithMetadata() throws Exception{
    ContentInformation res = new ContentInformation();
    res.getMetadata().put("test", "OK");

    SimpleRepositoryClient client = SimpleRepositoryClient.createClient("http://localhost:8080/api/v1/dataresources/");
    Path tmp = Paths.get("testFile.txt");
    try{

      Files.write(tmp, "This is a test".getBytes());

      UploadClient uploadClient = client.withResourceId("test123").withFile(tmp.toFile()).withMetadata(res).withOverwrite(true);

      uploadClient.setRestTemplate(restTemplate);

      mockServer.expect(once(), requestTo("http://localhost:8080/api/v1/dataresources/test123/data/testFile.txt?force=true"))
              .andRespond(MockRestResponseCreators.withCreatedEntity(URI.create("http://localhost:8080/api/v1/dataresources/test123/data/testFile.txt")));

      int status = uploadClient.upload("testFile.txt");

      mockServer.verify();
      assertEquals(201, status);
    } finally{
      try{
        Files.delete(tmp);
      } catch(Exception e){
      }
    }
  }

  @Test
  public void testUploadContentOnlyWithMetadata() throws Exception{
    ContentInformation res = new ContentInformation();
    res.setContentUri("http://google.com");

    SimpleRepositoryClient client = SimpleRepositoryClient.createClient("http://localhost:8080/api/v1/dataresources/");
    Path tmp = Paths.get("testFile.txt");
    try{

      Files.write(tmp, "This is a test".getBytes());

      UploadClient uploadClient = client.withResourceId("test123").withMetadata(res).withOverwrite(true);

      uploadClient.setRestTemplate(restTemplate);

      mockServer.expect(once(), requestTo("http://localhost:8080/api/v1/dataresources/test123/data/testFile.txt?force=true"))
              .andRespond(MockRestResponseCreators.withCreatedEntity(URI.create("http://localhost:8080/api/v1/dataresources/test123/data/testFile.txt")));

      int status = uploadClient.upload("testFile.txt");

      mockServer.verify();
      assertEquals(201, status);
    } finally{
      try{
        Files.delete(tmp);
      } catch(Exception e){
      }
    }
  }
}
