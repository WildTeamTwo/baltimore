package com.baltimore;

/**
 * Created by paul on 18.07.18.
 */
public enum Resource {

    PROPERTY("https://data.baltimorecity.gov/resource/6act-qzuy.json","property","propertyID"),
    ARREST ("https://data.baltimorecity.gov/resource/icjs-e3jg.json","arrest","arrestDate"),
    VICTIM("https://data.baltimorecity.gov/resource/4ih5-d5d5.json","victim","crimeDate"),
    THREE11("https://data.baltimorecity.gov/resource/ni4d-8w7k.json","three11calls","createdDate"),
    PARKING("https://data.baltimorecity.gov/resource/citations.json","parkingcitations","importdate");

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
