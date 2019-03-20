package com.baltimore.google;

import com.baltimore.model.GoogleResults;
import com.baltimore.model.Googleable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.baltimore.common.Configuration.GOOGLE_API_REST_TIME;
import static com.baltimore.common.Configuration.GOOGLE_CALL_LIMIT;

/**
 * Created by paul on 19.09.18.
 */

public class GoogleBatch {

    private GoogleClient google;

    private GoogleBatch() throws IOException {
        google = GoogleClient.init();
    }

    public static GoogleBatch init() throws IOException {
        return new GoogleBatch();
    }

    public void crossReferenceWithGoogle(List<? extends Googleable> elements) throws IOException {
        System.out.println(elements.size() != 0 ? String.format("\tGoogle Neighborhood. Police district. City Council Member. Analyzing %s police and city events...", elements.size()) : "\tBatch did not contain data required to cross reference. Skipping. ");

        List<Googleable> batch = new ArrayList<>();
        int run_time_exceptions_max = 5;
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
                if (run_time_exceptions_max >= 5) {
                    throw new RuntimeException(e);
                }
                run_time_exceptions_max++;
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

}
