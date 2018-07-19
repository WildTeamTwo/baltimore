package com.baltimore;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;

/**
 * Created by paul on 03.07.18.
 *
 * Downloads Balitmore Open Data sets to disk.
 *
 */
public class Main {


    public static void main(String[] args) throws Exception {

        startMessage();
        setupCache();
        refreshCacheProgram();

    }

    private static void refreshCacheProgram() throws Exception{
        Scanner scanner = new Scanner(System.in);
        String choice = null;
        Resource[] resources = Resource.values();
        StringBuilder builder = new StringBuilder();

        builder.append("\nEnter a number for fresh data\n");
        for (int i =0; i < resources.length; i++){
            builder.append(Integer.toString(i+1)).append(") - ").append(resources[i].name()).append("\n");
        }
        builder.append("N  - None ");
        builder.append("\nEnter choice: ");

        do {
            System.out.print(builder.toString());
            choice = scanner.next();

            if (choice.equals("1")) {
                downloadData(Resource.PROPERTY);
            } else if (choice.equals("2")) {
                downloadData(Resource.ARREST);
            } else if (choice.equals("3")) {
                downloadData(Resource.VICTIM);
            } else if (choice.equals("4")) {
                downloadData(Resource.THREE11);
            } else if (choice.equals("5")) {
                downloadData(Resource.PARKING);
            }

            System.out.print("\n");
        }
        while (!choice.equalsIgnoreCase("N"));

    }

    private static void setupCache() throws Exception {

        System.out.println("Initializing cache ...");
        CacheInitializor.setup();
        downloadData();
        System.out.println("Cache setup is complete ...");

    }

    private static void downloadData() throws Exception {
        new BaltimoreAPI().downloadResources();
    }

    private static void downloadData(Resource resource) throws Exception {
        System.out.printf("Refreshing data set...");
        new BaltimoreAPI().downloadResource(resource);
        System.out.println("Refresh complete.");
    }


    public static Long averageAge(List<Arrest> arrests) {
        Long ageSum = Long.valueOf(arrests.get(0).getAge());
        for (int i = 0; i < arrests.size(); i++) {
            ageSum += getAge(arrests.get(i));
        }
        return ageSum / arrests.size();
    }

    private static void average(List<Arrest> arrests) {

        System.out.println("Total Arrests: " + arrests.size());
        System.out.println("Average Arrest Age: " + averageAge(arrests));
    }

    private static Long getAge(Arrest arrest) {
        try {
            return Long.valueOf(arrest.getAge());
        } catch (Exception e) {
            System.err.println("Encountered error converting age");
            return 0L;
        }
    }

    private static void printStars() {
        int length = 80;
        for (int i = 0; i < length; i++) {
            System.out.print("*");
        }
        System.out.println("");
    }


    private static void startMessage(){
        printStars();
        System.out.println("Baltimore Open Data Client");
        printStars();

    }
}
