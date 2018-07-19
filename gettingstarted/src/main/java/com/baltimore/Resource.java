package com.baltimore;

/**
 * Created by paul on 18.07.18.
 */
public enum Resource {

    PROPERTY("https://data.baltimorecity.gov/resource/6act-qzuy.json","property"),
    ARREST ("https://data.baltimorecity.gov/resource/icjs-e3jg.json","arrest"),
    VICTIM("https://data.baltimorecity.gov/resource/4ih5-d5d5.json","victim"),
    THREE11("https://data.baltimorecity.gov/resource/ni4d-8w7k.json","three11calls"),
    PARKING("https://data.baltimorecity.gov/resource/citations.json","parkingcitations");

    public String url;
    public String path;

    Resource(String apiUrl, String localpath){
        url = apiUrl;
        path = localpath;

    }

    public String toString(){
        return this.name();
    }
}
