package com.baltimore.common;

import java.util.Scanner;

/**
 * Created by paul on 20.03.19.
 */
public class Console {
    Scanner scanner;

    public Console(){
        scanner = new Scanner(System.in);
    }
    private final String newLine = "\n";

    public String input(){
        return scanner.next();
    }

    public void outln(String msg){
        out(msg + newLine);
    }

    public void out(String msg){
        System.out.print(msg);
    }
}
