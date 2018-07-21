package com.baltimore;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * Created by paul on 18.07.18.
 */
public class CacheStore {

    private static final List<Resource> resourceQueue = Arrays.asList(Resource.values());

    private static final String PROJECT_PATH = "bmore/open";
    private static final String VERSION = "2.0";
    private static final String DATA_FILE_NAME = "data";
    private static final Path HOME;
    private static final Path PROJECT_ROOT;

    static {
        HOME = Paths.get(System.getProperty("user.home"));
        PROJECT_ROOT = HOME.resolve(PROJECT_PATH);
    }

    public void store(String content, Resource resource, String page) throws IOException {
        if (!Optional.ofNullable(content).isPresent()) {
            return;
        }
        Path fullPath = filePath(resource, page);
        BufferedOutputStream os = new BufferedOutputStream(new FileOutputStream(fullPath.toFile(), false));
        os.write(content.getBytes());
        os.flush();
        os.close();
    }

    private static Path filePath(Resource resource, String page){
        return toPath(resource.path, DATA_FILE_NAME + page);
    }


    private static Path toPath(String path, String fileName) {
        return PROJECT_ROOT.resolve(path).resolve(VERSION).resolve(fileName);
    }

}
