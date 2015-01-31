package com.dni.rck.coa2;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.RectF;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Random;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by rck on 1/27/2015.
 */
public class DanParse {
    private static DanParse instance = null;
    private static final float MYTEXTSIZE = 18.0f;
    MainActivity main;
    AssetManager assetManager;
    Random rand = new Random();
    Vector<Card> deck;
    RectF bounds;
    Card currentCard;
    private DanParse(Context context, String cardTextFile){
        main = (MainActivity)context;
        bounds = new RectF(0,0,main.screenWidth,main.screenHeight);
        assetManager = main.getAssets();
        try {
            createDeck(cardTextFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static DanParse getInstance() {
        return instance;
    }

    public static DanParse createInstance(Context context, String cardTextFile){
        instance = new DanParse(context, cardTextFile);
        return instance;
    }

    private Card createCard(String bitmapName, String cardDescription, Vector<Choice> choices, String eventID){
        Card card = null;
        InputStream iS;
        Bitmap bitmap = null;
        int charactersPerLine = 32;
//  Get the screen's density scale
        final float scale = main.getResources().getDisplayMetrics().density;
//  Convert the dps to pixels, based on density scale
        int textSizePx = (int) (MYTEXTSIZE * scale + 0.5f);
        try {
            iS = assetManager.open(bitmapName);
            bitmap = BitmapFactory.decodeStream(iS);
        }
        catch (IOException e){
            System.out.println("RCK: error loading bitmap named " + bitmapName);
        }
        card = new Card(bitmap,cardDescription,bounds,textSizePx,charactersPerLine,choices, eventID);
        return card;
    }

    private void createDeck(String cardTextFile) throws Exception {
        //here is where your magicks would go...

        String raw = getCardString(cardTextFile);
        Log.i("DGK", raw);

        final int PATTERN_FLAGS = Pattern.DOTALL | Pattern.MULTILINE;

        //Stuff to match and extract event id and string data
        Pattern eventPattern = Pattern.compile(
                "eventID:\\s*(.+?)\\s*\\n(.+?)\\n\\s*endEvent",
                PATTERN_FLAGS);
        Matcher eventMatcher;
        String eventId, eventData;

        //Stuff to match and extract description from event data
        Pattern descriptionPattern = Pattern.compile(
                "description:\\s*\\{\\$\\s*(.+?)\\s*\\$\\}",
                PATTERN_FLAGS);
        Matcher descriptionMatcher;
        String description;

        //Stuff to match and extract choice-outcome data from event data
        Pattern choiceOutcomePattern = Pattern.compile(
                "choice:\\s*\\{\\$\\s*(.+?)\\s*\\$\\}\\s+?outcomes:\\s*\\{\\$\\s*(.+?)\\s*\\$\\}",
                PATTERN_FLAGS);
        Matcher choiceOutcomeMatcher;
        String choice, outcomeData;

        //Stuff to match and extract outcome prbabilities and destinations from outcome data
        Pattern outcomePattern = Pattern.compile(
                "(\\d+)\\s*:\\s*(.+?)\\s*$",
                PATTERN_FLAGS);
        Matcher outcomeMatcher;
        String outcomeProb, outcomeDest;

        //Stuff to package the extracted data into a class
        final int MAX_CHOICES = 4;
        Vector<Integer> probabilities = new Vector<Integer>(MAX_CHOICES);
        Vector<String>  destinations = new Vector<String>(MAX_CHOICES);

        //Search for events
        eventMatcher = eventPattern.matcher(raw);
        while(eventMatcher.find()) {

            //Extract event id and event data. The event data will be used in subsequent regex matching
            eventId = eventMatcher.group(1);
            eventData = eventMatcher.group(2);
            if(eventId.length() == 0 || eventData.length() == 0) {
                throw(new Exception("Error reading event data"));
            }

            //Extract description
            descriptionMatcher = descriptionPattern.matcher(eventData);
            if(descriptionMatcher.find()) {
                description = descriptionMatcher.group(1);
            } else {
                throw(new Exception("Missing or malformed description of event: " + eventId));
            }

            //Extract all choice-outcome groups
            choice = null;
            choiceOutcomeMatcher = choiceOutcomePattern.matcher(eventData);
            while(choiceOutcomeMatcher.find()) {

                choice = choiceOutcomeMatcher.group(1);
                outcomeData = choiceOutcomeMatcher.group(2);

                //Extract all outcomes
                probabilities.clear();
                destinations.clear();
                outcomeMatcher = outcomePattern.matcher(outcomeData);
                while(outcomeMatcher.find()) {
                    outcomeProb = outcomeMatcher.group(1);		System.out.println(outcomeProb);
                    outcomeDest = outcomeMatcher.group(2);		System.out.println(outcomeDest);

                    probabilities.add(Integer.parseInt(outcomeProb));
                    destinations.add(outcomeDest);
                }
                if(probabilities.isEmpty()) {
                    throw(new Exception("Missing or malformed outcomes of choice '" + choice + "' in event: " + eventId));
                }

                //TODO: Add probabilities and destinations to a Choice data structure, and append to a vector
                //choices.add(new Choice(choice, probabilities, destinations));
                //Clear choices before entering choices loop

            }
            if(choice == null) {
                System.out.println("WARNING: No choices found for event: " + eventId + ". This event is terminal!");
            }
        }

        //TODO: Add all event data to an Event data structure, and append to a HashMap
        //HashMap<String, Event> events = new HashMap<String, Event>();		//define once above
        //events.put(eventId, new Event(description, choices));
    }

    String getCardString(String cardTextFile) {
        InputStream iS;
        BufferedReader reader;
        String pile = "";

        try {
            iS = assetManager.open(cardTextFile);
            reader = new BufferedReader(new InputStreamReader(iS));

            String line;
            while( (line = reader.readLine()) != null) {
                pile += line + "\n";
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return pile;
    }
}
