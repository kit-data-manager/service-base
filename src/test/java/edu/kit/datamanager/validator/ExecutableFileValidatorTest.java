/*
 * Copyright 2020 hartmann-v.
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
package edu.kit.datamanager.validator;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import javax.validation.ConstraintValidatorContext;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author hartmann-v
 */
public class ExecutableFileValidatorTest {
  
  private static URL BASH_EXECUTABLE;
  
  public ExecutableFileValidatorTest() {
  }

  @BeforeClass
  public static void setUpClass() throws IOException {
     BASH_EXECUTABLE = new File("/bin/sh").toURI().toURL();
  }
  
  @AfterClass
  public static void tearDownClass() {
  }
  
  @Before
  public void setUp() {
  }
  
  @After
  public void tearDown() {
  }

  /**
   * Test of isValid method, of class ExecutableFileValidator.
   */
  @Test
  public void testIsValid() throws MalformedURLException {
    System.out.println("isValid");
    URL value = BASH_EXECUTABLE;
    ConstraintValidatorContext context = null;
    ExecutableFileValidator instance = new ExecutableFileValidator();
    boolean expResult = true;
    boolean result = instance.isValid(value, context);
    assertEquals(expResult, result);
  }
  @Test
  public void testIsInvalidFile() throws MalformedURLException {
    System.out.println("testIsInvalidFile");
    URL value = new URL("file:invalid/file");
    ConstraintValidatorContext context = null;
    ExecutableFileValidator instance = new ExecutableFileValidator();
    boolean expResult = false;
    boolean result = instance.isValid(value, context);
    assertEquals(expResult, result);
  }
  @Test
  public void testIsNull() throws MalformedURLException {
    System.out.println("testIsNull");
    URL value = null;
    ConstraintValidatorContext context = null;
    ExecutableFileValidator instance = new ExecutableFileValidator();
    boolean expResult = false;
    boolean result = instance.isValid(value, context);
    assertEquals(expResult, result);
  }
  @Test
  public void testIsPath() throws MalformedURLException {
    System.out.println("testIsPath");
    URL value = new URL("file:///tmp");
    ConstraintValidatorContext context = null;
    ExecutableFileValidator instance = new ExecutableFileValidator();
    boolean expResult = false;
    boolean result = instance.isValid(value, context);
    assertEquals(expResult, result);
  }
  @Test
  public void testIsNotExecutable() throws MalformedURLException {
    System.out.println("testIsNotExecutable");
    URL value = new URL("file:src/test/resources/examples/gemma/simple.json");
    ConstraintValidatorContext context = null;
    ExecutableFileValidator instance = new ExecutableFileValidator();
    boolean expResult = false;
    boolean result = instance.isValid(value, context);
    assertEquals(expResult, result);
  }
  @Test
  public void testIsNotURL() throws MalformedURLException {
    System.out.println("testIsNotURL");
    URL value = new URL("file: src/test/resources/examples/gemma/simple.json");
    ConstraintValidatorContext context = null;
    ExecutableFileValidator instance = new ExecutableFileValidator();
    boolean expResult = false;
    boolean result = instance.isValid(value, context);
    assertEquals(expResult, result);
  }
  
}
