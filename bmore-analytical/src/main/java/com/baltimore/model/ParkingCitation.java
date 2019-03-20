package com.baltimore.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;


/**
 * Created by paul on 13.08.18.
 */

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ParkingCitation implements Googleable {
    @JsonProperty
    private String openpenalty;
    @JsonProperty
    private String citation;
    @JsonProperty
    private String importdate;
    @JsonProperty
    private String description;
    @JsonProperty
    private String balance;
    @JsonProperty
    private String openfine;
    @JsonProperty
    private String expmm;
    @JsonProperty
    private String expyy;
    @JsonProperty
    private String location;
    @JsonProperty
    private String tag;
    @JsonProperty
    private String state;
    @JsonProperty
    private String make;
    @JsonProperty
    private String violdate;
    @JsonProperty
    private String violcode;
    @JsonProperty
    private String violfine;
    @JsonProperty
    private Location2 location_2;
    @JsonProperty
    private String neighborhood;
    @JsonProperty
    private String councildistrict;
    @JsonProperty
    private String noticedate;
    @JsonProperty
    private String policedistrict;
    @JsonIgnore
    private GoogleResults googleResults;

    public static boolean isCoordinatePresent(ParkingCitation parkingCitation) {

        if (null == parkingCitation.getLocation_2()) {
            return false;
        }
        return parkingCitation.getLocation_2().getLatitude() != null || parkingCitation.getLocation_2().getLongitude() != null;
    }

    @Override
    public String getLatitude() {
        return location_2 != null ? location_2.getLatitude() : null;
    }

    @Override
    public String getLongitude() {
        return location_2 != null ? location_2.getLongitude() : null;
    }

    @Override
    public String getAddress() {
        return location;
    }


    /**
     * Created by paul on 19.08.18.
     */
    @Data
    public static class GeoCode {

        private String politicalNeighborhood;
        private String streetNumber;
        private String route;
        private String postalCode;
        private String postalCodeSuffix;
        private String nearbyPointOfInterests;
    }
}
