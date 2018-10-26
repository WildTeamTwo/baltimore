package com.baltimore.common.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Arrest {

    @JsonProperty
    String arrest;
    @JsonProperty
    int age;
    @JsonProperty
    String sex;
    @JsonProperty
    String race;
    @JsonProperty
    String arrestdate;
    @JsonProperty
    String arresttime;
    @JsonProperty
    String arrestlocation;
    @JsonProperty("incidento")
    String incidentoffense;
    @JsonProperty("incidentl")
    String incidentlocation;
    @JsonProperty
    String charge;
    @JsonProperty
    String chargedescription;
    @JsonProperty
    String district;
    @JsonProperty
    int post;
    @JsonProperty
    String neighborhood;
    @JsonProperty
    Double longitude;
    @JsonProperty
    Double latitude;
    @JsonProperty("location_1")
    CrimeLocation location1;
    @JsonProperty("name1")
    String locationName;
    @JsonProperty(":@computed_region_5kre_ccpb")
    String computedRegion_ccpb;
    @JsonProperty(":@computed_region_gwq4_fjxs")
    String computedRegion_fjxs;
    @JsonProperty(":@computed_region_s6p5_2pgr")
    String computedRegion_2pgr;

}
