package com.baltimore.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * Created by paul on 15.08.18.
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Location2 {
    @JsonProperty
    private String latitude;
    @JsonProperty
    private String longitude;
    @JsonProperty
    private String needs_recoding;
}
