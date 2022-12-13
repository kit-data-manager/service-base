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
package edu.kit.datamanager.util.xml.converters;

import com.github.dozermapper.core.DozerConverter;
import com.github.dozermapper.core.Mapper;
import com.github.dozermapper.core.MapperAware;
import java.util.HashSet;
import org.datacite.schema.kernel_4.Resource;


/**
 * Converter between HashSet and Formats object.
 * @author jejkal
 */
public class FormatsConverter extends DozerConverter<HashSet, Resource.Formats> implements MapperAware{

  private Mapper mapper;

  public FormatsConverter(){
    super(HashSet.class, Resource.Formats.class);
  }

  @Override
  public Resource.Formats convertTo(HashSet a, Resource.Formats b){
    Resource.Formats formats = new Resource.Formats();
    for(Object o : a){
      formats.getFormat().add((String) o);
    }
    return formats;
  }

  @Override
  public HashSet convertFrom(Resource.Formats b, HashSet a){
    return null;
  }

  @Override
  public void setMapper(Mapper mapper){
    this.mapper = mapper;
  }

}
