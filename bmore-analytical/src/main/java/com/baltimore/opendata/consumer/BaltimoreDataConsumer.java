package com.baltimore.opendata.consumer;

import com.baltimore.common.Console;
import com.baltimore.common.Resource;
import com.baltimore.opendata.Task;
import com.baltimore.persistence.FileSystemStore;
import com.google.common.base.Enums;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by paul on 26.10.18.
 * <p/>
 * Downloads MAX_RECORDS_PER_DOWNLOAD records per call from the Open Data API.
 * Saves MAX_SIMULTANEOUS_DOWNLOADS * MAX_BATCH FILES files to the file sytem.
 *
 * Saving 100 files takes roughly 2 minutes
 *
 * Total # of Records = MAX_RECORDS_PER_DOWNLOAD * MAX_SIMULTANEOUS_DOWNLOADS * MAX_BATCH
 */
@Component
public class BaltimoreDataConsumer implements Task {

    private final String menu;
    private OpenDataAPIClient openDataApiClient;
    private FileSystemStore fileSystem;
    private static final int MAX_RECORDS_PER_DOWNLOAD = 2000;
    private static final int MAX_SIMULTANEOUS_DOWNLOADS = 5;
    private static final int MAX_BATCH = 100;

    public BaltimoreDataConsumer(@Autowired OpenDataAPIClient client, @Autowired FileSystemStore fileSystemStore) {
        openDataApiClient = client;
        fileSystem = fileSystemStore;
        menu = buildChoices();
    }

    @Override
    public void start() {
        try {
            startMessage();
            promptUser(menu);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    private void promptUser(String menu) throws Exception {
        System.out.print(menu);
        Scanner scanner = new Scanner(System.in);
        String choice;
        while (!(choice = scanner.next()).equalsIgnoreCase("q")) {
            try {
                Resource resource = Enums.getIfPresent(Resource.class, choice.toUpperCase()).orNull();
                cleanDirectory(resource);
                downloadAsynchronously(resource);
                System.out.print(menu);
            } catch (IllegalStateException e) {
                System.out.println("\nCannot start new download session. Move files or quit.");
                System.out.print(menu);
            }
        }
    }

    private void downloadAsynchronously(Resource resource) throws Exception {
        if (resource == null) {
            System.out.printf("\nFound no dataset matching that name. Try again\n");
            return;
        }
        Integer offset = 0;
        Collection<Callable<String>> tasks = new ArrayList<>();
        for (int i = 0; i < MAX_BATCH; i++) {
            offset = initSimultaneousDownloadTasks(resource, tasks, offset);
            List<Future<String>> results = executeDownloadsSimultaneously(tasks);
            store(results, resource);
            tasks.clear();
            apiRest();

        }
    }

    private Integer initSimultaneousDownloadTasks(Resource resource, Collection<Callable<String>> callables, Integer start) {
        Integer lastOffset = 0; //lastOffset exist because offset in a lambda expression which requires an "effectively final variable" - a variable with a value that does not change after initialtization. thus resultOffset acts as a temp variable.
        for (int i = 0; i < MAX_SIMULTANEOUS_DOWNLOADS; i++) {
            Integer offset = start + (i * MAX_RECORDS_PER_DOWNLOAD);
            Callable<String> callable = () -> {
                return openDataApiClient.download(resource, offset, MAX_RECORDS_PER_DOWNLOAD);
            };
            callables.add(callable);
            lastOffset = offset;
        }

        return lastOffset;
    }

    private List<Future<String>> executeDownloadsSimultaneously(Collection<Callable<String>> tasks) throws InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(MAX_SIMULTANEOUS_DOWNLOADS);
        List<Future<String>> results = executorService.invokeAll(tasks);
        executorService.shutdown();
        try {
            if (!executorService.awaitTermination(800, TimeUnit.MILLISECONDS)) {
                executorService.shutdownNow();
            }
        } catch (InterruptedException e) {
            executorService.shutdownNow();
        }
        return results;
    }

    private void store(List<Future<String>> futures, Resource resource) throws IOException, InterruptedException, ExecutionException {
        Integer nextFileNumber = fileSystem.nextFileName(resource);
        AtomicInteger counter = new AtomicInteger(nextFileNumber);
        System.out.print("Saving...");
        String response;
        for (Future<String> future : futures) {
            response = future.get();
            if(isResponseEmpty(response)){
                continue;
            }
            store(response, resource, counter.getAndIncrement());
            System.out.print(".");
        }
    }

    private void store(String response, Resource resource, Integer fileName) {
        try {
            fileSystem.store(response, resource, fileName.toString());
        } catch (IOException e) {
            System.out.printf("Encountered failure when caching resource %s\n", resource.name());
        }
    }

    private boolean isResponseEmpty(String response){
        try {
            return response.startsWith("[]");
        }catch (Exception e){
            return false;
        }
    }
    @Override
    public String displayName() {
        return "Download Raw Data From Baltimore Open Data API";
    }

    private void startMessage() {
        System.out.println(displayName());
    }

    private synchronized void apiRest() throws Exception {
        Random random = new Random();
        Integer probability = random.nextInt(100);

        if(probability < 25)
            wait(2000L);
        else if(probability < 51)
            wait(5000L);
        else
            wait(8000L);
    }

    private static String buildChoices() {
        StringBuilder builder = new StringBuilder();
        Resource[] resources = Resource.values();
        builder.append("\nWhich data set would you like to download. Enter the name:\n");
        for (Resource resource : resources) {
            builder.append("- ").append(resource).append("\n");
        }
        builder.append("- Q / quit \n\n");
        builder.append("Enter name: ");

        return builder.toString();
    }


    private void cleanDirectory(Resource resource) throws IOException, IllegalStateException {
        if (!fileSystem.isDirEmpty(resource)) {
            System.out.printf("This app must delete files in location %s to run. Do you wish to overwrite them (Y/N)? ", FileSystemStore.directoryToPath(resource.path).toString());
            if (Console.input().equalsIgnoreCase("y")) {
                fileSystem.deleteDir(resource);
                fileSystem.createResourcePath(resource);
            } else {
                throw new IllegalStateException();
            }
        }
    }


}
