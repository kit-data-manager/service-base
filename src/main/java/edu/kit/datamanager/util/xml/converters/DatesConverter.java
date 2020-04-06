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

import edu.kit.datamanager.entities.repo.Date;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import org.datacite.schema.kernel_4.DateType;
import org.datacite.schema.kernel_4.Resource;
import org.dozer.DozerConverter;
import org.dozer.Mapper;
import org.dozer.MapperAware;

/**
 *
 * @author jejkal
 */
public class DatesConverter extends DozerConverter<HashSet, Resource.Dates> implements MapperAware{

  private Mapper mapper;
  private final DateTimeFormatter fmt = DateTimeFormatter.ISO_DATE_TIME.withZone(ZoneOffset.UTC);

  public DatesConverter(){
    super(HashSet.class, Resource.Dates.class);
  }

  @Override
  public Resource.Dates convertTo(HashSet a, Resource.Dates b){
    Resource.Dates result = new Resource.Dates();

    for(Object o : a){
      Date d = (Date) o;
      Resource.Dates.Date date = new Resource.Dates.Date();
      if(d.getType() != null){
        date.setDateType(DateType.fromValue(d.getType().getValue()));
      }
      date.setValue(fmt.format(d.getValue()));
      result.getDate().add(date);
    }
    return result;
  }

  @Override
  public HashSet convertFrom(Resource.Dates b, HashSet a){
    return null;
  }

  @Override
  public void setMapper(Mapper mapper){
    this.mapper = mapper;
  }

}
