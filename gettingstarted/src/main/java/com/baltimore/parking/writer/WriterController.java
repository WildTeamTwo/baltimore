package com.baltimore.parking.writer;

import com.baltimore.common.Configuration;
import com.baltimore.common.Filezee;
import com.baltimore.common.data.GeoCode;
import com.baltimore.common.data.GoogleResults;
import com.baltimore.common.data.ParkingCitation;

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
    private static final String OUT_FILE = "geocode";
    private static final String ERROR_FILE = "error";
    private final static Path outDir;
    private final static Path resultsFile;
    private final static Path errorFile;
    private final static Path errorDir;

    private Writer writer;
    private Writer errWriter;

    private WriterController(){

    }

    static {
        outDir = Configuration.GEOCODE_HOME.resolve(DIRECTORY);
        errorDir = Configuration.GEOCODE_HOME.resolve(ERROR_DIRECTORY);
        resultsFile = outDir.resolve(OUT_FILE);
        errorFile = errorDir.resolve(ERROR_FILE);
    }

    public static WriterController init() throws IOException{
        WriterController writerController = new WriterController();
        writerController.initFileSystem();
        return writerController;
    }

    public void addToFile(List<ParkingCitation> parkingCitations) throws IOException {
        initWriter();
        writeToFile(parkingCitations);
    }

    private void writeToFile(List<ParkingCitation> parkingCitations) throws IOException {

        for (ParkingCitation parkingCitation : parkingCitations) {
            try {
                String formatted = format(parkingCitation);
                writeToFile(formatted.getBytes());
            } catch (Exception e) {
                errWriter.writeToFile(String.format("Parking Citation %s - %s ", parkingCitation.getCitation(), Arrays.toString(e.getStackTrace())).getBytes() );
                continue;
            }
        }

        writer.closeOutputStream();
        errWriter.closeOutputStream();
    }


    private void writeToFile(final byte[] line_of_data) throws IOException {
        writer.writeToFile(line_of_data);
    }

    private String format(final ParkingCitation parkingCitation) {
        final GeoCode geo = initGeoCode(parkingCitation.getGoogleResults());
        final String tab = "\t";

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
