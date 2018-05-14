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
package edu.kit.datamanager.util;

import edu.kit.datamanager.entities.BaseEnum;
import java.util.Objects;

/**
 *
 * @author jejkal
 */
public class EnumUtils{

  public static boolean equals(Enum<? extends BaseEnum> first, Enum<? extends BaseEnum> second){
    String first_value = (first == null) ? null : ((BaseEnum) first).getValue();
    String second_value = (second == null) ? null : ((BaseEnum) second).getValue();
    return Objects.equals(first_value, second_value);
  }

  public static int hashCode(Enum<? extends BaseEnum> value){
    return (value == null) ? Objects.hashCode(null) : Objects.hashCode(((BaseEnum) value).getValue());
  }

}
