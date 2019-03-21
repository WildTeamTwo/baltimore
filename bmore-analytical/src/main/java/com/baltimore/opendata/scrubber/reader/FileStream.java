package com.baltimore.opendata.scrubber.reader;

import com.baltimore.common.Configuration;
import com.baltimore.common.Filezee;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

/**
 * Created by paul on 21.08.18.
 */
class FileStream {

    private final List<Path> rawParking;


    private FileStream(List<Path> files) {
        rawParking = files;
    }

    static FileStream load() throws IOException {
        return new FileStream(Filezee.filesInDirectory(Configuration.PARKING_HOME));
    }

    Path nextFile() {
        if (hasNext()) {
            return rawParking.get(0);
        }
        return null;
    }

    Path removeCurrent() {
        return rawParking.remove(0);
    }

    boolean hasNext() {
        return !rawParking.isEmpty();
    }

}
