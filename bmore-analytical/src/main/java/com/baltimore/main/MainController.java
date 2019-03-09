package com.baltimore.main;

import com.baltimore.subprogram.SubProgram;
import com.baltimore.subprogram.download.BmoreDownloadController;
import com.baltimore.subprogram.parking.ParkingController;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

import static com.baltimore.common.Cosmetics.printBeginning;
import static com.baltimore.common.Cosmetics.printEnding;

/**
 * Created by paul on 03.07.18.
 *
 * Downloads Baltimore Open Data sets to disk.
 */
@SpringBootApplication
public class MainController {

    final List<SubProgram> subPrograms;

    private MainController() {
        SubProgram bmoreController = BmoreDownloadController.init();
        SubProgram parkingController = ParkingController.init();
        List<SubProgram> subPrograms = new ArrayList<>();
        subPrograms.add(bmoreController);
        subPrograms.add(parkingController);
        this.subPrograms = Collections.unmodifiableList(subPrograms);
    }

    public static MainController init() {
        return new MainController();
    }

    public static void main(String[] args) {
        try {

            run();
            System.exit(1);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static void run() throws Exception{
        MainController mainController = MainController.init();
        Scanner scanner = new Scanner(System.in);

        do {
            mainController.intro();
            mainController.displayProgramNames();
        }
        while (mainController.menu());

        mainController.outro();
    }
    public void displayProgramNames() {
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

    public void intro() {
        printBeginning();
        System.out.printf("Below are several programs that you can run. What would you like to do? \n\n");

    }

    private boolean menu() throws Exception {
        Scanner scanner = new Scanner(System.in);
        String choice;

        choice = scanner.next();

        if (choice.equals("1")) {
            executeBmoreDownloadContoller();
        } else if (choice.equals("2")) {
            executeParkingController();
        }
        else if (choice.equalsIgnoreCase("q")){
            return false;
        }

        return true;
    }

    private void executeBmoreDownloadContoller() {
        BmoreDownloadController.init().start();
    }

    private void executeParkingController() throws Exception {
        ParkingController.init().start();
    }

    private void outro() {
        printEnding();
    }
}
