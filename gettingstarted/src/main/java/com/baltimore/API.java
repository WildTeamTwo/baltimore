package com.baltimore;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Optional;

/**
 * Created by paul on 18.07.18.
 */
public class API {

    private static final String SPACE_URL_ENCODE = "%20";
    public String readLive(Resource resource) throws IOException, URISyntaxException {
       return readLive(resource.url,null,null,resource.orderby);
    }


    public String readLive(String resource, String offset, String limit, String orderBy) throws IOException, URISyntaxException {
        HttpClient client = HttpClientBuilder.create().build();
        HttpGet request = buildUrl(resource, offset, limit, orderBy);
        HttpResponse response = client.execute(request);
        if(response.getStatusLine().getStatusCode() != 200)
        {
            System.err.printf("\n Received status code %d. \n See response \n %s",
                    response.getStatusLine().getStatusCode(),
                    EntityUtils.toString(response.getEntity()));
        }
        String responseStr = EntityUtils.toString(response.getEntity());

        EntityUtils.consume(response.getEntity());
        return responseStr;

    }

    private static HttpGet buildUrl(String resource, String offset, String limit, String orderBy) throws URISyntaxException{
        URIBuilder builder = new URIBuilder(resource);

        if(Optional.ofNullable(orderBy).isPresent() )
            builder.setParameter("$order", orderBy + " DESC" );

        if(Optional.ofNullable(offset).isPresent() )
            builder.setParameter("$offset", offset );

        if(Optional.ofNullable(limit).isPresent() )
            builder.setParameter("$limit",limit);

        return new HttpGet(builder.build());
    }



}
