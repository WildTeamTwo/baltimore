package com.baltimore.common;

import java.util.Scanner;

/**
 * Created by paul on 20.03.19.
 * TODO: use
 */
public class Console {
    private static final String newLine = "\n";
    static Scanner scanner;

    static {
        scanner = new Scanner(System.in);
    }

    public static String input() {
        return scanner.next();
    }

    public static void outl(String msg) {
        System.out.print(newLine + msg + newLine);
    }

}
