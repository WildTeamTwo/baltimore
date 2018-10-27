package com.baltimore.subprogram.download;

import com.baltimore.common.Resource;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * Created by paul on 17.07.18.
 */
public class BaltimoreAPI {

    private static final CacheStore cache = new CacheStore();
    private static final API api = new API();
    private final int pageLimit;

    private BaltimoreAPI(){
        pageLimit = 1000;
    }

    private BaltimoreAPI(int pageLimit){
        this.pageLimit = pageLimit;
    }

    public static BaltimoreAPI init(){
        return new BaltimoreAPI();
    }

    public static BaltimoreAPI init(int pageLimit){
        return new BaltimoreAPI(pageLimit);
    }

    public void downloadAllResources() throws Exception {
        download(Arrays.asList(Resource.values()));
    }

    public void downloadResource(Resource resource) throws Exception {
        download(Arrays.asList(resource));
    }

    private void download(List<Resource> downloadQueue) {
        final int limit = pageLimit;
        String response = null;
        Integer offset = 1;
        for (Resource resource : downloadQueue) {
            try {
                System.out.printf("\n Downloading %s data. Maximum of %s pages...\n", resource.name(), pageLimit);
                for (int page = 1; page <= pageLimit; page++) {
                    System.out.printf("Downloading page %s ...\n", Integer.toString(page));
                    response = api.readLive(resource, offset.toString(), null);
                    if (response == null) {
                        break;
                    }
                    cache(response, resource, page);
                    offset = page * limit + 1;
                    apiRest();
                }

            } catch (Exception e) {
                System.err.printf("Unexpected exception. Skipping resource %s\n", resource.name());
                e.printStackTrace(System.err);
                continue;
            }
        }

    }

    private synchronized void apiRest() throws Exception {
        wait(5000L);
    }

    private void cache(String response, Resource resource, int page) {
        try {
            cache.store(response, resource, new Integer(page).toString());
        } catch (IOException e) {
            System.err.printf("Encountered failure when caching resource %s\n", resource.name());
            e.printStackTrace(System.err);
        }
    }

}
