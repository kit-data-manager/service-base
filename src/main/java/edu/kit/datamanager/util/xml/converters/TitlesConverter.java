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
import edu.kit.datamanager.entities.repo.Title;
import java.util.HashSet;
import org.datacite.schema.kernel_4.Resource;
import org.datacite.schema.kernel_4.TitleType;

/**
 *
 * @author jejkal
 */
public class TitlesConverter extends DozerConverter<HashSet, Resource.Titles> implements MapperAware {

    private Mapper mapper;

    public TitlesConverter() {
        super(HashSet.class, Resource.Titles.class);
    }

    @Override
    public Resource.Titles convertTo(HashSet a, Resource.Titles b) {
        Resource.Titles result = new Resource.Titles();
        for (Object o : a) {
            Title t = (Title) o;
            Resource.Titles.Title title = new Resource.Titles.Title();
            title.setLang(t.getLang());
            if (t.getTitleType() != null) {
                title.setTitleType(TitleType.fromValue(t.getTitleType().getValue()));
            } else {
                title.setTitleType(TitleType.OTHER);
            }
            title.setValue(t.getValue());
            result.getTitle().add(title);
        }
        return result;
    }

    @Override
    public HashSet convertFrom(Resource.Titles b, HashSet a) {
        return null;
    }

    @Override
    public void setMapper(Mapper mapper) {
        this.mapper = mapper;
    }

}
