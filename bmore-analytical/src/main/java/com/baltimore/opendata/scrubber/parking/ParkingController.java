package com.baltimore.opendata.scrubber.parking;

import com.baltimore.model.ParkingCitation;
import com.baltimore.google.GoogleBatchService;
import com.baltimore.opendata.Task;
import com.baltimore.opendata.scrubber.reader.BatchCitationReader;
import com.baltimore.opendata.scrubber.writer.BatchWriter;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.springframework.beans.factory.annotation.Autowired;

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
public class ParkingController implements Task {

    private static final Predicate<ParkingCitation> NEIGHBORHOOD_PRESENT = p -> (p.getNeighborhood() != null);
    private static final Predicate<ParkingCitation> POLICE_DISCTRICT_PRESENT = p -> (p.getPolicedistrict() != null);
    private static final Predicate<ParkingCitation> LOCATION_MISSING = p -> (p.getLocation_2() == null && p.getLocation() == null);
    private final int BATCH_MAX;
    private final int EXCEPTION_MAX = 10;
    private BatchCitationReader batchCitationReader;
    private GoogleBatchService googleBatchService;
    private BatchWriter batchWriter;

    public ParkingController(@Autowired BatchCitationReader batchReader, @Autowired GoogleBatchService googleBatchService, @Autowired BatchWriter batchWriter) {
        this(CITATION_BATCH_MAX);
        this.batchCitationReader = batchReader;
        this.googleBatchService = googleBatchService;
        this.batchWriter = batchWriter;
    }

    private ParkingController(int batch_max) {
        BATCH_MAX = batch_max > 0 ? batch_max : CITATION_BATCH_MAX;
    }

    @Override
    public String displayName() {
        return "Fix Parking Data. Find missing data (e.g Police District, City Council Member, Neighborhood, Zip Code, etc)";
    }

    public void start() throws IOException {
        markTime("Program start time");
        try {
            initResources();
            int count = 0, exceptionCount = 0;
            while (count < BATCH_MAX && exceptionCount < EXCEPTION_MAX) {
                try {
                    doProcessBatch(loadCitationBatch(count, BATCH_MAX));
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
        googleBatchService.crossReferenceWithGoogle(batch);
        persistBatch(batch);
    }

    private void persistBatch(final List<ParkingCitation> elements) throws IOException {
        try {
            batchWriter.addToFile(elements);
            elements.clear();
        } catch (IOException e) {
            e.addSuppressed(new IOException("Encountered unexpected error while persisting data."));
        }
    }

    private void initResources() throws IOException {
        batchWriter = BatchWriter.init();
        batchCitationReader = BatchCitationReader.init();
    }

    private List<ParkingCitation> loadCitationBatch(final int current, final int total) throws IOException {
        System.out.printf("Reading citations batch %s of %s...\n", current + 1, total);
        return batchCitationReader.loadCitationBatch();
    }

    private static void markTime(String msg) {
        System.out.printf("\n%s %s\n\n", msg, DateTime.now(DateTimeZone.UTC));
    }
}
