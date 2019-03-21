package com.baltimore.google;


import com.baltimore.common.Configuration;
import com.baltimore.common.JsonMap;
import com.baltimore.model.GoogleResults;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.sql.SQLException;

/**
 * Created by paul on 04.09.18.
 */
public class GoogleClient {

    GeoCodeHttpClient geocodeHttpClient;
    GeoCodeCache geocodeCache;

    public GoogleClient(@Autowired GeoCodeHttpClient geocodeHttpClient, @Autowired GeoCodeCache geocodeCache) {
        this.geocodeHttpClient = geocodeHttpClient;
        this.geocodeCache = geocodeCache;
    }

    public GoogleResults requestGeocode(String latitude, String longitude, String address) throws IOException {
        GoogleResults results = null;
        try {

            String geocode = findGeoCodeInCache(latitude, longitude, address);
            if (geocode == null && Configuration.ACTIVATE_GOOGLE_CALL) {
                geocode = callGoogle(latitude, longitude, address);
                results = JsonMap.map(geocode, latitude, longitude);
                cacheGeoCode(results, latitude, longitude, address, geocode);
                return results;
            } else if (geocode != null) {
                return JsonMap.map(geocode, latitude, longitude);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String callGoogle(String latitude, String longitude, String address) throws IOException {
        return latitude != null ? geocodeHttpClient.requestGeocode(latitude, longitude) : geocodeHttpClient.requestGeocode(address);
    }

    private String findGeoCodeInCache(String latitude, String longitude, String address) throws SQLException {
        return latitude != null && !latitude.isEmpty() ? geocodeCache.get(latitude, longitude) : geocodeCache.get(address);
    }

    private void cacheGeoCode(GoogleResults results, String latitude, String longitude, String address, String geocode) throws IOException, SQLException {
        if (results.getStatus().equalsIgnoreCase("OK") || results.getStatus().equalsIgnoreCase("ZERO_RESULTS")) {
            if (latitude != null)
                geocodeCache.store(latitude, longitude, geocode);
            else
                geocodeCache.store(address, geocode);
        }
    }

}
