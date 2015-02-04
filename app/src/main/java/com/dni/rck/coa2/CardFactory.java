package com.dni.rck.coa2;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.RectF;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Random;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by rck on 1/24/2015.
 */
public class CardFactory {
    private static CardFactory instance = null;
    MainActivity main;
    private static final float MYTEXTSIZE = 18.0f;
    AssetManager assetManager;
    Random rand = new Random();
    Vector<Card> deck;
    RectF bounds;
    Card currentCard;

    private CardFactory(Context context, String cardTextFile){
        main = (MainActivity)context;
        bounds = new RectF(0,0,main.screenWidth,main.screenHeight);
        assetManager = main.getAssets();
        deck = new Vector<>();
        createMetaStrings(createString(cardTextFile));
    }
    public static CardFactory getInstance(Context context, String cardTextFile){
        if (instance == null)
            instance = new CardFactory(context, cardTextFile);
        return instance;
    }
    private String createString(String cardsTextFile){
        String ret="",sCurrentLine;
        BufferedReader reader;
        InputStream iS;

        try {
            iS = assetManager.open(cardsTextFile);
            reader = new BufferedReader(new InputStreamReader(iS));
            while ((sCurrentLine = reader.readLine()) != null) {
                ret+=sCurrentLine;
            }
        }
        catch (IOException e){
            System.out.println("RCK: error loading cardsTextFile " + cardsTextFile);
        }
        return ret;
    }
    private void createMetaStrings(String textFileAsString){
        Vector<String>metaStrings = new Vector<>();
        Pattern p = Pattern.compile("(eventID:.+?)endEvent");//.+ grab all stuff  //w stands for word characters //? grabs shortest sequence
        Matcher m = p.matcher(textFileAsString);
        while (m.find()) {
            String name = m.group(1);
            metaStrings.add(name);
        }
        parseMetaStrings(metaStrings);
    }
    private void parseMetaStrings(Vector<String> metaStrings){
        for (int i = 0; i < metaStrings.size(); i++){
            parseMetaString(metaStrings.elementAt(i));
        }
    }
    private void parseMetaString(String metaString){
        String eventId="", description="", choiceDescription = "", bitmapName ="";
        String choiceName = "";
        Vector<Choice> choices;
        choices = new Vector<>();
        Pattern p = Pattern.compile("eventID: (.+?)bitmap: (.+?)description::(.+?):::(.+?):::::");//.+ grab all stuff  //w stands for word characters //? grabs shortest sequence
        int[]probabilities = new int[4];
        String[]destinations = new String[4];
        Matcher m;
        m = p.matcher(metaString);
        while (m.find()) {
            eventId = m.group(1);
            bitmapName = m.group(2);
            description = m.group(3);
            choiceDescription = m.group(4)+"::";
        }
        p = Pattern.compile("choice: (.+?)outcomes::(.+?)::");//.+ grab all stuff  //w stands for word characters //? grabs shortest sequence
        m = p.matcher(choiceDescription);

        while (m.find()){
            choices.add(new Choice(m.group(1),m.group(2)));
        }
        deck.add(createCard(bitmapName,description,choices,eventId));

    }

    private Card createCard(String bitmapName, String cardDescription, Vector<Choice> choices, String eventID){
        Card card = null;
        InputStream iS;
        Bitmap bitmap = null;
        int charactersPerLine = 32;
// Get the screen's density scale
        final float scale = main.getResources().getDisplayMetrics().density;
// Convert the dps to pixels, based on density scale
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
    public void setCurrentCardToEventId(String eventId){
        for (int i = 0; i < deck.size(); i++){
            if (deck.elementAt(i).eventID.equalsIgnoreCase(eventId))
                currentCard = deck.elementAt(i);
        }
    }


    public void deckReport(){
        for (int i = 0; i < deck.size(); i++)
        {
            System.out.println("RCK: "+ deck.elementAt(i).description);
        }
    }
    public void chooseRandomCard(){
        Card card = null;
        if(deck.size()>0) {
            while (card == null) {
                card = deck.elementAt(rand.nextInt(deck.size()));
                if (card.bitmap == null)
                    card = null;
            }
            if (card != null) {
                currentCard = card;
            }
        }
    }
}