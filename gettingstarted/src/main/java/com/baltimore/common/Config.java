package com.baltimore.common;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created by paul on 14.08.18.
 */
public class Config {

    public static final Path PROJECT_HOME;
    public static final Path PARKING_HOME;
    public static final String VERSION = "2.0";
    public static final int CITATION_LIMIT = 50;
    public static final long API_WAIT_TIME = 30000;
    public static final boolean ACTIVATE_GOOGLE_CALL = true;
    public static final int GOOGLE_CALL_LIMIT = 10;

    static {

        Path HOME = Paths.get(System.getProperty("user.home"));
        PROJECT_HOME = HOME.resolve("bmore/open");
        PARKING_HOME =  PROJECT_HOME.resolve("parkingcitations").resolve(VERSION);
    }

}
