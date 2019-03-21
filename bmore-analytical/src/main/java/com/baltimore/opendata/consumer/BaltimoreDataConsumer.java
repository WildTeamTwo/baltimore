package com.baltimore.opendata.consumer;

import com.baltimore.common.Resource;
import com.baltimore.opendata.Task;
import com.google.common.base.Enums;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Scanner;

/**
 * Created by paul on 26.10.18.
 */
@Component
public class BaltimoreDataConsumer implements Task {

    private final String menu;
    private OpenDataAPIClient apiClient;

    public BaltimoreDataConsumer(@Autowired OpenDataAPIClient client) {
        apiClient = client;
        menu = buildChoices();
    }

    private void promptUser(String menu) throws Exception {
        Scanner scanner = new Scanner(System.in);
        String choice;
        do {

            System.out.print(menu);
            choice = scanner.next();
            Resource resource = Enums.getIfPresent(Resource.class, choice.toUpperCase()).orNull();
            download(resource);
        }
        while (!choice.equalsIgnoreCase("Q"));

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

    private void download(Resource resource) throws Exception {
        if(resource == null){
            System.out.printf("\nFound no dataset matching that name. Try again\n");
            return;
        }
        apiClient.downloadResource(resource);
    }


    private static int promptMaxPages() throws Exception{
        System.out.print("How many pages would you like to download (Default is 1000 pages which is ~550MB) : ");
        Scanner scanner = new Scanner(System.in);
        return scanner.nextInt();
    }

    @Override
    public String displayName() {
        return "Download Raw Data From Baltimore Open Data API";
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

    private void startMessage() {
        System.out.println(displayName());
    }


}
