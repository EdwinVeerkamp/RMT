package de.flower.rmt.service.geocoding;

import de.flower.common.util.geo.LatLng;

import java.io.Serializable;
import java.util.Map;

/**
 * @author flowerrrr
 */
public class GeocodingResult implements Serializable {

    private String formatted_address;

    private Geometry geometry;

    public String getAddress() {
        return formatted_address;
    }

    public LatLng getLatLng() {
        return new LatLng(geometry.location.get("lat"), geometry.location.get("lng"));
    }

    private static class Geometry implements Serializable {

        public Map<String, Double> location;

    }

}
