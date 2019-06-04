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

import edu.kit.datamanager.entities.repo.Identifier;
import java.util.HashSet;
import org.datacite.schema.kernel_4.Resource;
import org.dozer.DozerConverter;
import org.dozer.Mapper;
import org.dozer.MapperAware;

/**
 *
 * @author jejkal
 */
public class AlternateIdentifierConverter extends DozerConverter<HashSet, Resource.AlternateIdentifiers> implements MapperAware{
  
  private Mapper mapper;
  
  public AlternateIdentifierConverter(){
    super(HashSet.class, Resource.AlternateIdentifiers.class);
  }
  
  @Override
  public Resource.AlternateIdentifiers convertTo(HashSet a, Resource.AlternateIdentifiers b){
    Resource.AlternateIdentifiers result = new Resource.AlternateIdentifiers();
    for(Object o : a){
      Identifier id = (Identifier) o;
      Resource.AlternateIdentifiers.AlternateIdentifier identifier = new Resource.AlternateIdentifiers.AlternateIdentifier();
      if(id.getIdentifierType() != null){
        identifier.setAlternateIdentifierType(id.getIdentifierType().getValue());
      } else{
        identifier.setAlternateIdentifierType("Other");
      }
      identifier.setValue(id.getValue());
      result.getAlternateIdentifier().add(identifier);
    }
    return result;
  }
  
  @Override
  public HashSet convertFrom(Resource.AlternateIdentifiers b, HashSet a){
    return null;
  }
  
  @Override
  public void setMapper(Mapper mapper){
    this.mapper = mapper;
  }
  
}
