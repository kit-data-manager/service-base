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

import edu.kit.datamanager.entities.repo.ResourceType;
import org.datacite.schema.kernel_4.Resource;
import org.dozer.DozerConverter;
import org.dozer.Mapper;
import org.dozer.MapperAware;

/**
 *
 * @author jejkal
 */
public class ResourceTypeConverter extends DozerConverter<ResourceType, Resource.ResourceType> implements MapperAware{
  
  private Mapper mapper;
  
  public ResourceTypeConverter(){
    super(ResourceType.class, Resource.ResourceType.class);
  }
  
  @Override
  public Resource.ResourceType convertTo(ResourceType a, Resource.ResourceType b){
    Resource.ResourceType result = new Resource.ResourceType();
    if(a.getTypeGeneral() != null){
      result.setResourceTypeGeneral(org.datacite.schema.kernel_4.ResourceType.fromValue(a.getTypeGeneral().getValue()));
    } else{
      result.setResourceTypeGeneral(org.datacite.schema.kernel_4.ResourceType.OTHER);
    }
    result.setValue(a.getValue());
    return result;
  }
  
  @Override
  public ResourceType convertFrom(Resource.ResourceType b, ResourceType a){
    return null;
  }
  
  @Override
  public void setMapper(Mapper mapper){
    this.mapper = mapper;
  }
  
}
