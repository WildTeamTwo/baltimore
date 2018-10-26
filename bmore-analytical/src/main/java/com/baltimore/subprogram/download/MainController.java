package com.baltimore.subprogram.download;

import com.baltimore.SubProgram;
import com.baltimore.common.Cosmetics;
import com.baltimore.subprogram.parking.ParkingController;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Created by paul on 03.07.18.
 * <p>
 * Downloads Balitmore Open Data sets to disk.
 */
public class MainController {

    final List<SubProgram> subPrograms;

    private MainController(){
        SubProgram bmoreController = BmoreDownloadController.init();
        SubProgram parkingController = ParkingController.init();
        subPrograms = new ArrayList<>();
        subPrograms.add(bmoreController);
        subPrograms.add(parkingController);

    }
    public static MainController init(){
        return new MainController();
    }

    public static void main(String[] args)  {
        try {
            intro();
            MainController mainController = MainController.init();
            mainController.displayProgramNames();
            mainController.promptUser();
        }
        catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    public void displayProgramNames(){
        System.out.print( buildChoices() );
    }

    private String buildChoices(){
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < subPrograms.size(); i++) {
            builder.append(Integer.toString(i + 1)).append(" - ").append(subPrograms.get(i).displayName()).append("\n");
        }
        builder.append("N  - None \n");
        builder.append("Enter choice: ");

        return builder.toString();
    }

    public static void intro(){
        Cosmetics.printStars();
        System.out.printf("Welcome to Analytics Baltimore \n\n");
        Cosmetics.printStars();
        System.out.printf("Below are several programs that you can run. What would you like to do? \n");

    }

    private void promptUser() throws  Exception{
        Scanner scanner = new Scanner(System.in);
        String choice;

        do {
            choice = scanner.next();

            if (choice.equals("1")) {
                executeBmoreDownloadContoller();
            }
            else if (choice.equals("2")) {
                executeParkingController();
            }
            System.out.print("\n");
        }
        while (!choice.equalsIgnoreCase("N"));

    }


    private void executeBmoreDownloadContoller(){
        BmoreDownloadController.init().start();
    }

    private void executeParkingController() throws Exception {
        ParkingController.init().start();
    }
}
