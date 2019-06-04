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

import edu.kit.datamanager.entities.repo.Description;
import java.util.HashSet;
import org.datacite.schema.kernel_4.DescriptionType;
import org.datacite.schema.kernel_4.Resource;
import org.dozer.DozerConverter;
import org.dozer.Mapper;
import org.dozer.MapperAware;

/**
 *
 * @author jejkal
 */
public class DescriptionsConverter extends DozerConverter<HashSet, Resource.Descriptions> implements MapperAware{

  private Mapper mapper;

  public DescriptionsConverter(){
    super(HashSet.class, Resource.Descriptions.class);
  }

  @Override
  public Resource.Descriptions convertTo(HashSet a, Resource.Descriptions b){
    Resource.Descriptions result = new Resource.Descriptions();
    for(Object o : a){
      Description d = (Description) o;
      Resource.Descriptions.Description description = new Resource.Descriptions.Description();
      description.setLang(d.getLang());
      if(d.getType() != null){
        description.setDescriptionType(DescriptionType.fromValue(d.getType().getValue()));
      } else{
        description.setDescriptionType(DescriptionType.OTHER);
      }
      description.getContent().add(d.getDescription());
      result.getDescription().add(description);
    }
    return result;
  }

  @Override
  public HashSet convertFrom(Resource.Descriptions b, HashSet a){
    return null;
  }

  @Override
  public void setMapper(Mapper mapper){
    this.mapper = mapper;
  }

}
