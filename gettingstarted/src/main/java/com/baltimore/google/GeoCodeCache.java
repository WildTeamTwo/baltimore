package com.baltimore.google;

import com.baltimore.persistence.DAO;

import java.sql.SQLException;

/**
 * Created by paul on 04.09.18.
 */
class GeoCodeCache {

    DAO dao;

    private GeoCodeCache(DAO dao){
        this.dao = dao;
    }
    public void store(String lat, String longitude, String json) throws SQLException{
        if(lat != null || longitude != null || json != null){
            dao.createGeoCode(lat, longitude, json);
        }

    }

    public String get(String lat, String longitude) throws SQLException{
        return dao.readGeoCode(lat, longitude);
    }

    public static GeoCodeCache init(){
        return new GeoCodeCache(DAO.init());
    }
}
