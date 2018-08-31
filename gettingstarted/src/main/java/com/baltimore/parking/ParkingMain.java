package com.baltimore.parking;

import com.baltimore.common.JsonMap;
import com.baltimore.common.data.GoogleResults;
import com.baltimore.common.data.ParkingCitation;
import com.baltimore.google.GeoCodeClient;
import com.baltimore.parking.reader.BatchCitationReader;
import com.baltimore.parking.writer.WriterController;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.function.Predicate;
import static com.baltimore.common.Config.ACTIVATE_GOOGLE_CALL;
import static com.baltimore.common.Config.GOOGLE_CALL_LIMIT;
import static com.baltimore.common.Config.PARKING_HOME;
/**
 * Created by paul on 13.08.18.
 */
public class ParkingMain {

    private BatchCitationReader batchCitationReader;
    private WriterController writer;
    private GeoCodeClient geo = new GeoCodeClient();
    private final int BATCH_MAX = 2;
    private final int EXCEPTION_MAX = 10;
    private static final Predicate<ParkingCitation> NEIGHBORHOOD_PRESENT = p -> (p.getNeighborhood() != null);
    private static final Predicate<ParkingCitation> POLICE_DISCTRICT_PRESENT = p -> (p.getPolicedistrict() != null);
    private static final Predicate<ParkingCitation> COORDINATES_MISSING = p -> (p.getLocation_2() == null || (p.getLocation_2().getLatitude() == null || p.getLocation_2().getLatitude() == null));

    public void begin() throws IOException {
        List<ParkingCitation> parkingCitations;

        try {
            initResources();
            int count = 0;
            int ioExceptionCount = 0;
            while (count < BATCH_MAX && ioExceptionCount < EXCEPTION_MAX) {
                try {

                    parkingCitations = batchCitationReader.loadCitationBatch();
                    doProcessBatch(parkingCitations);
                } catch (IOException e) {
                    e.printStackTrace();
                    ioExceptionCount++;
                }
                count++;

            }
        } catch (FileNotFoundException e) {
            e.addSuppressed(new FileNotFoundException(String.format("Input files not found in expected location %s. Please add Baltimore API Parking data to this directory.", PARKING_HOME.toAbsolutePath().toString())));
        }

    }

    private void doProcessBatch(final List<ParkingCitation> batch) throws IOException {
        List<ParkingCitation> parkingCitationsWithGoogleGeoCode = requestLocationDetails(batch);
        persistCitations(parkingCitationsWithGoogleGeoCode);
    }

    private void persistCitations(List<ParkingCitation> parkingCitationsWithGoogleGeoCode) throws IOException {
        try {
            writer.addToFile(parkingCitationsWithGoogleGeoCode);
        } catch (IOException e) {
            e.addSuppressed(new IOException("Aborting. Encountered unexpected error while persisting data."));
        }
    }

    private List<ParkingCitation> requestLocationDetails(List<ParkingCitation> citations) throws IOException {
        citations.removeIf(NEIGHBORHOOD_PRESENT);
        citations.removeIf(POLICE_DISCTRICT_PRESENT);
        citations.removeIf(COORDINATES_MISSING);

        for (int i = 0; i < citations.size() && i < GOOGLE_CALL_LIMIT; i++) {

            try {
                GoogleResults response = callGoogle(citations.get(i));
                citations.get(i).setGoogleResults(response);
            } catch (IOException e) {
                e.printStackTrace();
                continue;
            } catch (NullPointerException e) {
                e.printStackTrace();
                continue;
            }

        }
        return citations;
    }

    private GoogleResults callGoogle(final ParkingCitation citation) throws IOException {
        String response = requestGeoCode(citation.getLocation_2().getLatitude(), citation.getLocation_2().getLongitude());
        return response != null ? JsonMap.map(response, citation.getLocation_2().getLatitude(), citation.getLocation_2().getLongitude()) : null;
    }

    private String requestGeoCode(String latitude, String longitude) throws IOException {
        try {
            if (ACTIVATE_GOOGLE_CALL) {
                return geo.requestGeocode(latitude, longitude);
            }
        } catch (IOException e) {
            e.addSuppressed(new IOException(String.format("Google API - Encounterd error for coordinates %s and %s. \n", latitude, longitude)));
        }
        return null;
    }


    private void initResources() throws IOException {
        batchCitationReader = BatchCitationReader.init();
        writer = WriterController.init();
    }

    public static void main(String[] args) throws Exception {
        new ParkingMain().begin();
    }


}
