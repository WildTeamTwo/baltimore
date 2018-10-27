package com.baltimore.google;

import com.baltimore.persistence.DAO;

import java.sql.SQLException;

/**
 * Created by paul on 04.09.18.
 */
class GeoCodeCache {

    DAO dao;

    private GeoCodeCache(DAO dao) {
        this.dao = dao;
    }

    public static GeoCodeCache init() {
        return new GeoCodeCache(DAO.init());
    }

    public void store(String lat, String longitude, String json) throws SQLException {
        if (lat != null && longitude != null && json != null) {
            dao.createGeoCode(lat, longitude, json);
        }

    }

    public void store(String address, String json) throws SQLException {
        if (address != null && json != null) {
            dao.createGeoCode(address, json);
        }

    }

    public String get(String lat, String longitude) throws SQLException {
        return dao.readGeoCode(lat, longitude);
    }

    public String get(String address) throws SQLException {
        return dao.readGeoCode(address);
    }
}
