package com.baltimore;

import com.baltimore.common.Console;
import com.baltimore.opendata.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static com.baltimore.common.Cosmetics.intro;
import static com.baltimore.common.Cosmetics.outro;

@SpringBootApplication
public class Application {

    @Autowired
    private List<Task> tasks;
    @Autowired
    private Console console;
    private String menu;

    public static void main(String[] args) throws Exception {
        ApplicationContext context = SpringApplication.run(AppConfiguration.class, args);
        Application app = context.getBean(Application.class);
        app.run(args);
    }

    public void run(String[] args) throws Exception {
        do {
            displayIntro();
            displayMenu();
        }
        while (promptUser());
        displayOutro();
        terminate();
    }

    private String buildMenu() {
        StringBuilder builder = new StringBuilder();
        AtomicInteger count = new AtomicInteger(1);
        tasks.stream().forEach(subProgram -> {
            builder.append(Integer.toString(count.getAndIncrement()))
                    .append(" - ")
                    .append(subProgram.displayName()).append("\n");
        });
        builder.append("Q - Quit \n");
        builder.append("\nEnter choice: ");
        return menu = builder.toString();
    }

    private void displayMenu() {
        System.out.print(menu == null ? buildMenu() : menu);
    }

    private void displayIntro() {
        intro();
        System.out.printf("\nBelow are several tasks. Which would you like to run? \n\n");
    }

    private boolean promptUser() throws Exception {
        String choice = console.input();
        //TODO: implement formal menu provided by jdk

        if (choice.equals("1")) {
            tasks.get(0).start();
        } else if (choice.equals("2")) {
            tasks.get(1).start();
        } else if (choice.equalsIgnoreCase("q")) {
            return false;
        }
        return true;
    }

    private static void terminate() {
        System.exit(1);
    }

    private void displayOutro() {
        outro();
    }
}
