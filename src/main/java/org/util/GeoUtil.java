package org.util;

import org.geotools.geometry.jts.JTSFactoryFinder;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;

public class GeoUtil {

    public static Point getPoint(String longitude, String latitude) {
        return getPoint(Double.parseDouble(latitude), Double.parseDouble(longitude));
    }

    public static Point getPoint(double latitude, double longitude) {
        GeometryFactory geometryFactory = JTSFactoryFinder.getGeometryFactory();
        Coordinate coordinate = new Coordinate(longitude, latitude);
        return geometryFactory.createPoint(coordinate);
    }

}
