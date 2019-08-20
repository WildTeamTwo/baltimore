package com.baltimore.persistence;

import com.baltimore.Exceptions.AppSetupException;
import com.baltimore.common.Console;
import com.baltimore.common.Resource;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

/**
 * Created by paul on 17.07.18.
 */
public class FileSystemSetup {

    private static final String PROJECT_PATH = "bmore/open";
    private static final String VERSION = "2.0";
    private static final Path HOME;
    private static final Path PROJECT_ROOT;
    private static final List<Resource> resourceQueue = Arrays.asList(Resource.values());

    static {
        HOME = Paths.get(System.getProperty("user.home"));
        PROJECT_ROOT = HOME.resolve(PROJECT_PATH);
    }

    public static void setup() throws AppSetupException, IOException {
        if (!fileExists(PROJECT_ROOT)) {
            Console.outl("This app will create working directories. Continue (Y/N)? ");
            if (!"Y".equalsIgnoreCase(Console.input())) {
                throw new AppSetupException();
            }
        }
        createProjectRoot();
        createDirectoryAndFile();
    }

    private static void createProjectRoot() throws IOException {
        createDirectory(PROJECT_ROOT);
    }

    private static void createDirectoryAndFile() throws IOException {
        for (Resource resource : resourceQueue) {
            createDirectory(PROJECT_ROOT.resolve(resource.path).resolve(VERSION));
        }
    }

    private static void createDirectory(Path path) throws IOException {
        if (!fileExists(path))
            Files.createDirectories(path);
    }

    private static boolean fileExists(Path path) {
        return Files.exists(path);
    }

    private boolean doesProjectRootExist() throws IOException {
        return fileExists(PROJECT_ROOT);
    }

}
