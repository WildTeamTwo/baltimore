package com.baltimore.common;

import java.util.Scanner;

/**
 * Created by paul on 20.03.19.
 */
public class Console {
    static Scanner scanner;
    public Console(){
        scanner = new Scanner(System.in);
    }
    private static final String newLine = "\n";

    public static  String input(){
        return scanner.next();
    }

    public static void outl(String msg){
        System.out.print(newLine + msg + newLine);
    }

}
