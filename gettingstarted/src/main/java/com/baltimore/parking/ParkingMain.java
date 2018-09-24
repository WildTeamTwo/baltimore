package com.baltimore.parking;

import com.baltimore.common.data.ParkingCitation;
import com.baltimore.google.GoogleBatch;
import com.baltimore.parking.reader.BatchCitationReader;
import com.baltimore.parking.writer.WriterController;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.function.Predicate;

import static com.baltimore.common.Configuration.CITATION_BATCH_MAX;
import static com.baltimore.common.Configuration.PARKING_HOME;


/**
 * Created by paul on 13.08.18.
 * <p>
 * Attempts to clean parking data by cross-referencing it with google geocode to complete data picture. Results are persisted to file.
 */
public class ParkingMain {

    private BatchCitationReader batchCitationReader;

    private GoogleBatch googleBatch;
    private final int BATCH_MAX;
    private final int EXCEPTION_MAX = 10;
    private WriterController writer;
    private static final Predicate<ParkingCitation> NEIGHBORHOOD_PRESENT = p -> (p.getNeighborhood() != null);
    private static final Predicate<ParkingCitation> POLICE_DISCTRICT_PRESENT = p -> (p.getPolicedistrict() != null);
    private static final Predicate<ParkingCitation> LOCATION_MISSING = p -> (p.getLocation_2() == null && p.getLocation() == null);

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
                    System.out.println("\tBatch complete");
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
        batch.removeIf(NEIGHBORHOOD_PRESENT);
        batch.removeIf(POLICE_DISCTRICT_PRESENT);
        batch.removeIf(LOCATION_MISSING);
        googleBatch.crossReferenceWithGoogle(batch);
        persistBatch(batch);
    }

    private void persistBatch(final List<ParkingCitation> elements) throws IOException {
        try {
            writer.addToFile(elements);
            elements.clear();

        } catch (IOException e) {
            e.addSuppressed(new IOException("Encountered unexpected error while persisting data."));
        }
    }

    private static void markTime(String msg) {
        System.out.printf("\n%s %s\n\n", msg, DateTime.now(DateTimeZone.UTC));
    }

    public static ParkingMain init(int batch_max) {
        return new ParkingMain(batch_max);
    }

    private ParkingMain(int batch_max) {
        BATCH_MAX = batch_max > 0 ? batch_max : CITATION_BATCH_MAX;
    }

    private void initResources() throws IOException {
        writer = WriterController.init();
        batchCitationReader = BatchCitationReader.init();
        googleBatch = GoogleBatch.init();
    }

    private List<ParkingCitation> loadCitationBatch(final int current, final int total) throws IOException {
        System.out.printf("Reading citations batch %s of %s...\n", current + 1, total);
        return batchCitationReader.loadCitationBatch();
    }

    public static void main(String[] args) throws Exception {
        ParkingMain.init(0).start();
    }

}
