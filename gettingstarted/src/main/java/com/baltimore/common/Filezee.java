package com.baltimore.common;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Filezee {


    public static void createDir(Path path) throws IOException {
        if (!exists(path)) {
            java.nio.file.Files.createDirectories(path);
        }
    }

    public static void createFile(Path path) throws IOException {
        if (!exists(path)) {
            java.nio.file.Files.createFile(path);
        }
    }

    public static void removeIfExist(Path path) throws IOException{
        java.nio.file.Files.deleteIfExists(path);
    }

    public static boolean exists(Path path) {
        return java.nio.file.Files.exists(path);
    }


    public static List<Path> filesInDirectory(Path path) throws  IOException {
       return Files.list(path).collect(Collectors.<Path>toList());
    }
}
