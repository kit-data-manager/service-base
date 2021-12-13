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
import edu.kit.datamanager.entities.repo.Subject;
import java.util.HashSet;
import org.datacite.schema.kernel_4.Resource;

/**
 *
 * @author jejkal
 */
public class SubjectsConverter extends DozerConverter<HashSet, Resource.Subjects> implements MapperAware{

  private Mapper mapper;

  public SubjectsConverter(){
    super(HashSet.class, Resource.Subjects.class);
  }

  @Override
  public Resource.Subjects convertTo(HashSet a, Resource.Subjects b){
    Resource.Subjects result = new Resource.Subjects();
    for(Object o : a){
      Subject s = (Subject) o;
      Resource.Subjects.Subject subject = new Resource.Subjects.Subject();
      subject.setLang(s.getLang());
      if(s.getScheme() != null){
        subject.setSchemeURI(s.getScheme().getSchemeUri());
        subject.setSubjectScheme(s.getScheme().getSchemeId());
      }
      subject.setValue(s.getValue());
      subject.setValueURI(s.getValueUri());
      result.getSubject().add(subject);
    }
    return result;
  }

  @Override
  public HashSet convertFrom(Resource.Subjects b, HashSet a){
    return null;
  }

  @Override
  public void setMapper(Mapper mapper){
    this.mapper = mapper;
  }

}
