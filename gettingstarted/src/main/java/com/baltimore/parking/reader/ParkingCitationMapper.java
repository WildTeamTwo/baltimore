package com.baltimore.parking.reader;

import com.baltimore.common.data.ParkingCitation;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by paul on 21.08.18.
 */
public class ParkingCitationMapper {

    protected static List<ParkingCitation> map(List<String> jsons) throws IOException {
        List<ParkingCitation> all = new ArrayList<ParkingCitation>();
        if (jsons == null) {
            return all;
        }
        String array_of_jsons = null;
        for (String json : jsons) {
            try {
                array_of_jsons = makeArrayOfJsons(json);

                all.addAll(unmarshallToCollection(array_of_jsons));
            }
            catch (IOException e){
                //TODO: impelement try again - if unmarshall fails it's likely because the first/last json object is not well formed.  If this happens delete characters until the next best json is reached do the operation again. If it still fails. continue to next element.
                e.printStackTrace();
                System.err.print(array_of_jsons);
                continue;

            }
        }
        return all;
    }

    private static List<ParkingCitation> unmarshallToCollection(String array_of_jsons) throws IOException{
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(array_of_jsons, new TypeReference<List<ParkingCitation>>(){});

    }
    private static String makeArrayOfJsons(String json)  {
        return new StringBuilder().append("[").append(json).append("]").toString();
    }

}



