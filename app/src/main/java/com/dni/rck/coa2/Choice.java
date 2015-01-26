package com.dni.rck.coa2;

import android.graphics.RectF;

import java.util.Random;

/**
 * Created by rck on 1/25/2015.
 */
public class Choice extends TextBox{
    static int MAX_CHOICES = 4;
    int[]probability = new int[MAX_CHOICES];
    int[]destination = new int[MAX_CHOICES];
    Random rand = new Random();

    public Choice(RectF bounds, String text, int textSize, int charactersPerLine, int[]probability, int[]destination) {
        super(bounds, text, textSize, charactersPerLine);
        this.probability = probability;
        this.destination = destination;
    }
    private void parseChoice(String s){
        int choiceProbability;
        int choiceDestination;
        String choiceDescription;

    }
    public boolean isClicked(float clickX, float clickY){
        boolean ret = false;
        if (bounds.contains(clickX,clickY)) ret = true;
        return ret;
    }
    public int processClick(float clickX, float clickY){
        int ret = -1;
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
        if (ret == -1)
            System.out.println("RCK: error in processClick ret was not set to a valid value");

        return ret;
    }
}
