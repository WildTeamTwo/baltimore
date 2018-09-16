package com.baltimore.common;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created by paul on 14.08.18.
 */
public class Configuration {

    public static final Path PROJECT_HOME;
    public static final Path PARKING_HOME;
    public static final Path GEOCODE_HOME;
    public static final String VERSION = "2.0";
    public static final boolean ACTIVATE_GOOGLE_CALL = true;
    public static final int CITATION_BATCH_MAX = 35;
    public static final int PERSIST_BATCH_MAX = 75;
    public static final int GOOGLE_CALL_LIMIT = 40; //positive non-prime number.
    public static final int FUZZY_SEARCH_MINIMUM_CONFIDENCE_SCORE = 75;

    static {

        Path HOME = Paths.get(System.getProperty("user.home"));
        PROJECT_HOME = HOME.resolve("bmore/open");
        PARKING_HOME = createStandardPath("parkingcitations");
        GEOCODE_HOME = createStandardPath("geocode");

    }

    private static Path createStandardPath(String dir){
        return PROJECT_HOME.resolve(dir).resolve(VERSION);
    }

}
