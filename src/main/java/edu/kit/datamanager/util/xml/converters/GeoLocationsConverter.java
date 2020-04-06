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

import edu.kit.datamanager.entities.repo.GeoLocation;
import java.util.HashSet;
import org.datacite.schema.kernel_4.Box;
import org.datacite.schema.kernel_4.Point;
import org.datacite.schema.kernel_4.Resource;
import org.dozer.DozerConverter;
import org.dozer.Mapper;
import org.dozer.MapperAware;

/**
 *
 * @author jejkal
 */
public class GeoLocationsConverter extends DozerConverter<HashSet, Resource.GeoLocations> implements MapperAware{

  private Mapper mapper;

  public GeoLocationsConverter(){
    super(HashSet.class, Resource.GeoLocations.class);
  }

  @Override
  public Resource.GeoLocations convertTo(HashSet a, Resource.GeoLocations b){
    Resource.GeoLocations result = new Resource.GeoLocations();
    for(Object o : a){
      GeoLocation t = (GeoLocation) o;
      Resource.GeoLocations.GeoLocation location = new Resource.GeoLocations.GeoLocation();
      location.setGeoLocationPlace(t.getPlace());

      if(t.getPoint() != null){
        Point point = new Point();
        point.setPointLatitude(t.getPoint().getLatitude());
        point.setPointLongitude(t.getPoint().getLongitude());
        location.setGeoLocationPoint(point);
      }

      if(t.getBox() != null){
        Box box = new Box();
        box.setEastBoundLongitude(t.getBox().getEastLongitude());
        box.setNorthBoundLatitude(t.getBox().getNorthLatitude());
        box.setSouthBoundLatitude(t.getBox().getSouthLatitude());
        box.setWestBoundLongitude(t.getBox().getWestLongitude());
        location.setGeoLocationBox(box);
      }

      if(t.getPolygon() != null){
        Resource.GeoLocations.GeoLocation.GeoLocationPolygon poly = new Resource.GeoLocations.GeoLocation.GeoLocationPolygon();
        for(edu.kit.datamanager.entities.repo.Point p : t.getPolygon().getPoints()){
          Point point = new Point();
          point.setPointLatitude(p.getLatitude());
          point.setPointLongitude(p.getLongitude());
          poly.getPolygonPoint().add(point);
        }
        location.setGeoLocationPolygon(poly);
      }

      result.getGeoLocation().add(location);
    }
    return result;
  }

  @Override
  public HashSet convertFrom(Resource.GeoLocations b, HashSet a){
    return null;
  }

  @Override
  public void setMapper(Mapper mapper){
    this.mapper = mapper;
  }

}
