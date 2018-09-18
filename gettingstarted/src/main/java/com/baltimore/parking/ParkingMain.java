package com.baltimore.parking;

import com.baltimore.common.data.GoogleResults;
import com.baltimore.common.data.ParkingCitation;
import com.baltimore.google.GoogleClient;
import com.baltimore.google.GoogleResponse;
import com.baltimore.parking.reader.BatchCitationReader;
import com.baltimore.parking.writer.WriterController;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

import static com.baltimore.common.Configuration.CITATION_BATCH_MAX;
import static com.baltimore.common.Configuration.GOOGLE_API_REST_TIME;
import static com.baltimore.common.Configuration.GOOGLE_CALL_LIMIT;
import static com.baltimore.common.Configuration.PARKING_HOME;
import static com.baltimore.common.Configuration.PERSIST_BATCH_MAX;


/**
 * Created by paul on 13.08.18.
 * <p>
 * Attempts to clean parking data by cross-referencing it with google geocode to complete data picture. Results are persisted to file.
 */
public class ParkingMain {

    private BatchCitationReader batchCitationReader;
    private WriterController writer;
    private GoogleClient google;
    private final int BATCH_MAX;
    private final int EXCEPTION_MAX = 10;
    private static final Predicate<ParkingCitation> NEIGHBORHOOD_PRESENT = p -> (p.getNeighborhood() != null);
    private static final Predicate<ParkingCitation> POLICE_DISCTRICT_PRESENT = p -> (p.getPolicedistrict() != null);
    private static final Predicate<ParkingCitation> LOCATION_MISSING = p -> (p.getLocation_2() == null && p.getLocation() == null);
    private Map<String, String> violations = new HashMap<>();


    public void start() throws IOException {
        List<ParkingCitation> parkingCitations;
        markTime("Program start time");
        try {
            initResources();
            int count = 0, exceptionCount = 0;
            while (count < BATCH_MAX && exceptionCount < EXCEPTION_MAX) {
                try {
                    parkingCitations = loadCitationBatch(count, BATCH_MAX);
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

        markTime("Program end time");
    }

    private void doProcessBatch(final List<ParkingCitation> batch) throws IOException {
        crossReferenceWithGoogle(batch);
    }

    private void persistBatch(List<ParkingCitation> parkingCitationsWithGoogleGeoCode) throws IOException {
        try {
            System.out.print("\u25A1\u25A0");
            writer.addToFile(parkingCitationsWithGoogleGeoCode);
        } catch (IOException e) {
            e.addSuppressed(new IOException("Encountered unexpected error while persisting data."));
        }
    }

    private List<ParkingCitation> crossReferenceWithGoogle(List<ParkingCitation> citations) throws IOException {
        citations.removeIf(NEIGHBORHOOD_PRESENT);
        citations.removeIf(POLICE_DISCTRICT_PRESENT);
        citations.removeIf(LOCATION_MISSING);
        System.out.println(citations.size() != 0 ? String.format("\tCombine baltimore data w/Google [%s parking citations]...", citations.size()) : "\tBatch did not contain data required to cross reference. Skipping. ");
        List<ParkingCitation> batch = new ArrayList<>();
        System.out.print("\tGoogle linking ");
        for (int i = 0; i < citations.size(); i++) {

            try {
                trackViolations(citations.get(i));
                GoogleResults response = callGoogle(citations.get(i));
                if (GoogleResponse.isOK(response)) {
                    ParkingCitation citation = citations.get(i);
                    citation.setGoogleResults(response);
                    batch.add(citation);
                }
                boolean isLastBatch = i + 1 == citations.size();
                restGoogleClient(batch.size());
                persistBatchIfReady(batch, isLastBatch);

            } catch (IOException e) {
                e.printStackTrace();
                continue;
            } catch (InterruptedException e) {
                e.printStackTrace();
                continue;
            }

        }
        System.out.print("\n\tPersists complete\n.");
        return citations;
    }

    private void trackViolations(ParkingCitation citation) {
        violations.put(citation.getViolcode(), citation.getDescription());
    }

    private void persistBatchIfReady(List<ParkingCitation> batch, boolean isLastBatch) throws IOException, InterruptedException {
        if (batch.size() == PERSIST_BATCH_MAX || isLastBatch) {
            persistBatch(batch);
            batch.clear();
        }
    }

    private GoogleResults callGoogle(final ParkingCitation citation) throws IOException {
        if (citation.getLocation_2() != null)
            return requestGeoCode(citation.getLocation_2().getLatitude(), citation.getLocation_2().getLongitude(), citation.getLocation());
        else
            return requestGeoCode(null, null, citation.getLocation());
    }

    private void restGoogleClient(int time) throws InterruptedException {
        if (time % GOOGLE_CALL_LIMIT == 0) {
            sleep(GOOGLE_API_REST_TIME);
        }
    }

    private void sleep(long time) throws InterruptedException {
        Thread.sleep(time);
    }

    private GoogleResults requestGeoCode(String latitude, String longitude, String address) throws IOException {
        try {
            return google.requestGeocode(latitude, longitude, address);
        } catch (IOException e) {
            e.addSuppressed(new IOException(String.format("Google API - Encounterd error for coordinates %s and %s. \n", latitude, longitude)));
        }
        return null;
    }

    private ParkingMain(int batch_max) {
        BATCH_MAX = batch_max > 0 ? batch_max : CITATION_BATCH_MAX;
    }

    private static void markTime(String msg) {
        System.out.printf("\n%s %s\n\n", msg, DateTime.now(DateTimeZone.UTC));

    }

    public static ParkingMain init(int batch_max) {
        return new ParkingMain(batch_max);
    }

    private void initResources() throws IOException {
        batchCitationReader = BatchCitationReader.init();
        writer = WriterController.init();
        google = GoogleClient.init();
    }

    private List<ParkingCitation> loadCitationBatch(final int current, final int total) throws IOException {
        System.out.printf("Reading citations batch %s of %s...\n", current + 1, total);
        return batchCitationReader.loadCitationBatch();
    }

    public static void main(String[] args) throws Exception {
        ParkingMain.init(0).start();
    }

}
