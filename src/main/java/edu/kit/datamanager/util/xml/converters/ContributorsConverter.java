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
import edu.kit.datamanager.entities.repo.Contributor;
import java.util.HashSet;
import org.datacite.schema.kernel_4.ContributorType;
import org.datacite.schema.kernel_4.Resource;


/**Converter between HashSet and Contributors object.
 *
 * @author jejkal
 */
public class ContributorsConverter extends DozerConverter<HashSet, Resource.Contributors> implements MapperAware{

  private Mapper mapper;

  public ContributorsConverter(){
    super(HashSet.class, Resource.Contributors.class);
  }

  @Override
  public Resource.Contributors convertTo(HashSet a, Resource.Contributors b){
    Resource.Contributors result = new Resource.Contributors();
    for(Object o : a){
      Contributor c = (Contributor) o;
      Resource.Contributors.Contributor contributor = new Resource.Contributors.Contributor();
      if(c.getUser().getFamilyName() != null && c.getUser().getGivenName() != null){
        contributor.setContributorName(c.getUser().getFamilyName() + ", " + c.getUser().getGivenName());
      }
      contributor.setGivenName(c.getUser().getGivenName());
      contributor.setFamilyName(c.getUser().getGivenName());
      if(c.getContributionType() != null){
        contributor.setContributorType(ContributorType.fromValue(c.getContributionType().getValue()));
      } else{
        contributor.setContributorType(ContributorType.OTHER);
      }
      result.getContributor().add(contributor);
    }
    return result;
  }

  @Override
  public HashSet convertFrom(Resource.Contributors b, HashSet a){
    return null;
  }

  @Override
  public void setMapper(Mapper mapper){
    this.mapper = mapper;
  }

}
