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
    private static final String VERSION = "1.0";
    private static final String DATA_FILE_NAME = "data.json";
    private static final Path HOME;
    private static final Path PROJECT_ROOT;

    static {
        HOME = Paths.get(System.getProperty("user.home"));
        PROJECT_ROOT = HOME.resolve(PROJECT_PATH);
    }


    public List<Resource> uncachedResources() throws IOException {
        List<Resource> uncachedResources = new ArrayList<Resource>();
        for (Resource resource : resourceQueue) {
            if (!isCached(resource)) {
                uncachedResources.add(resource);
            }
        }
        return uncachedResources;
    }

    public void store(String content, Resource resource) throws Exception {
        if (!Optional.ofNullable(content).isPresent()) {
            return;
        }
        Path fullPath = toPath(resource.path);
        BufferedOutputStream os = new BufferedOutputStream(new FileOutputStream(fullPath.toFile(), false));
        os.write(content.getBytes());
        os.flush();
        os.close();
    }

    private static boolean isCached(Resource resource) {
        Path path = toPath(resource.path);
        return fileExists(path);
    }

    private static boolean fileExists(Path path) {
        return Files.exists(path) && hasContent(path);
    }

    private static boolean hasContent(Path path) {
        FileChannel channel = null;
        try {
            channel = FileChannel.open(path);
            return channel.size() > 0L;
        } catch (IOException e) {
            return false;
        } finally {
            try {
                channel.close();
            } catch (IOException e) {
                return false;
            }

        }
    }

    private static Path toPath(String path) {
        return PROJECT_ROOT.resolve(path).resolve(VERSION).resolve(DATA_FILE_NAME);
    }
}
