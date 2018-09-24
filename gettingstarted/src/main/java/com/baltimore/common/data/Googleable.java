package com.baltimore.common.data;

/**
 * Created by paul on 23.09.18.
 */
public interface Googleable {

    public String getLatitude();
    public String getLongitude();
    public String getAddress();
    public void setGoogleResults(GoogleResults r);
    public GoogleResults getGoogleResults();
}

