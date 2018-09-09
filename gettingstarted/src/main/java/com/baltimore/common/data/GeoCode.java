package com.baltimore.common.data;

import lombok.Data;

/**
 * Created by paul on 20.08.18.
 */
@Data
public class GeoCode {
    private String streetNumber;
    private String route;
    private String politicalNeighborhood;
    private String postalCode;
    private String postalCodeSuffix;
    private String nearbyPointOfInterests;

    private GeoCode(){

    }

    public static GeoCode init(){
        return new GeoCode();
    }
    public static GeoCode toGeoCode(GoogleResults.GoogleResult [] results){
        GeoCode geoCode = init();
        if(results == null)
        {
            return null;
        }

        for(GoogleResults.GoogleResult result : results){
            for( AddressComponent c : result.getAddress_components()){
                if ( AddressComponent.isPoliticalNeighborhood(c)  ){
                    geoCode.setPoliticalNeighborhood(c.getLong_name());
                }
                if ( AddressComponent.isStreetNumber(c)  ){
                    geoCode.setPoliticalNeighborhood(c.getLong_name());
                }

                if ( AddressComponent.isRoute(c)  ){
                    geoCode.setPoliticalNeighborhood(c.getLong_name());
                }


                if ( AddressComponent.isPostalCode(c)  ){
                    geoCode.setPoliticalNeighborhood(c.getLong_name());
                }



                if ( AddressComponent.isPostalSuffix(c)  ){
                    geoCode.setPoliticalNeighborhood(c.getLong_name());
                }

                if ( AddressComponent.isNearbyPointOfInterest(c)  ){
                    geoCode.setPoliticalNeighborhood(c.getLong_name());
                }

            }
        }

        return geoCode;

    }
}
