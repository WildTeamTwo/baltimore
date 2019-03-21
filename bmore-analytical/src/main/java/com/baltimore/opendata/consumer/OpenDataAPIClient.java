package com.baltimore.opendata.consumer;

import com.baltimore.common.Resource;
import com.baltimore.persistence.FileSystemStore;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * Created by paul on 17.07.18.
 */
@Component
public class OpenDataAPIClient {

    private FileSystemStore fileSystem;
    private OpenDataApiHttpClient httpClient;
    private static final int PAGE_MAX=1000;
    private static final int PAGE_DEFAULT=5;
    private final int pageLimit;

    public OpenDataAPIClient(){
        this(PAGE_DEFAULT);
    }

    public OpenDataAPIClient(int pages){
        if(pages <= 0 || pages > PAGE_MAX)
        {
            throw new IllegalArgumentException("Invalid page count");
        }
        pageLimit = pages;
        fileSystem = new FileSystemStore();
        httpClient = new OpenDataApiHttpClient();
    }

    public void downloadResource(Resource resource) throws Exception {
        download(Arrays.asList(resource));
    }

    private void download(List<Resource> resources) {
        final int limit = PAGE_MAX;
        String response;
        Integer offset = 1;
        for (Resource resource : resources) {
            try {
                System.out.printf("Attepming %s dataset download from Baltimore Open Data API\n", resource.name());
                for (int page = 1; page <= pageLimit; page++) {
                    System.out.printf("Page %d of %d ...\n", page, pageLimit);
                    response = httpClient.readLive(resource, offset.toString(), null);
                    if (response == null) {
                        break;
                    }
                    store(response, resource, page);
                    offset = page * limit + 1;
                    apiRest();
                }

            } catch (Exception e) {
                System.out.printf("Unexpected exception. Skipping resource %s\n", resource.name());
                e.printStackTrace(System.err);
                continue;
            }
        }

    }

    private synchronized void apiRest() throws Exception {
        wait(5000L);
    }

    private void store(String response, Resource resource, int page) {
        try {
            fileSystem.store(response, resource, new Integer(page).toString());
        } catch (IOException e) {
            System.out.printf("Encountered failure when caching resource %s\n", resource.name());
        }
    }

}
