package com.dni.rck.coa2;

import android.graphics.RectF;

import java.util.Random;

/**
 * Created by rck on 1/25/2015.
 */
public class Choice{
    public static int MAX_CHOICES = 4;
    int[]probability = new int[MAX_CHOICES];
    String[]destination = new String[MAX_CHOICES];
    Random rand = new Random();
    String description;

    public Choice(String description, int[]probability, String[]destination) {
        this.probability = probability;
        this.destination = destination;
        this.description = description;
    }
    public String processClick(float clickX, float clickY){
        String ret ="";
        //jer 
        int possibleOutcomes = probability.length;
        int totalOfProbabilityValues = 0;
        for(int i = 0; i < possibleOutcomes; i++){
            totalOfProbabilityValues+=probability[i];
        }
        int roll = rand.nextInt(totalOfProbabilityValues);

        int checkChoice = 0;
        for (int i=0; i < MAX_CHOICES; i++){
            checkChoice += probability[i];
            if (roll < checkChoice){
                ret = destination[i];
                break;
            }
        }
        if (ret.equalsIgnoreCase(""))
            System.out.println("RCK: error in processClick ret was not set to a valid value");

        return ret;
    }
}
