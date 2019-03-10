package com.baltimore.main;

import com.baltimore.subprogram.SubProgram;
import com.baltimore.subprogram.download.BmoreDownloadController;
import com.baltimore.subprogram.parking.ParkingController;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

import static com.baltimore.common.Cosmetics.printEnding;

@SpringBootApplication
public class MainController {

    final List<SubProgram> subPrograms;

    private MainController() {
        this.subPrograms = Collections.unmodifiableList(Arrays.asList(BmoreDownloadController.init(), ParkingController.init()));
    }

    public static MainController init() {
        return new MainController();
    }

    public static void main(String[] args) {
        try {
            run();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static void run() throws Exception {
        MainController mainController = MainController.init();
        do {
            mainController.displayIntro();
            mainController.displayMenu();
        }
        while (mainController.promptUser());
        mainController.displayOutro();
        terminateProgram();
    }

    public void displayMenu() {
        System.out.print(buildChoices());
    }

    private String buildChoices() {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < subPrograms.size(); i++) {
            builder.append(Integer.toString(i + 1)).append(" - ").append(subPrograms.get(i).displayName()).append("\n");
        }
        builder.append("Q - quit \n");
        builder.append("\nEnter choice: ");

        return builder.toString();
    }

    public void displayIntro() {
        displayIntro();
        System.out.printf("Below are several programs that you can run. What would you like to do? \n\n");

    }

    private boolean promptUser() throws Exception {
        Scanner scanner = new Scanner(System.in);
        try {
            String choice = scanner.next();

            if (choice.equals("1")) {
                executeBmoreDownloadContoller();
            } else if (choice.equals("2")) {
                executeParkingController();
            } else if (choice.equalsIgnoreCase("q")) {
                return false;
            }
            return true;
        } finally {
            scanner.close();
        }
    }

    private void executeBmoreDownloadContoller() {
        BmoreDownloadController.init().start();
    }

    private void executeParkingController() throws Exception {
        ParkingController.init().start();
    }

    private static void terminateProgram() {
        //todo: release resources
        System.exit(1);
    }

    private void displayOutro() {
        printEnding();
    }
}
