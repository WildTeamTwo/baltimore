package com.baltimore;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.io.Resources;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;

/**
 * Created by paul on 03.07.18.
 */
public class Main {

    private static final String ARREST_RESOURCE = "https://data.baltimorecity.gov/resource/icjs-e3jg.json";
    private static final boolean LIVE = true;

    public static void main(String[] args) throws Exception {
        String responseStr = readBaltimoreCrime();
        List<Arrest> arrests = new ObjectMapper().readValue(responseStr, new TypeReference<List<Arrest>>(){});
        System.out.println(responseStr);
        System.out.println("Total Arrests: " + arrests.size() );
        System.out.println("Average Arrest Age: " + averageAge(arrests));
    }

    public static String readBaltimoreCrime() throws Exception {
        return LIVE ? readLive() : readTest();
    }

    public static String readLive() throws IOException {
        HttpClient client = HttpClientBuilder.create().build();
        HttpGet request = new HttpGet(ARREST_RESOURCE);
        HttpResponse response = client.execute(request);

        String responseStr = EntityUtils.toString(response.getEntity());

        EntityUtils.consume(response.getEntity());
        return responseStr;

    }

    public static String readTest() throws IOException{
        return Resources.toString(Resources.getResource("com/baltimore/baltimore_arrest.json"), Charset.defaultCharset());
    }

    public static Long averageAge(List<Arrest> arrests){
        Long ageSum = Long.valueOf(arrests.get(0).getAge());
        for(int i =0; i < arrests.size(); i++){
            ageSum += getAge(arrests.get(i));
        }

        return ageSum / arrests.size() ;
    }

    private static Long getAge(Arrest arrest){
        try {
            return Long.valueOf(arrest.getAge());
        }
        catch (Exception e){
            System.err.println("Encountered error converting age");
            return 0L;
        }
    }

}
