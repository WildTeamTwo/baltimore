package com.baltimore.model;

/**
 * Created by paul on 23.09.18.
 */
public interface Googleable {

    public String getLatitude();

    public String getLongitude();

    public String getAddress();

    public GoogleResults getGoogleResults();

    public void setGoogleResults(GoogleResults r);
}

