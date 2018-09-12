package com.baltimore.parking;

import com.baltimore.common.data.GoogleResults;
import com.baltimore.common.data.ParkingCitation;
import com.baltimore.google.GoogleClient;
import com.baltimore.parking.reader.BatchCitationReader;
import com.baltimore.parking.writer.WriterController;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import static com.baltimore.common.Configuration.ACTIVATE_GOOGLE_CALL;
import static com.baltimore.common.Configuration.PARKING_HOME;
import static com.baltimore.common.Configuration.PERSIST_BATCH_MAX;
import static com.baltimore.common.Configuration.GOOGLE_CALL_LIMIT;

/**
 * Created by paul on 13.08.18.
 * <p>
 * Attempts to clean parking data by cross-referencing it with google geocode to complete missing fields. Results are persisted to file.
 */
public class ParkingMain {

    private BatchCitationReader batchCitationReader;
    private WriterController writer;
    private GoogleClient google;
    private final int BATCH_MAX;
    private final int EXCEPTION_MAX = 10;
    private static final Predicate<ParkingCitation> NEIGHBORHOOD_PRESENT = p -> (p.getNeighborhood() != null);
    private static final Predicate<ParkingCitation> POLICE_DISCTRICT_PRESENT = p -> (p.getPolicedistrict() != null);
    private static final Predicate<ParkingCitation> COORDINATES_MISSING = p -> (p.getLocation_2() == null || (p.getLocation_2().getLatitude() == null || p.getLocation_2().getLongitude() == null));

    public void start() throws IOException {
        List<ParkingCitation> parkingCitations;

        try {
            initResources();
            int count = 0;
            int exceptionCount = 0;
            while (count < BATCH_MAX && exceptionCount < EXCEPTION_MAX) {
                try {

                    parkingCitations = loadCitationBatch();
                    doProcessBatch(parkingCitations);
                    System.out.println("\tBatch complete.");
                } catch (IOException e) {
                    e.printStackTrace();
                    exceptionCount++;
                }
                count++;

            }
        } catch (FileNotFoundException e) {
            e.addSuppressed(new FileNotFoundException(String.format("Input files not found in expected location %s. Please add Baltimore API Parking data to this directory.", PARKING_HOME.toAbsolutePath().toString())));
        }

    }

    private void doProcessBatch(final List<ParkingCitation> batch) throws IOException {
        crossReferenceWithGoogle(batch);
    }

    private void persistBatch(List<ParkingCitation> parkingCitationsWithGoogleGeoCode) throws IOException {
        try {
            System.out.print("..");
            writer.addToFile(parkingCitationsWithGoogleGeoCode);
        } catch (IOException e) {
            e.addSuppressed(new IOException("Encountered unexpected error while persisting data."));
        }
    }

    private List<ParkingCitation> crossReferenceWithGoogle(List<ParkingCitation> citations) throws IOException {
        citations.removeIf(NEIGHBORHOOD_PRESENT);
        citations.removeIf(POLICE_DISCTRICT_PRESENT);
        citations.removeIf(COORDINATES_MISSING);
        System.out.println(citations.size() != 0 ? String.format("\tCross Referencing baltimore parking batch w/Google location data [%s records]...", citations.size()) : "\tBatch did not contain data required to cross reference. Skipping. ");
        List<ParkingCitation> batch = new ArrayList<>();
        System.out.print("\tPersisting.");
        for (int i = 0; i < citations.size(); i++) {

            try {
                GoogleResults response = callGoogle(citations.get(i));

                if(isResponseOK(response)) {
                    ParkingCitation citation = citations.get(i);
                    citation.setGoogleResults(response);
                    batch.add(citation);
                }
                boolean isLastBatch = i + 1 == citations.size() ;
                pause(batch.size());
                persistBatchIfReady(batch, isLastBatch);

            } catch (IOException e) {
                e.printStackTrace();
                continue;
            }
            catch (InterruptedException e){
                e.printStackTrace();
                continue;
            }

        }
        System.out.print("\n\tPersists complete\n.");
        return citations;
    }

    private void persistBatchIfReady(List<ParkingCitation> batch, boolean isLastBatch) throws IOException, InterruptedException{
        if(batch.size() == PERSIST_BATCH_MAX || isLastBatch ) {
            persistBatch(batch);
            batch.clear();
            sleep(1500l);
        }
    }
    private GoogleResults callGoogle(final ParkingCitation citation) throws IOException {
        return requestGeoCode(citation.getLocation_2().getLatitude(), citation.getLocation_2().getLongitude());
    }

    private void pause(int time) throws InterruptedException{
        if(time % GOOGLE_CALL_LIMIT == 0 ){
            sleep(2500l);
        }
    }
    private void sleep(long time) throws InterruptedException{
        Thread.sleep(time);
    }
    private static boolean isResponseOK(GoogleResults results){

        if(results == null) {
            return false;
        }
        switch(results.getStatus()){
            case "OVER_QUERY_LIMIT" : throw new RuntimeException(results.getError_message());
            case "REQUEST_DENIED" : throw new RuntimeException(results.getError_message());
            case "OVER_DAILY_LIMIT" : throw new RuntimeException(results.getError_message());
            case "UNKNOWN_ERROR" : throw new RuntimeException(results.getError_message());
            case "OK" : return true;
            case "ZERO_RESULTS" : return true;
            default:throw new RuntimeException(String.format("Unknown google response status [%s] - Error message [ %s ]", results.getStatus(), results.getError_message()));
        }
    }
    private GoogleResults requestGeoCode(String latitude, String longitude) throws IOException {
        try {
            if (ACTIVATE_GOOGLE_CALL) {
                return google.requestGeocode(latitude, longitude);
            }
        } catch (IOException e) {
            e.addSuppressed(new IOException(String.format("Google API - Encounterd error for coordinates %s and %s. \n", latitude, longitude)));
        }
        return null;
    }

    private ParkingMain(int batch_max) {
        BATCH_MAX = batch_max > 0 ? batch_max : 20;
    }

    public static ParkingMain init(int batch_max) {
        return new ParkingMain(batch_max);
    }

    private void initResources() throws IOException {
        batchCitationReader = BatchCitationReader.init();
        writer = WriterController.init();
        google = GoogleClient.init();
    }

    private List<ParkingCitation> loadCitationBatch() throws IOException{
        System.out.println("Reading batch of citations...");
        return batchCitationReader.loadCitationBatch();
    }
    public static void main(String[] args) throws Exception {
        ParkingMain.init(0).start();
    }

}
