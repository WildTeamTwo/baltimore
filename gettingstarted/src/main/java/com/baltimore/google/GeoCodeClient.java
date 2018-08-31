package com.baltimore.google;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Objects;

/**
 * Created by paul on 13.08.18.
 */
public class GeoCodeClient {

    private final String GeoCode_URL = "https://maps.googleapis.com/maps/api/geocode/json";
    private final String API_KEY = "AIzaSyAw1ChJmKIWkbnpDqUKh_INNGpIDIJyenc";
    private final String coordinate_handle = "latlng";
    private final String api_handle = "key";

    public String requestGeocode(String latitude, String longitude) throws IOException {
        if(Objects.isNull(latitude) || Objects.isNull(longitude) || Double.isNaN(Double.parseDouble(latitude)) || Double.isNaN(Double.parseDouble(longitude))){
             return null;
        }

        try {
            HttpResponse response = request(encodeURL(latitude, longitude));
            String body = EntityUtils.toString(response.getEntity());
            if(isStatus200(response) && isSuccess(body) ){
                EntityUtils.consumeQuietly(response.getEntity());
                return body;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        //TODO: implement handling of timeouts

        return null;
    }

    private HttpResponse request(HttpGet httpGet) throws IOException {
        HttpClient client = HttpClientBuilder.create().build();
        HttpResponse response = client.execute(httpGet);
        return  response;
    }

    private boolean isStatus200(HttpResponse response) {
        return response.getStatusLine().getStatusCode() == 200;
    }


    private boolean isSuccess(String body) {
        return body != null || !body.contains("error_message");
    }

    private HttpGet encodeURL(String latitude, String longitude) throws URISyntaxException {
        StringBuilder latlng = new StringBuilder(latitude).append(',').append(longitude);
        URIBuilder builder = new URIBuilder(GeoCode_URL);
        builder.setParameter(coordinate_handle, latlng.toString());
        builder.setParameter(api_handle, API_KEY);

        return new HttpGet(builder.build());
    }
}
