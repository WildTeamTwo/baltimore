package com.baltimore.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import static com.baltimore.common.Util.arrayHas;

/**
 * Created by paul on 19.08.18.
 */
@Data
public class AddressComponent {

    @JsonProperty
    private String long_name;
    @JsonProperty
    private String short_name;
    @JsonProperty
    private String[] types;

    public static boolean isPoliticalNeighborhood(AddressComponent c) {
        return isAddressOfType(c, "neighborhood", "political");
    }

    public static boolean isStreetNumber(AddressComponent c) {
        return isAddressOfType(c, "street_number");
    }

    public static boolean isRoute(AddressComponent c) {
        return isAddressOfType(c, "route");
    }

    public static boolean isCity(AddressComponent c) {
        return isAddressOfType(c, "localtiy", "political");
    }

    public static boolean isPostalCode(AddressComponent c) {
        return isAddressOfType(c, "postal_code");
    }

    public static boolean isPostalSuffix(AddressComponent c) {
        return isAddressOfType(c, "postal_code_suffix");
    }

    public static boolean isNearbyPointOfInterest(AddressComponent c) {
        return isAddressOfType(c, "point_of_interest");
    }

    public static boolean isAddressOfType(AddressComponent component, String... keys) {
        return arrayHas(component.getTypes(), keys);
    }
}
