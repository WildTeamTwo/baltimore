package com.baltimore.google;

import com.baltimore.persistence.Cache;

import java.sql.SQLException;

/**
 * Created by paul on 04.09.18.
 */
public class GeoCodeCache {

    Cache cache;

    public GeoCodeCache(Cache cache) {
        this.cache = cache;
    }

    public void store(String lat, String longitude, String json) throws SQLException {
        if (lat != null && longitude != null && json != null) {
            cache.createGeoCode(lat, longitude, json);
        }

    }

    public void store(String address, String json) throws SQLException {
        if (address != null && json != null) {
            cache.createGeoCode(address, json);
        }

    }

    public String get(String lat, String longitude) throws SQLException {
        return cache.readGeoCode(lat, longitude);
    }

    public String get(String address) throws SQLException {
        return cache.readGeoCode(address);
    }
}
