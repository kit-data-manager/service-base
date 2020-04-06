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

import edu.kit.datamanager.entities.repo.RelatedIdentifier;
import java.util.HashSet;
import org.datacite.schema.kernel_4.RelatedIdentifierType;
import org.datacite.schema.kernel_4.RelationType;
import org.datacite.schema.kernel_4.Resource;
import org.dozer.DozerConverter;
import org.dozer.Mapper;
import org.dozer.MapperAware;

/**
 *
 * @author jejkal
 */
public class RelatedIdentifierConverter extends DozerConverter<HashSet, Resource.RelatedIdentifiers> implements MapperAware{

  private Mapper mapper;

  public RelatedIdentifierConverter(){
    super(HashSet.class, Resource.RelatedIdentifiers.class);
  }

  @Override
  public Resource.RelatedIdentifiers convertTo(HashSet a, Resource.RelatedIdentifiers b){
    Resource.RelatedIdentifiers result = new Resource.RelatedIdentifiers();
    for(Object o : a){
      RelatedIdentifier id = (RelatedIdentifier) o;
      Resource.RelatedIdentifiers.RelatedIdentifier identifier = new Resource.RelatedIdentifiers.RelatedIdentifier();
      if(id.getIdentifierType() != null){
        identifier.setRelatedIdentifierType(RelatedIdentifierType.fromValue(id.getIdentifierType().getValue()));
      }

      identifier.setRelationType(RelationType.fromValue(id.getRelationType().getValue()));
      identifier.setRelatedMetadataScheme(id.getRelatedMetadataScheme());
      if(id.getScheme() != null){
        identifier.setSchemeType(id.getScheme().getSchemeId());
        identifier.setSchemeURI(id.getScheme().getSchemeUri());
      }
      identifier.setValue(id.getValue());
      result.getRelatedIdentifier().add(identifier);
    }
    return result;
  }

  @Override
  public HashSet convertFrom(Resource.RelatedIdentifiers b, HashSet a){
    return null;
  }

  @Override
  public void setMapper(Mapper mapper){
    this.mapper = mapper;
  }

}
