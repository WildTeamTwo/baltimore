package com.baltimore;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.FileAttribute;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.PosixFilePermissions;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

/**
 * Created by paul on 17.07.18.
 */
public class CacheInitializor {

    private static final String PROJECT_PATH = "bmore/open";
    private static final String VERSION = "1.0";
    private static final Path HOME;
    private static final Path PROJECT_ROOT;
    private static final String DATA_FILE_NAME = "data.json";
    private static final List<Resource> resourceQueue = Arrays.asList(Resource.values());

    static {
        HOME = Paths.get(System.getProperty("user.home"));
        PROJECT_ROOT = HOME.resolve(PROJECT_PATH);
    }

    public static void setup() throws IOException {
        createProjectRoot();
        createDirectoryAndFile();
    }

    private static void createProjectRoot() throws IOException{
        createDirectory(PROJECT_ROOT);
    }

    private static void createDirectoryAndFile() throws IOException {
        for(Resource resource : resourceQueue){
            createDirectory(PROJECT_ROOT.resolve(resource.path).resolve(VERSION));
            createEmptyDataFile(resource);
        }
    }

    private static void createDirectory(Path path) throws IOException{
        if(!fileExists(path))
            Files.createDirectories(path);
    }

    private static boolean fileExists(Path path){
        return Files.exists(path);
    }

    private static void createEmptyDataFile(Resource resource) throws IOException{
        Path path = toPath(resource.path);
        if(fileExists(path)){
            return;
        }

        Set<PosixFilePermission> permissions = PosixFilePermissions.fromString("rw-rw-rw-");
        FileAttribute<Set<PosixFilePermission>> fileAttributes = PosixFilePermissions
                .asFileAttribute(permissions);

        Files.createFile(path, fileAttributes);
    }

    private static Path toPath(String path) {
        return PROJECT_ROOT.resolve(path).resolve(VERSION).resolve(DATA_FILE_NAME);
    }
}
