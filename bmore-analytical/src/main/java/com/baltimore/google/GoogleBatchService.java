package com.baltimore.google;

import com.baltimore.model.GoogleResults;
import com.baltimore.model.Googleable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.baltimore.common.Configuration.GOOGLE_API_REST_TIME;
import static com.baltimore.common.Configuration.GOOGLE_CALL_LIMIT;

/**
 * Created by paul on 19.09.18.
 */

@Component
public class GoogleBatchService {

    private GoogleClient google;
    private static final int RUNTIME_EXCEPTION_MAX = 5;
    private int GEOCODE_DAILY_CALL_COUNT = 1000;
    private String GEOCODE_CALL_COUNT_TIME_STAMP;
    private int GEOCODE_DAILY_CALL_MAX = 1000;

    public GoogleBatchService(@Autowired GoogleClient googleClient) {
        google = googleClient;
    }

    public void crossReferenceWithGoogle(List<? extends Googleable> elements)  {
        System.out.println(elements.size() != 0 ? String.format("\tGoogle Neighborhood. Police district. City Council Member. Analyzing %s police and city events...", elements.size()) : "\tBatch did not contain data required to cross reference. Skipping. ");

        List<Googleable> batch = new ArrayList<>();
        int runtimeExceptions = 0;
        System.out.print("\tSearching... ");

        for (int i = 0; i < elements.size(); i++) {
            try {
                GoogleResults response = callGoogle(elements.get(i));
                if (GoogleResponse.isOK(response)) {
                    Googleable citation = elements.get(i);
                    citation.setGoogleResults(response);
                    batch.add(citation);
                }
                restGoogleClient(batch.size());
                printProcessingSymbols(batch);
            } catch (IOException e) {
                e.printStackTrace();
                continue;
            } catch (InterruptedException e) {
                e.printStackTrace();
                continue;
            } catch (RuntimeException e) {
                if (runtimeExceptions >= RUNTIME_EXCEPTION_MAX) {
                    throw new RuntimeException(e);
                }
                runtimeExceptions++;
                continue;
            }
        }
        System.out.print("\n\tSave complete\n");
    }

    private GoogleResults callGoogle(final Googleable event) throws IOException {
        if (event.getLatitude() != null)
            return requestGeoCode(event.getLatitude(), event.getLongitude(), null);
        else
            return requestGeoCode(null, null, event.getAddress());
    }

    private GoogleResults requestGeoCode(String latitude, String longitude, String address) throws IOException {
        try {
            return google.requestGeocode(latitude, longitude, address);
        } catch (IOException e) {
            e.addSuppressed(new IOException(String.format("Google API - Encounterd error for inputs %s %s or %s. \n", latitude, longitude, address)));
        }
        return null;
    }

    private void restGoogleClient(int time) throws InterruptedException {
        if (time % GOOGLE_CALL_LIMIT == 0) {
            sleep(GOOGLE_API_REST_TIME);
        }
    }

    private void sleep(long time) throws InterruptedException {
        Thread.sleep(time);
    }

    private void printProcessingSymbols(List<Googleable> batch) {
        if (batch.size() % 50 == 0) {
            System.out.print("\u25A1\u25A0");
        }
    }

    private void calculateGoogleGeoCodeCallMax() {
        // get daily call count
        // get timestamp
        /* if ( timestamp isToday && DAILY_CALL_COUNT < DAILY_CALL_MAX  ){
                return DAILY_CALL_MAX - DAILY_Call_Count;
            else
                return -1;

         */
    }
}
