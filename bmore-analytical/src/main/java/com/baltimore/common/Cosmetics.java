package com.baltimore.common;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Stack;

/**
 * Created by paul on 26.10.18.
 */
public class Cosmetics {

    static Map<Long,String> map = new HashMap<>();
    static ArrayList<Long> keys = new ArrayList<>();

    public static void intro() {

    }

    public static void outro() {
        //TODO - insert quote generator
        //System.out.println("");
    }


    public static void main(String [] args){
        int length = 20000;
        LinkedList<Integer> linkedList = new LinkedList();
        ArrayList<Integer> arrayList = new ArrayList<>();
        Integer [] array = new Integer[length];
        Stack stack = new Stack();
        long lStart = 0;
        long lEnd = 0;


        for(int i = 0; i < length; i++)  {
            linkedList.add(i);
        }

        for(int i = 0; i < length; i++)  {
            arrayList.add(i);
        }

        for(int i = 0; i < length; i++)  {
            array[i] = i;
        }

        for(int i = 0; i < length; i ++){
            stack.push(i);
        }

        lStart = System.nanoTime();
        linkedList.stream().forEach(Long::valueOf);
        lEnd = System.nanoTime();
        add(lEnd, lStart, "linked list forEach");

        lStart = System.nanoTime();
        arrayList.stream().forEach(Long::valueOf);
        lEnd = System.nanoTime();
        add(lEnd, lStart, "array list forEach");

        lStart = System.nanoTime();
        for(int i =0; i < arrayList.size(); i++){
            Long.valueOf(arrayList.get(0));
        }
        lEnd = System.nanoTime();
        add(lEnd, lStart, "array list for loop");

        lStart = System.nanoTime();
        Iterator a = linkedList.iterator();
        while (a.hasNext()){
            a.next();
        }
        lEnd = System.nanoTime();
        add(lEnd, lStart, "linked list iterator");

        lStart = System.nanoTime();
        Iterator b = arrayList.iterator();
        while (b.hasNext()){
            b.next();
        }
        lEnd = System.nanoTime();
        add(lEnd, lStart, "array list iterator");

        lStart = System.nanoTime();
        for(int i =0; i < array.length; i++){
            Long.valueOf(array[i]);
        }
        lEnd = System.nanoTime();
        add(lEnd, lStart, "array for loop");

        lStart = System.nanoTime();
        Iterator c = stack.iterator();
        while(c.hasNext()){
         Integer.valueOf((Integer)c.next());
        }
        lEnd = System.nanoTime();
        add(lEnd, lStart, "stack iterator");

        lStart = System.nanoTime();
        Iterator d = stack.iterator();
        System.out.println(stack.size());
        for(; d.hasNext(); )
        {
            Integer.valueOf((Integer)stack.pop());
        }
        lEnd = System.nanoTime();
        add(lEnd, lStart, "stack for loop");


        print();
    }

    private static long duration(long a, long b){
        return b - a;
    }

    private static void add(long lEnd, long lStart, String name){
        long duration = duration(lStart, lEnd);
        keys.add(duration);
        map.put(duration,name);



        /**
         * add alll durations to a list, sort the list.
         *
         * add all durations and names to a map<duration,name>
         *
         *  iterate over the list to get the key
         *   pull the name from the map
         *   print name: duration
         *
         */

        //
    }


    private static void print(){
        Collections.sort(keys);
        for(Long key : keys){
            print(key, map.get(key));
        }
    }
    private static void print(long duration, String name){
        System.out.printf("%20s time: %d\n", name, duration);
    }
}
