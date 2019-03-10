package com.baltimore.main;

import com.baltimore.subprogram.SubProgram;
import com.baltimore.subprogram.download.BmoreDownloadController;
import com.baltimore.subprogram.parking.ParkingController;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;

import static com.baltimore.common.Cosmetics.intro;
import static com.baltimore.common.Cosmetics.outro;

@SpringBootApplication
public class MainController {

    final List<SubProgram> subPrograms;
    final String menu;

    private MainController() {
        this.subPrograms = Collections.unmodifiableList(Arrays.asList(BmoreDownloadController.init(), ParkingController.init()));
        menu = buildMenu();
    }

    public static MainController init() {
        return new MainController();
    }

    public static void main(String[] args) throws Exception{
            run();
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
        System.out.print(buildMenu());
    }

    private String buildMenu() {
        if(menu != null){
            return menu;
        }
        StringBuilder builder = new StringBuilder();
        AtomicInteger count = new AtomicInteger(1);
        subPrograms.stream().forEach( subProgram -> {
            builder.append(Integer.toString(count.getAndIncrement()))
                    .append(" - ")
                    .append(subProgram.displayName()).append("\n");
        } );

        builder.append("Q - Quit \n");
        builder.append("\nEnter choice: ");

        return builder.toString();
    }

    public void displayIntro() {
        intro();
        System.out.printf("\nBelow are several programs. Which would you like to run? \n\n");
    }

    private boolean promptUser() throws Exception {
        Scanner scanner = new Scanner(System.in);
        try {
            String choice = scanner.next();
            //TODO: implement formal menu provided by jdk

            if (choice.equals("1")) {
                subPrograms.get(0).start();
            } else if (choice.equals("2")) {
                subPrograms.get(1).start();
            } else if (choice.equalsIgnoreCase("q")) {
                return false;
            }
            return true;
        } finally {
            scanner.close();
        }
    }

    private static void terminateProgram() {
        //todo: release resources
        System.exit(1);
    }

    private void displayOutro() {
        outro();
    }
}
