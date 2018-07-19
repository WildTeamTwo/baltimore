package com.baltimore;

import java.util.Arrays;
import java.util.List;

/**
 * Created by paul on 17.07.18.
 */
public class BaltimoreAPI {

    private static final CacheStore cache = new CacheStore();
    private static final API api = new API();

    public void downloadResources() throws Exception {
        List<Resource> resourcesToDownload = cache.uncachedResources();
        if (resourcesToDownload.size() > 0) {
            System.out.println("Some cache buckets were empty. updating now...");
            download(resourcesToDownload);
        }
        else{
            System.out.println("Cache buckets already contain data. Skip initialization");
        }
    }

    public void downloadResource(Resource resource) throws Exception {
        download(Arrays.asList(resource));
    }

    private void download(List<Resource> downloadQueue) {
        for (Resource resource : downloadQueue) {
            try {
                String response = api.readLive(resource);
                cache(response, resource);
                apiRest();
            } catch (Exception e) {
                System.err.printf("Unexpected Exception. Skipping resource %s", resource.name());
                e.printStackTrace(System.err);
                continue;
            }
        }

    }

    private synchronized void apiRest() throws Exception {
        wait(5000L);
    }

    private void cache(String response, Resource resource) {
        try {
            cache.store(response, resource);
        } catch (Exception e) {
            System.err.printf("Encountered failure when caching resource s%", resource.name());
            e.printStackTrace(System.err);
        }
    }

}
