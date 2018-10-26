package com.baltimore.subprogram.download;

import com.baltimore.SubProgram;
import com.baltimore.common.Cosmetics;
import com.baltimore.common.Resource;

import java.util.Scanner;

/**
 * Created by paul on 26.10.18.
 */
public class BmoreDownloadController implements SubProgram {

    private BmoreDownloadController(){

    }

    public static BmoreDownloadController init(){
        return new BmoreDownloadController();
    }

    @Override
    public String displayName() {
        return "Baltimore Data Grab";
    }

    @Override
    public void start() {
        try {
            startMessage();
            chooseBmoreDataToDownload();
            end();
        }
        catch ( Exception e){
            e.printStackTrace();
            System.exit(1);
        }
    }

    @Override
    public void destroy() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void end() {
        endMessage();
        throw new UnsupportedOperationException();
    }


    private static void chooseBmoreDataToDownload() throws Exception {
        promptUser(getChoices());
    }

    private static void promptUser(String choices) throws  Exception{
        Scanner scanner = new Scanner(System.in);
        String choice;

        do {
            System.out.print(choices);
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
            } else if (choice.equals("6")) {
                downloadData(Resource.LIQUOR);
            } else if (choice.equalsIgnoreCase("A")) {
                downloadData();
            }

            System.out.print("\n");
        }
        while (!choice.equalsIgnoreCase("N"));

    }

    private static String getChoices(){

        StringBuilder builder = new StringBuilder();
        Resource[] resources = Resource.values();
        builder.append("\nWhich data set would you like to download. Enter a number:\n");
        for (int i = 0; i < resources.length; i++) {
            builder.append(Integer.toString(i + 1)).append(" - ").append(resources[i].name()).append("\n");
        }
        builder.append("A  - All \n");
        builder.append("N  - None \n");
        builder.append("Enter choice: ");

        return builder.toString();
    }

    private static void downloadData(Resource resource) throws Exception {
        System.out.printf("Refreshing data set...");
        new BaltimoreAPI().downloadResource(resource);
        System.out.println("Refresh complete.");
    }


    private static void downloadData() throws Exception {
        new BaltimoreAPI().downloadAllResources();
    }

    private static void startMessage() {
        Cosmetics.printStars();
        System.out.println("Baltimore Data Grab");
        Cosmetics.printStars();
    }


    private static void endMessage() {
        Cosmetics.printStars();
        System.out.println("Noooooooooooooooooooo. Just Kidding.  Cheers. Tschuss. Auf Wiedersehen");
        Cosmetics.printStars();
    }


}
