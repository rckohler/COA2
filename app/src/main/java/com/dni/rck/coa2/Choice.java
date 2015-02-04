package com.dni.rck.coa2;

import android.graphics.RectF;

import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by rck on 1/25/2015.
 */
public class Choice{
    public static int MAX_CHOICES = 4;
    int[]probability = new int[MAX_CHOICES];
    String[]destination = new String[MAX_CHOICES];
    Random rand = new Random();
    String description;

    public Choice (String choiceString){

    }
    public Choice(String description, int[]probability, String[]destination) {
        this.probability = probability;
        this.destination = destination;
        this.description = description;
    }
    public Choice (String description, String outcomeString){
        outcomeString+="1:"; //gives end signal at end of line
        this.description = description;
        Pattern p = Pattern.compile("(\\d+):(\\D+)"); //THIS IS ALL FUCKED UP...
        Matcher m = p.matcher(outcomeString);
        int i = 0;
        while (m.find()){ //currently only finding first match...
            destination[i] =m.group(2);
            probability[i] =Integer.parseInt(m.group(1));
            i++;
        }
    }
    public String processClick(){
        String ret ="";
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
