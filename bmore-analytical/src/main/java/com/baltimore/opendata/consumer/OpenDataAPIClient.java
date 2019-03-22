package com.baltimore.opendata.consumer;

import com.baltimore.common.Resource;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.LinkedList;

/**
 * Created by paul on 17.07.18.
 */
public class OpenDataAPIClient {

    private OpenDataApiHttpClient httpClient;
    private static final int PAGE_MAX = 1000;
    private static final int PAGE_DEFAULT = 10;
    private static final Integer RESULT_LIMIT = 1000;
    private final int pages;

    public OpenDataAPIClient() {
        this(PAGE_DEFAULT);
    }

    public OpenDataAPIClient(int pages) {
        if (pages <= 0 || pages > PAGE_MAX) {
            throw new IllegalArgumentException("Invalid page count");
        }
        this.pages = pages;
        this.httpClient = new OpenDataApiHttpClient();
    }

    public LinkedList<String> download(Resource resource) throws Exception {
        System.out.printf("Attepming %s dataset download from Baltimore Open Data API\n", resource.name());
        String response;
        Integer offset = 0;
        LinkedList<String> responses = new LinkedList<>();  //LinkedList chosen for queue FIFO ability - elements are retrieved in order they were constructed and stored.
        for (int page = 1; page <= pages; page++) {
            System.out.printf("Page %d of %d ...\n", page, pages);
            response = httpClient.readLive(resource, offset.toString(), RESULT_LIMIT.toString());
            if (response == null) {
                continue;
            }
            responses.add(response);
            offset = offset + RESULT_LIMIT;
        }
        return responses;
    }

    public String download(Resource resource, Integer offset, Integer limit) throws Exception {
        String response = httpClient.readLive(resource, offset.toString(), limit.toString());
        if (response == null) {
           return null;
        }
        return response;
    }



}
