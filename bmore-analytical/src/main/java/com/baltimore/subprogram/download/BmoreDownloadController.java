package com.baltimore.subprogram.download;

import com.baltimore.common.Resource;
import com.baltimore.subprogram.SubProgram;
import com.google.common.base.Enums;

import java.util.Scanner;

/**
 * Created by paul on 26.10.18.
 */
public class BmoreDownloadController implements SubProgram {

    private final String menu;

    private BmoreDownloadController() {
        menu = buildChoices();
    }

    public static BmoreDownloadController init() {
        return new BmoreDownloadController();
    }

    private void promptUser() throws Exception {
        promptUserX(menu);
    }

    private void promptUser(String choices) throws Exception {
        Scanner scanner = new Scanner(System.in);
        String choice;

        do {
            System.out.print(choices);
            choice = scanner.next();

            if (choice.equals("1")) {
                download(Resource.PROPERTY);
            } else if (choice.equals("2")) {
                download(Resource.ARREST);
            } else if (choice.equals("3")) {
                download(Resource.VICTIM);
            } else if (choice.equals("4")) {
                download(Resource.THREE11);
            } else if (choice.equals("5")) {
                download(Resource.PARKING);
            } else if (choice.equals("6")) {
                download(Resource.LIQUOR);
            } else if (choice.equalsIgnoreCase("A")) {
                downloadData();
            }

            System.out.print("\n");
        }
        while (!choice.equalsIgnoreCase("N"));

    }

    private void promptUserX(String choices) throws Exception {
        Scanner scanner = new Scanner(System.in);
        String choice;

        do {
            System.out.print(choices);
            choice = scanner.next();
            Resource resource = Enums.getIfPresent(Resource.class, choice.toUpperCase()).or(Resource.DEFAULT);
            if(!choice.equalsIgnoreCase("N") && resource == Resource.DEFAULT){
                System.out.println("Did not recognize this choice. Try again");
            }
            else {
                download(resource);
            }
            System.out.print("\n");
        }
        while (!choice.equalsIgnoreCase("N"));

    }
    private static int promptUserMaxPages() throws Exception{
        System.out.print("How many pages would you like to download (Default is 1000 pages which is ~550MB) : ");
        Scanner scanner = new Scanner(System.in);
        return scanner.nextInt();
    }

    private static String buildChoices() {

        StringBuilder builder = new StringBuilder();
        Resource[] resources = Resource.values();
        builder.append("\nWhich data set would you like to download. Enter the name:\n");
        for (int i = 0; i < resources.length; i++) {
            builder.append(Integer.toString(i + 1)).append(" - ").append(resources[i].name()).append("\n");
        }
        builder.append("A  - All \n");
        builder.append("N  - None \n");
        builder.append("Enter name: ");

        return builder.toString();
    }

    private static void download(Resource resource) throws Exception {
        int pageLimit = promptUserMaxPages();
        System.out.printf("Refreshing data set...");
        BaltimoreAPI.init(pageLimit).downloadResource(resource);
        System.out.println("Refresh complete.");
    }

    private static void downloadData() throws Exception {
        BaltimoreAPI.init().downloadAllResources();
    }

    @Override
    public String displayName() {
        return "Download Raw Data From Baltimore Open Data API";
    }

    @Override
    public void start() {
        try {
            startMessage();
            promptUser();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    private void startMessage() {
        System.out.println(displayName());
    }


}
