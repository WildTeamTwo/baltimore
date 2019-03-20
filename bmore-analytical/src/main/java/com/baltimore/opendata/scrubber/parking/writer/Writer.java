package com.baltimore.opendata.scrubber.parking.writer;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;

/**
 * Created by paul on 20.08.18.
 */
public class Writer {

    private BufferedOutputStream writer;

    public Writer() {

    }

    protected void writeToFile(byte[] line_of_data) throws IOException {
        NullPointerException e = null;

        writer.write(line_of_data);
    }

    protected Writer initWriter(Path path, boolean append) throws IOException {
        writer = new BufferedOutputStream(new FileOutputStream(path.toFile(), append));
        return this;
    }

    protected void closeOutputStream() throws IOException {
        writer.flush();
        writer.close();
    }
}
