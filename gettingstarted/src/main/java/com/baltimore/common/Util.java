package com.baltimore.common;

import java.util.Arrays;
import java.util.List;

/**
 * Created by paul on 17.08.18.
 */
public class Util {


    public static void release(List list) {
        list.clear();
    }


    public static boolean arrayHas(String[] arr, String ... keys ) throws IndexOutOfBoundsException{
        List targets = Arrays.asList(keys);
        List list =  Arrays.asList(arr);
        return list.containsAll(targets);

    }



}
