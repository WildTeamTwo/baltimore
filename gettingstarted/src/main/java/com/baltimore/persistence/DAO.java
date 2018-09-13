package com.baltimore.persistence;

import com.baltimore.common.JsonMap;
import com.baltimore.common.data.GoogleResults;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Base64;

/**
 * Created by paul on 10.09.18.
 */
public class DAO {

    DAOImpl daoImpl;

    private DAO(DAOImpl daoImpl){
        this.daoImpl = daoImpl;
    }

    public static DAO init(){
        try {
            return new DAO(DAOImpl.init());
        }
        catch (SQLException e ){
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
    public String readGeoCode(final String latitude, final String longitude){
        try {
                return readeEncoded(latitude, longitude);
        }
        catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    public String readGeoCode(final String address){
        try {
            return readeEncoded(address);
        }
        catch (SQLException e){
            throw new RuntimeException(e);
        }
    }
    private String readeEncoded(final String address) throws  SQLException{
        String geocodeBase64 = readGeoCodeEncoded(address);
        return geocodeBase64 != null ? new String(Base64.getDecoder().decode(geocodeBase64)) : null;
    }

    private String readeEncoded(final String latitude, final  String longitude) throws  SQLException{
        String geocodeBase64 = readGeoCodeEncoded(latitude, longitude);
        return geocodeBase64 != null ? new String(Base64.getDecoder().decode(geocodeBase64)) : null;
    }

    private String readGeoCodeEncoded(final String latitude, final String longitude) throws SQLException{
        return daoImpl.readGeoCodeEncoded(latitude, longitude);
    }


    private String readGeoCodeEncoded(final String address) throws SQLException{
        return daoImpl.readGeoCodeEncoded(address);
    }
    public boolean createGeoCode(final String latitude, final String longitude, final String json){
        try {
           return daoImpl.createGeoCode(latitude, longitude, json);
        }
        catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    public boolean createGeoCode(final String address, final String json){
        try {
            return daoImpl.createGeoCode(address, json);
        }
        catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    public void close(){
        try {
            daoImpl.close();
        }
        catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

}

