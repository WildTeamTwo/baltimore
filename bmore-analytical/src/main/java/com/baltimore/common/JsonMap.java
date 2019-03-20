package com.baltimore.common;

import com.baltimore.model.GoogleResults;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

/**
 * Created by paul on 17.08.18.
 */
public class JsonMap {

    public static <T extends Object> T mapJsonToCitation(String json, Class<T> clazz) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();

        return objectMapper.readValue(json, clazz);

    }

    public static GoogleResults map(String response, String latitude, String longitude) throws IOException {
        try {
            return JsonMap.mapJsonToCitation(response, GoogleResults.class);
        } catch (IOException e) {
            e.addSuppressed(new IOException(String.format("Failed to serialize response for %s %s. Exception  %s", latitude, longitude, e.getMessage())));
            throw e;
        }
    }

}
