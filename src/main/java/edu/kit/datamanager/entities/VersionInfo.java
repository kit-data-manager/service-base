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
package edu.kit.datamanager.entities;

/**
 *
 * @author jejkal
 */
import java.util.Set;

import javax.validation.constraints.NotBlank;

import lombok.Getter;
import lombok.Setter;

/**
 * Version information that contains all details of an object.
 *
 * @author Chelbi
 */
@Getter
@Setter
public class VersionInfo{

  /**
   * object identifier
   */
  private String objectId;

  /**
   * version identifier
   */
  private String versionId;

  /**
   * parent version
   */
  private String parent;

  /**
   * finalize attribute defines if the version is closed or not
   */
  private String finalize;

  /**
   * token number attribute
   */
  private String token;

  /**
   * commit message attribute
   */
  private String commitMessage;

  /**
   * user attribute
   */
  private String user;

  /**
   * relative paths of the files
   */
  private Set<String> relativePaths;

  public VersionInfo(@NotBlank String objectId, @NotBlank String versionId, String parent, String finalize,
          String token, String commitMessage, String user, Set<String> relativePaths){
    this.objectId = objectId;
    this.versionId = versionId;
    this.parent = parent;
    this.finalize = finalize;
    this.token = token;
    this.commitMessage = commitMessage;
    this.user = user;
    this.relativePaths = relativePaths;
  }
}
