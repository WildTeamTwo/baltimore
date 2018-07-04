package com.baltimore;

import lombok.Data;

/**
 * Created by paul on 27.06.18.
 */
@Data
public class CrimeLocation {

    String type;
    String[] coordinates = new String[2];
}
