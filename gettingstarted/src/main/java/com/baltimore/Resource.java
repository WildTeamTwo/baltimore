package com.baltimore;

import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by paul on 18.07.18.
 */
public enum Resource {

    PROPERTY("https://data.baltimorecity.gov/resource/6act-qzuy.json","property","propertyid"),
    ARREST ("https://data.baltimorecity.gov/resource/icjs-e3jg.json","arrest","arrestdate"),
    VICTIM("https://data.baltimorecity.gov/resource/4ih5-d5d5.json","victim","crimedate"),
    THREE11("https://data.baltimorecity.gov/resource/ni4d-8w7k.json","three11calls","createddate"),
    PARKING("https://data.baltimorecity.gov/resource/citations.json","parkingcitations","importdate"),
    LIQUOR("https://data.baltimorecity.gov/resource/g2jf-x8pp.json","liquor","licensedate");

    public String url;
    public String path;
    public String orderby;

    Resource(String apiUrl, String localpath, String orderby){
        this.url = apiUrl;
        this.path = localpath;
        this.orderby = orderby;

    }

    public String toString(){
        return this.name();
    }
}
