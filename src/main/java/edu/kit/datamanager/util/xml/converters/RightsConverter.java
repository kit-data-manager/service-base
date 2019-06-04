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

import edu.kit.datamanager.entities.repo.Scheme;
import java.util.HashSet;
import org.datacite.schema.kernel_4.Resource;
import org.dozer.DozerConverter;
import org.dozer.Mapper;
import org.dozer.MapperAware;

/**
 *
 * @author jejkal
 */
public class RightsConverter extends DozerConverter<HashSet, Resource.RightsList> implements MapperAware{

  private Mapper mapper;

  public RightsConverter(){
    super(HashSet.class, Resource.RightsList.class);
  }

  @Override
  public Resource.RightsList convertTo(HashSet a, Resource.RightsList b){
    Resource.RightsList result = new Resource.RightsList();
    for(Object o : a){
      Scheme r = (Scheme) o;
      Resource.RightsList.Rights rights = new Resource.RightsList.Rights();
      rights.setRightsURI(r.getSchemeUri());
      rights.setValue(r.getSchemeId());
      result.getRights().add(rights);
    }
    return result;
  }

  @Override
  public HashSet convertFrom(Resource.RightsList b, HashSet a){
    return null;
  }

  @Override
  public void setMapper(Mapper mapper){
    this.mapper = mapper;
  }

}
