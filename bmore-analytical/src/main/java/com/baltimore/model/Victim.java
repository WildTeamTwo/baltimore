package com.baltimore.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.joda.time.DateTime;

/**
 * Created by paul on 25.06.18.
 */
@Data
public class Victim {

    @JsonProperty
    DateTime CrimeDate;
    @JsonProperty
    String CrimeTime;
    @JsonProperty
    String CrimeCode;
    @JsonProperty
    String Location;
    @JsonProperty
    String Description;
    @JsonProperty
    String InsideOutside;
    @JsonProperty
    String Weapon;
    @JsonProperty
    int Post;
    @JsonProperty
    String District;
    @JsonProperty
    String Neighborhood;
    @JsonProperty
    Double Longitude;
    @JsonProperty
    Double Latitude;
    @JsonProperty
    String Location1;
    @JsonProperty
    String Premise;
    @JsonProperty
    int TotalIncidents;
}
