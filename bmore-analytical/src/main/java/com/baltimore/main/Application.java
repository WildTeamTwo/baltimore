package com.baltimore.main;

import com.baltimore.subprogram.SubProgram;
import com.baltimore.subprogram.download.BmoreDownloadController;
import com.baltimore.subprogram.parking.ParkingController;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;

import static com.baltimore.common.Cosmetics.intro;
import static com.baltimore.common.Cosmetics.outro;

@SpringBootApplication
public class Application {

    final List<SubProgram> subPrograms;
    final String menu;

    private Application() {
        this.subPrograms = Collections.unmodifiableList(Arrays.asList(BmoreDownloadController.init(), ParkingController.init()));
        menu = buildMenu();
    }

    public static Application init() {
        return new Application();
    }

    public static void main(String[] args) throws Exception{
        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse( initOptions(), args );
        if(cmd.getArgList().size() != 0){
            runOption(cmd.getOptions());
        }
        else {
            run();
        }
        terminate();
    }

    private static void run() throws Exception {
        Application mainController = Application.init();
        do {
            mainController.displayIntro();
            mainController.displayMenu();
        }
        while (mainController.promptUser());
        mainController.displayOutro();
    }

    private static void runOption(Option[] options) throws Exception {
        Application mainController = Application.init();
        mainController.displayIntro();
        mainController.runProgram();
        mainController.displayOutro();

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

    private void runProgram() throws Exception{
        subPrograms.get(0).start();
    }
    private static void terminate() {
        //todo: release resources
        System.exit(1);
    }

    private static Options initOptions(){
        final Options options = new Options();
        options.addOption(Option.builder().argName("d").longOpt("download").hasArg(true).desc("download data set [arrest|parking]").build());
        options.addOption(Option.builder().argName("m").longOpt("months").hasArg(true).desc("months of data to retrieve").build());
        options.addOption(Option.builder().argName("y").longOpt("years").hasArg(true).desc("years of data to retrieve").build());
        return options;
    }
    private void displayOutro() {
        outro();
    }
}
