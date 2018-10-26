package com.baltimore.google;


import com.baltimore.common.Configuration;
import com.baltimore.common.JsonMap;
import com.baltimore.common.data.GoogleResults;

import java.io.IOException;
import java.sql.SQLException;

/**
 * Created by paul on 04.09.18.
 */
public class GoogleClient {

    GeoCodeClient geocodeClient;
    GeoCodeCache geocodeCache;

    private GoogleClient() {
        geocodeClient = new GeoCodeClient();
        geocodeCache = GeoCodeCache.init();
    }

    public GoogleResults requestGeocode(String latitude, String longitude, String address) throws IOException {
        GoogleResults results = null;
        try {

            String geocode = findGeoCodeInCache(latitude, longitude, address);
            if (geocode == null && Configuration.ACTIVATE_GOOGLE_CALL ) {
                geocode = callGoogle(latitude, longitude, address);
                results = JsonMap.map(geocode, latitude, longitude);
                cacheGeoCode(results, latitude, longitude, address, geocode);
                return results;
            }
            else if(geocode != null) {
                return JsonMap.map(geocode, latitude, longitude);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String callGoogle(String latitude, String longitude, String address ) throws IOException{
        return latitude != null ? geocodeClient.requestGeocode(latitude, longitude) : geocodeClient.requestGeocode(address);
    }
    private String findGeoCodeInCache(String latitude, String longitude, String address) throws SQLException{
        return latitude != null && !latitude.isEmpty() ? geocodeCache.get(latitude, longitude) : geocodeCache.get(address);
    }

    private void cacheGeoCode(GoogleResults results, String latitude, String longitude, String address, String geocode) throws IOException, SQLException {
        if(results.getStatus().equalsIgnoreCase("OK") || results.getStatus().equalsIgnoreCase("ZERO_RESULTS")) {
            if(latitude != null)
                geocodeCache.store(latitude, longitude, geocode);
            else
                geocodeCache.store(address, geocode);
        }
    }

    public static GoogleClient init() {
        return new GoogleClient();
    }

}
