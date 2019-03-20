package com.baltimore.opendata.scrubber.parking.writer;

import com.baltimore.common.Configuration;
import com.baltimore.common.Filezee;
import com.baltimore.model.GeoCode;
import com.baltimore.model.GoogleResults;
import com.baltimore.model.Neighborhood;
import com.baltimore.model.ParkingCitation;
import com.baltimore.model.PoliceDistrict;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * Created by paul on 13.08.18.
 */
public class WriterController {

    private static final String DIRECTORY = "geocode/";
    private static final String ERROR_DIRECTORY = "error/";
    private static final String RESULTS_FILE = "geocode";
    private static final String ERROR_FILE = "error";
    private static final String EXTENSION = ".txt";
    private final static Path outDir;
    private final static Path resultsFile;
    private final static Path errorFile;
    private final static Path errorDir;
    private static DateTimeFormatter formatter;

    static {
        formatter = DateTimeFormat.forPattern("MMddYY-HHMM");
        outDir = Configuration.GEOCODE_HOME.resolve(DIRECTORY);
        errorDir = Configuration.GEOCODE_HOME.resolve(ERROR_DIRECTORY);
        resultsFile = outDir.resolve(fileName(RESULTS_FILE));
        errorFile = errorDir.resolve(fileName(ERROR_FILE));

    }

    private Writer writer;
    private Writer errWriter;

    private WriterController() {

    }

    public static WriterController init() throws IOException {
        WriterController writerController = new WriterController();
        writerController.initFileSystem();
        return writerController;
    }

    private static boolean isInBaltimore(GeoCode geoCode) {
        try {
            Integer zip = new Integer(geoCode.getPostalCode());
            return zip >= 21201 && zip <= 21298;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private static String fileName(String filename) {
        return appendTimeStamp(filename) + EXTENSION;
    }

    private static String appendTimeStamp(String filename) {
        return filename + "-" + formatter.print(DateTime.now());
    }

    public void addToFile(List<ParkingCitation> parkingCitations) throws IOException {
        initWriter();
        writeToFile(parkingCitations);
    }

    private void writeToFile(List<ParkingCitation> parkingCitations) throws IOException {

        for (ParkingCitation parkingCitation : parkingCitations) {
            try {
                final GeoCode geo = initGeoCode(parkingCitation.getGoogleResults());
                if (!isInBaltimore(geo)) {
                    //todo: create better filter
                    continue;
                }
                String formatted = format(geo, parkingCitation);
                writeToFile(formatted.getBytes());
            } catch (Exception e) {
                errWriter.writeToFile(String.format("Parking Citation %s - %s ", parkingCitation.getCitation(), Arrays.toString(e.getStackTrace())).getBytes());
                continue;
            }
        }

        writer.closeOutputStream();
        errWriter.closeOutputStream();
    }

    private void writeToFile(final byte[] line_of_data) throws IOException {
        writer.writeToFile(line_of_data);
    }

    private String format(final GeoCode geo, final ParkingCitation parkingCitation) {

        final String tab = "\t";
        PoliceDistrict policeDistrict = PoliceDistrict.unknown;
        String cityCouncil = null;
        if (geo.getPoliticalNeighborhood() != null) {
            policeDistrict = Neighborhood.policeDistrictOf(geo.getPoliticalNeighborhood());
            cityCouncil = Neighborhood.cityCouncilOf(geo.getPoliticalNeighborhood());
        }

        final String formatted = new StringBuilder(parkingCitation.getCitation()).
                append(tab).
                append(parkingCitation.getLocation_2().getLatitude()).
                append(tab).
                append(parkingCitation.getLocation_2().getLongitude()).
                append(tab).
                append(geo.getPoliticalNeighborhood()).
                append(tab).
                append(geo.getStreetNumber()).
                append(tab).
                append(geo.getRoute()).
                append(tab).
                append(geo.getPostalCode()).
                append(tab).
                append(geo.getPostalCodeSuffix()).
                append(tab).
                append(geo.getNearbyPointOfInterests()).
                append(tab).
                append(policeDistrict).
                append(tab).
                append(cityCouncil).
                append(tab).
                append(parkingCitation.getViolcode()).
                append(tab).
                append(parkingCitation.getDescription()).
                append(tab).
                append(parkingCitation.getVioldate()).
                append(tab).
                append(parkingCitation.getOpenfine()).
                append("\n").
                toString();

        return formatted;
    }

    private GeoCode initGeoCode(GoogleResults results) {
        if (Objects.isNull(results) || !results.getStatus().equalsIgnoreCase("OK")) {
            return GeoCode.init();
        }
        return GeoCode.toGeoCode(results.getResults());
    }

    private void initWriter() throws IOException {
        this.writer = new Writer().initWriter(resultsFile, true);
        this.errWriter = new Writer().initWriter(errorFile, true);
    }

    private void initFileSystem() throws IOException {
        Filezee.removeIfExist(resultsFile);
        Filezee.removeIfExist(errorFile);
        Filezee.createDir(outDir);
        Filezee.createDir(errorDir);
        Filezee.createFile(resultsFile);
        Filezee.createFile(errorFile);
    }

}
