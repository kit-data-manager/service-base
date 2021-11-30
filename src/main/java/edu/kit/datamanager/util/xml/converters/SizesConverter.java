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
 *
 * @author jejkal
 */
public class SizesConverter extends DozerConverter<HashSet, Resource.Sizes> implements MapperAware{

  private Mapper mapper;

  public SizesConverter(){
    super(HashSet.class, Resource.Sizes.class);
  }

  @Override
  public Resource.Sizes convertTo(HashSet a, Resource.Sizes b){
    Resource.Sizes sizes = new Resource.Sizes();
    for(Object o : a){
      sizes.getSize().add((String) o);
    }
    return sizes;
  }

  @Override
  public HashSet convertFrom(Resource.Sizes b, HashSet a){
    return null;
  }

  @Override
  public void setMapper(Mapper mapper){
    this.mapper = mapper;
  }

}
