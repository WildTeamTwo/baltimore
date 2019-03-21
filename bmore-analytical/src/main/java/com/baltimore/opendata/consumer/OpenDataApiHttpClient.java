package com.baltimore.opendata.consumer;

import com.baltimore.common.Resource;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Optional;

/**
 * Created by paul on 18.07.18.
 */
@Component
public class OpenDataApiHttpClient {

    private static final String DEFAULT_LIMIT = "1000";

    private static HttpGet buildUrl(String resource, String offset, String limit, String orderBy) throws URISyntaxException {
        URIBuilder builder = new URIBuilder(resource);

        if (Optional.ofNullable(orderBy).isPresent())
            builder.setParameter("$order", orderBy + " DESC");

        if (Optional.ofNullable(offset).isPresent())
            builder.setParameter("$offset", offset);

        if (Optional.ofNullable(limit).isPresent())
            builder.setParameter("$limit", limit);

        return new HttpGet(builder.build());
    }

    public String readLive(Resource resource, String offset, String limit) throws IOException, URISyntaxException {
        if(Strings.isEmpty(resource.url) || Strings.isEmpty(offset)){
            return null;
        }
        HttpClient client = HttpClientBuilder.create().build();
        HttpGet request = buildUrl(resource.url, offset, limit, resource.orderby);
        HttpResponse response = client.execute(request);
        if (response.getStatusLine().getStatusCode() != 200) {
            System.err.printf("\n Received status code %d. \n See response \n %s",
                    response.getStatusLine().getStatusCode(),
                    EntityUtils.toString(response.getEntity()));
            return null;
        }
        String responseStr = EntityUtils.toString(response.getEntity());

        EntityUtils.consume(response.getEntity());
        return responseStr;

    }


}
