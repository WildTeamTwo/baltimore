package com.baltimore.persistence;

import com.baltimore.common.Resource;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

/**
 * Created by paul on 18.07.18.
 */
public class FileSystemStore {
    //TODO: add home and project path directories to spring configuration

    private static final List<Resource> resourceQueue = Arrays.asList(Resource.values());
    private static final String PROJECT_PATH = "bmore/open";
    private static final String VERSION = "2.0";
    private static final Path HOME;
    private static final Path PROJECT_ROOT;

    static {
        HOME = Paths.get(System.getProperty("user.home"));
        PROJECT_ROOT = HOME.resolve(PROJECT_PATH);
    }

    private static Path filePath(Resource resource, String page) {
        return fileToPath(resource.path, page);
    }

    private static void createDirectory(Path path) throws IOException {
        if (!fileExists(path))
            Files.createDirectories(path);
    }

    private static boolean fileExists(Path path) {
        return Files.exists(path);
    }

    public static Path directoryToPath(String path) {
        return PROJECT_ROOT.resolve(path).resolve(VERSION);
    }

    private static Path fileToPath(String path, String fileName) {
        return directoryToPath(path).resolve(fileName);
    }

    private void makeResourceDirectory(Resource resource) throws IOException {
        createCacheRootDirectory();
        createResourcePath(resource);
    }

    private void createCacheRootDirectory() throws IOException {
        createDirectory(PROJECT_ROOT);
    }

    public void createResourcePath(Resource resource) throws IOException {
        createDirectory(directoryToPath(resource.path));
    }

    public Integer nextFileName(Resource resource) throws  IOException {
        List<Integer> fileNames = new ArrayList<>();
        Path dir = directoryToPath(resource.path);
        try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(dir)) {
            for (Path path : directoryStream) {
                try {
                    fileNames.add(new Integer(path.getFileName().toString()));
                }catch (NumberFormatException e){
                    System.err.printf("Invalid file %s. Skipping.", path.getFileName().toString());
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        if(fileNames.isEmpty()){
            return new Integer(1);
        }
        Collections.sort(fileNames);

        return new Integer(fileNames.get(fileNames.size()-1) + 1);
    }

    public boolean isDirEmpty(Resource resource) throws IOException {
        Path path = directoryToPath(resource.path);
        if(!fileExists(path)){
            return true;
        }
        try(DirectoryStream<Path> dirStream = Files.newDirectoryStream(path)) {
            return !dirStream.iterator().hasNext();
        }
    }

    public void deleteDir(Resource resource) throws IOException{
        Path pathToBeDeleted = directoryToPath(resource.path);

        Files.walk(pathToBeDeleted, FileVisitOption.FOLLOW_LINKS)
                .sorted(Comparator.reverseOrder())
                .map(Path::toFile)
                .forEach(File::delete);

    }

    public void store(String content, Resource resource, String page) throws IOException {
        if (!Optional.ofNullable(content).isPresent()) {
            return;
        }
        Path fullPath = filePath(resource, page);
        makeResourceDirectory(resource);
        BufferedOutputStream os = new BufferedOutputStream(new FileOutputStream(fullPath.toFile(), false));
        os.write(content.getBytes());
        os.flush();
        os.close();
    }

}
