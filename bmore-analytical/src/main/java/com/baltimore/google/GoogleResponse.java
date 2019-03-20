package com.baltimore.google;

import com.baltimore.model.GoogleResults;

/**
 * Created by paul on 13.09.18.
 */
public class GoogleResponse {

    public static boolean isOK(GoogleResults results) {

        if (results == null) {
            return false;
        }
        switch (results.getStatus()) {
            case "OVER_QUERY_LIMIT":
                throw new RuntimeException(results.getError_message());
            case "REQUEST_DENIED":
                throw new RuntimeException(results.getError_message());
            case "OVER_DAILY_LIMIT":
                throw new RuntimeException(results.getError_message());
            case "UNKNOWN_ERROR":
                throw new RuntimeException(results.getError_message());
            case "OK":
                return true;
            case "ZERO_RESULTS":
                return true;
            default:
                throw new RuntimeException(String.format("unknown google response status [%s] - Error message [ %s ]", results.getStatus(), results.getError_message()));
        }
    }
}
