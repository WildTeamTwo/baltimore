package com.baltimore.google;


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

    public GoogleResults requestGeocode(String latitude, String longitude) throws IOException {
        GoogleResults results = null;

        try {
            String geocode = geocodeCache.get(latitude, longitude);

            if (geocode == null) {
                geocode = geocodeClient.requestGeocode(latitude, longitude);
                results = JsonMap.map(geocode, latitude, longitude);
                cacheGeoCode(results, latitude, longitude, geocode);
                return results;
            }
            else {
                results = JsonMap.map(geocode, latitude, longitude);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return results;
    }

    private void cacheGeoCode(GoogleResults results, String latitude, String longitude, String geocode) throws IOException, SQLException {
        if(results.getStatus().equalsIgnoreCase("OK")) {
            geocodeCache.store(latitude, longitude, geocode);
        }
    }


    public static GoogleClient init() {
        return new GoogleClient();
    }
}
