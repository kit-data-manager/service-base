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

import com.fasterxml.jackson.databind.JsonNode;
import com.github.fge.jackson.jsonpointer.JsonPointer;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchOperation;
import com.github.fge.jsonpatch.ReplaceOperation;
import edu.kit.datamanager.annotations.SecureUpdate;
import edu.kit.datamanager.exceptions.PatchApplicationException;
import edu.kit.datamanager.exceptions.UpdateForbiddenException;
import edu.kit.datamanager.util.PatchUtil;
import java.util.Arrays;
import static edu.kit.datamanager.util.JsonMapper.mapper;
import java.io.IOException;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

/**
 *
 * @author jejkal
 */
@RunWith(PowerMockRunner.class)
public class PatchUtilTest{

  @Test
  public void patchUnsecuredField() throws IOException{
    TestEntity e = new TestEntity(0, 1, "test");

    JsonPatchOperation op = new ReplaceOperation(JsonPointer.of("text"), mapper.convertValue("updated", JsonNode.class));
    JsonPatch replace = new JsonPatch(Arrays.asList(op));

    TestEntity patched = PatchUtil.applyPatch(e, replace, TestEntity.class, Arrays.asList(new SimpleGrantedAuthority("ADMINISTRATOR")));
    Assert.assertEquals("updated", patched.getText());
  }

  @Test(expected = UpdateForbiddenException.class)
  public void patchForbiddenField() throws IOException{
    TestEntity e = new TestEntity(0, 1, "test");

    JsonPatchOperation op = new ReplaceOperation(JsonPointer.of("id"), mapper.convertValue(2, JsonNode.class));
    JsonPatch replace = new JsonPatch(Arrays.asList(op));

    PatchUtil.applyPatch(e, replace, TestEntity.class, Arrays.asList(new SimpleGrantedAuthority("ADMINISTRATOR")));
  }

  @Test
  public void patchSecuredFieldWithSufficientRole() throws IOException{
    TestEntity e = new TestEntity(0, 1, "test");

    JsonPatchOperation op = new ReplaceOperation(JsonPointer.of("number"), mapper.convertValue(2, JsonNode.class));
    JsonPatch replace = new JsonPatch(Arrays.asList(op));

    TestEntity patched = PatchUtil.applyPatch(e, replace, TestEntity.class, Arrays.asList(new SimpleGrantedAuthority("ADMINISTRATOR")));
    Assert.assertEquals(2, patched.getNumber());
  }

  @Test(expected = UpdateForbiddenException.class)
  public void patchSecuredFieldWithInsufficientRole() throws IOException{
    TestEntity e = new TestEntity(0, 1, "test");

    JsonPatchOperation op = new ReplaceOperation(JsonPointer.of("number"), mapper.convertValue(2, JsonNode.class));
    JsonPatch replace = new JsonPatch(Arrays.asList(op));

    PatchUtil.applyPatch(e, replace, TestEntity.class, Arrays.asList(new SimpleGrantedAuthority("MEMBER")));
  }

  @Test(expected = PatchApplicationException.class)
  public void applyInvalidPatch() throws IOException{
    TestEntity e = new TestEntity(0, 1, "test");

    JsonPatchOperation op = new ReplaceOperation(JsonPointer.of("invalidField"), mapper.convertValue("updated", JsonNode.class));
    JsonPatch replace = new JsonPatch(Arrays.asList(op));

    PatchUtil.applyPatch(e, replace, TestEntity.class, Arrays.asList(new SimpleGrantedAuthority("ADMINISTRATOR")));
  }
}

class TestEntity{

  @SecureUpdate(value = "FORBIDDEN")
  private int id;
  @SecureUpdate(value = "ADMINISTRATOR")
  private int number;
  private String text;

  public TestEntity(){
  }

  public TestEntity(int id, int number, String text){
    this.id = id;
    this.number = number;
    this.text = text;
  }

  public int getId(){
    return id;
  }

  public void setId(int id){
    this.id = id;
  }

  public int getNumber(){
    return number;
  }

  public void setNumber(int number){
    this.number = number;
  }

  public String getText(){
    return text;
  }

  public void setText(String text){
    this.text = text;
  }

}
