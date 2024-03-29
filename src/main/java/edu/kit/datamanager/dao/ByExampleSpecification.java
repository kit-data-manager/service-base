/*
 * Copyright 2018 Karlsruhe Institute of Technology.
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
package edu.kit.datamanager.dao;

import edu.kit.datamanager.annotations.Searchable;
import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.metamodel.Attribute;
import jakarta.persistence.metamodel.Attribute.PersistentAttributeType;
import jakarta.persistence.metamodel.SingularAttribute;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;
import org.apache.commons.lang3.Validate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;

/**
 * Helper to create find by example query.
 */
public class ByExampleSpecification{

  private final EntityManager entityManager;

  public ByExampleSpecification(EntityManager entityManager){
    this.entityManager = entityManager;
  }

  public <T> Specification<T> byExample(T example){
    return byExample(entityManager, example);
  }

  /**
   * Lookup entities having at least one String attribute matching the passed
   * pattern.
   *
   * @param <T> The generic type used by the returned specification.
   * @param pattern The pattern used to query for a string attribute.
   * @param entityType The entity type returned by the specification.
   *
   * @return The specification.
   */
  public <T> Specification<T> byPatternOnStringAttributes(String pattern, Class<T> entityType){
    return byPatternOnStringAttributes(entityManager, pattern, entityType);
  }

  private <T> Specification<T> byExample(final EntityManager em, final T example){
    Validate.notNull(example, "example must not be null");

    @SuppressWarnings("unchecked")
    final Class<T> type = (Class<T>) example.getClass();

    return (Root<T> root, CriteriaQuery<?> query, CriteriaBuilder builder) -> {
      List<Predicate> predicates = new ArrayList<>();

      Set<SingularAttribute<T, ?>> types = em.getMetamodel().entity(type).getDeclaredSingularAttributes();

      for(Attribute<T, ?> attr : types){
        if(attr.getPersistentAttributeType() == PersistentAttributeType.MANY_TO_ONE || attr.getPersistentAttributeType() == PersistentAttributeType.ONE_TO_ONE){
          //ignore relations
          continue;
        }

        String fieldName = attr.getName();

        try{
          Member javaMember = attr.getJavaMember();
          boolean searchableField;
          if(javaMember instanceof Field){
            searchableField = ((Field) javaMember).getAnnotation(Searchable.class) != null;
          } else{
            searchableField = ((Method) javaMember).getAnnotation(Searchable.class) != null;
          }

          if(searchableField){
            if(attr.getJavaType() == String.class){
              String fieldValue;
              if(javaMember instanceof Field){
                fieldValue = (String) ((Field) javaMember).get(example);
              } else{
                fieldValue = (String) ReflectionUtils.invokeMethod((Method) javaMember, example);
              }

              if(isNotEmpty(fieldValue)){
                // please compiler
                SingularAttribute<T, String> stringAttr = em.getMetamodel().entity(type).getDeclaredSingularAttribute(fieldName, String.class);
                // apply like
                predicates.add(builder.like(root.get(stringAttr), pattern(fieldValue)));
              }
            } else{
              Object fieldValue;
              if(javaMember instanceof Field){
                fieldValue = ((Field) javaMember).get(example);
              } else{
                fieldValue = (String) ReflectionUtils.invokeMethod((Method) javaMember, example);
              }

              if(fieldValue != null){
                // please compiler
                SingularAttribute<T, ?> anyAttr = em.getMetamodel().entity(type).getDeclaredSingularAttribute(fieldName, fieldValue.getClass());
                //  apply equal
                predicates.add(builder.equal(root.get(anyAttr), fieldValue));
              }
            }
          }
        } catch(IllegalAccessException | IllegalArgumentException e){
          throw new IllegalStateException("Cannot create specification, IllegalAccessException or IllegalArgumentException occured.", e);
        }
      }

      if(predicates.size() > 0){
        return builder.and((Predicate[]) predicates.toArray(new Predicate[predicates.size()]));
      }

      return builder.conjunction();
    };
  }

  private <T> Specification<T> byPatternOnStringAttributes(final EntityManager em, final String pattern, final Class<T> type){
    return (Root<T> root, CriteriaQuery<?> query, CriteriaBuilder builder) -> {
      List<Predicate> predicates = new ArrayList<>();

      Set<SingularAttribute<T, ?>> types = em.getMetamodel().entity(type).getDeclaredSingularAttributes();

      for(Attribute<T, ?> attr : types){
        if(attr.getPersistentAttributeType() == PersistentAttributeType.MANY_TO_ONE
                || attr.getPersistentAttributeType() == PersistentAttributeType.ONE_TO_ONE){
          continue;
        }

        String fieldName = attr.getName();

        try{
          if(attr.getJavaType() == String.class){
            if(isNotEmpty(pattern)){
              SingularAttribute<T, String> stringAttr = em.getMetamodel().entity(type).getDeclaredSingularAttribute(fieldName, String.class);
              predicates.add(builder.like(root.get(stringAttr), pattern(pattern)));
            }
          }
        } catch(Exception e){
          throw new IllegalStateException("Failed to add predicate for attribute " + fieldName + " for unknown reasons.", e);
        }
      }

      if(predicates.size() > 0){
        return builder.or((Predicate[]) predicates.toArray(new Predicate[predicates.size()]));
      }

      return builder.conjunction(); // 1 = 1
    };
  }

  private static String pattern(String str){
    return "%" + str + "%";
  }
}
