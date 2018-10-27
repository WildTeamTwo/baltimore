package com.baltimore.subprogram.parking.reader;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by paul on 21.08.18.
 * <p>
 * Class should remain immutable. So it can be optimized for multi-threading.
 */
class JsonReader {

    private final static int buffer_size = 81920; //81920 bytes ~80 KB which results in rougly 130 json objects.
    private final BufferedInputStream bufferedStream;
    private final FileInputStream fileStream;
    private final Path path;

    JsonReader(BufferedInputStream bs, FileInputStream fs, Path path) {
        fileStream = fs;
        bufferedStream = bs;
        this.path = path;
    }

    public static JsonReader init(Path path, long start) throws IOException {
        JsonReader jsonReader = initReader(path, start);
        return jsonReader;
    }

    private static JsonReader initReader(Path path, long start) throws IOException {
        JsonReader reader = initInputStream(path);
        skipToPosition(reader.bufferedStream, start);
        return reader;
    }

    private static JsonReader initInputStream(Path path) throws IOException {
        FileInputStream fs = new FileInputStream(path.toFile());
        BufferedInputStream bs = new BufferedInputStream(fs, buffer_size);
        return new JsonReader(bs, fs, path);
    }

    private static void skipToPosition(BufferedInputStream stream, long position) throws IOException {
        if (position > 0) {
            stream.skip(position);
        }
    }

    private static String cleanJson(String token) {
        String newToken = token;

        int startPos = nextJsonBeginPosition(newToken);
        int endPos = lastJsonEnding(newToken);

        if (startPos == -1 || endPos == -1) {
            return null;
        }
        newToken = newToken.substring(startPos, endPos);

        return newToken;
    }

    private static int nextJsonBeginPosition(final String string) {
        final String jsonBeginCharcacters = ",\u0020{";
        return string.indexOf(jsonBeginCharcacters, 0) + jsonBeginCharcacters.length() - 1;
    }

    private static int lastJsonEnding(final String s) {
        final String endCharacters = "\n}\n";
        int last = s.lastIndexOf(endCharacters);
        return last + endCharacters.length();
    }

    public List<String> loadToEOF() throws IOException {
        return readToEOF();
    }

    private List<String> readToEOF() throws IOException {
        List<String> elements = new ArrayList<String>();
        String json_objects;
        byte[] contents = new byte[buffer_size];
        long bytesRead = 0;
        while (bytesRead != -1) {
            //Todo - no longer a need to track bytesread or bytes used.
            bytesRead = bufferedStream.read(contents, 0, buffer_size);
            json_objects = cleanJson(new String(contents));

            if (json_objects != null) {
                elements.add(json_objects);
            }
        }

        return elements;
    }
}
