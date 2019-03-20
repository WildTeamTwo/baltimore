package com.baltimore.opendata.scrubber.parking.reader;

import com.baltimore.model.ParkingCitation;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by paul on 17.08.18.
 */
public class BatchCitationReader {

    private final FileStream fileStream;
    private final int MAX_FILES = 5;

    private BatchCitationReader(FileStream fs) {
        fileStream = fs;
    }

    public static BatchCitationReader init() throws IOException {
        FileStream fs = FileStream.load();
        return new BatchCitationReader(fs);
    }

    public List<ParkingCitation> loadCitationBatch() throws IOException {
        List<String> citations = new ArrayList<String>();
        do {
            citations.addAll(loadToEOF());
        } while (citations.size() < MAX_FILES && fileStream.hasNext());

        return ParkingCitationMapper.map(citations);
    }

    private List<String> loadToEOF() throws IOException {
        List<String> citations = null;

        if (fileStream.hasNext()) {
            JsonReader reader = JsonReader.init(fileStream.nextFile(), 0);
            citations = reader.loadToEOF();
            removeFile();
        }

        return citations;
    }

    private void removeFile() {
        fileStream.removeCurrent();
    }


}
