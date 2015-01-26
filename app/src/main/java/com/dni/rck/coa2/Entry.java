package com.dni.rck.coa2;

/**
 * Created by rck on 1/25/2015.
 */
public class Entry {
    public String type;
    public String content;
    char divider = ':';

    public Entry(String s){
        int divisionPoint = s.indexOf(divider);
        if (divisionPoint == -1){
            System.out.println("RCK: invalid entry did not contain divider. String = " + s);
        }
        type = s.substring(0,divisionPoint);
        content = s.substring(divisionPoint+1);
    }
}
