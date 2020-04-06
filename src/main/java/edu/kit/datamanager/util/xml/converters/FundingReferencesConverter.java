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

import edu.kit.datamanager.entities.repo.FundingReference;
import java.util.HashSet;
import org.datacite.schema.kernel_4.FunderIdentifierType;
import org.datacite.schema.kernel_4.Resource;
import org.dozer.DozerConverter;
import org.dozer.Mapper;
import org.dozer.MapperAware;

/**
 *
 * @author jejkal
 */
public class FundingReferencesConverter extends DozerConverter<HashSet, Resource.FundingReferences> implements MapperAware{
  
  private Mapper mapper;
  
  public FundingReferencesConverter(){
    super(HashSet.class, Resource.FundingReferences.class);
  }
  
  @Override
  public Resource.FundingReferences convertTo(HashSet a, Resource.FundingReferences b){
    Resource.FundingReferences result = new Resource.FundingReferences();
    for(Object o : a){
      FundingReference r = (FundingReference) o;
      Resource.FundingReferences.FundingReference reference = new Resource.FundingReferences.FundingReference();
      if(r.getAwardNumber() != null){
        Resource.FundingReferences.FundingReference.AwardNumber awardNumber = new Resource.FundingReferences.FundingReference.AwardNumber();
        awardNumber.setAwardURI(r.getAwardNumber().getSchemeUri());
        awardNumber.setValue(r.getAwardNumber().getSchemeId());
        reference.setAwardNumber(awardNumber);
      }
      reference.setAwardTitle(r.getAwardTitle());
      
      if(r.getFunderIdentifier() != null){
        Resource.FundingReferences.FundingReference.FunderIdentifier id = new Resource.FundingReferences.FundingReference.FunderIdentifier();
        if(r.getFunderIdentifier().getIdentifierType() != null){
          id.setFunderIdentifierType(FunderIdentifierType.fromValue(r.getFunderIdentifier().getType().getValue()));
        } else{
          id.setFunderIdentifierType(FunderIdentifierType.OTHER);
        }
        id.setValue(r.getFunderIdentifier().getValue());
        reference.setFunderIdentifier(id);
      }
      
      reference.setFunderName(r.getFunderName());
      result.getFundingReference().add(reference);
    }
    return result;
  }
  
  @Override
  public HashSet convertFrom(Resource.FundingReferences b, HashSet a){
    return null;
  }
  
  @Override
  public void setMapper(Mapper mapper){
    this.mapper = mapper;
  }
  
}
