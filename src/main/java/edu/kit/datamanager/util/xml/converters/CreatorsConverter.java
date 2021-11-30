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
import edu.kit.datamanager.entities.repo.Agent;
import java.util.HashSet;
import org.datacite.schema.kernel_4.Resource;

/**
 *
 * @author jejkal
 */
public class CreatorsConverter extends DozerConverter<HashSet, Resource.Creators> implements MapperAware{

  private Mapper mapper;

  public CreatorsConverter(){
    super(HashSet.class, Resource.Creators.class);
  }

  @Override
  public Resource.Creators convertTo(HashSet a, Resource.Creators b){
    Resource.Creators result = new Resource.Creators();
    for(Object o : a){
      Agent agent = (Agent) o;
      Resource.Creators.Creator creator = new Resource.Creators.Creator();
      if(agent.getFamilyName() != null && agent.getGivenName() != null){
        creator.setCreatorName(agent.getFamilyName() + ", " + agent.getGivenName());
      }
      creator.setGivenName(agent.getGivenName());
      creator.setFamilyName(agent.getFamilyName());
      result.getCreator().add(creator);
    }
    return result;
  }

  @Override
  public HashSet convertFrom(Resource.Creators b, HashSet a){
    return null;
  }

  @Override
  public void setMapper(Mapper mapper){
    this.mapper = mapper;
  }

}
