package com.baltimore.common.data;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * Created by paul on 19.08.18.
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class GoogleResults {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty
    private String error_message;
    @JsonProperty
    private GoogleResult[] results;
    @JsonProperty
    private String status;

    /**
     * Created by paul on 19.08.18.
     */
    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class GoogleResult {

        @JsonProperty("address_components")
        private AddressComponent[] address_components;
        @JsonProperty
        private String formatted_address;
        @JsonIgnore
        private Object geometry;
        @JsonIgnore
        private Object location;
        @JsonIgnore
        private Object location_type;
        @JsonIgnore
        private Object view_port;
        @JsonProperty
        private String place_id;
        @JsonProperty
        private String[] types;
        @JsonIgnore
        private Object plus_code;


    }
}
