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
        deck = createDeck(cardTextFile);
        System.out.println("RCK: deck built");
    }
    public static CardFactory getInstance(Context context, String cardTextFile){
        if (instance == null)
            instance = new CardFactory(context, cardTextFile);
        return instance;
    }
    private Vector<Entry> createEntries(String cardsTextFile){
        InputStream iS;
        Vector<Entry> entries = new Vector<Entry>();
        BufferedReader reader;
        String sCurrentLine;
        //read names and descriptions from text file and add them respectively to vectors.
        try {
            iS = assetManager.open(cardsTextFile);
            reader = new BufferedReader(new InputStreamReader(iS));
            while ((sCurrentLine = reader.readLine()) != null) {
                entries.add(new Entry(sCurrentLine));
            }
        }
        catch (IOException e){
            System.out.println("RCK: error loading cardsTextFile " + cardsTextFile);
        }
        return entries;
    }
    private Card createCard(String bitmapName, String cardDescription, Vector<Choice> choices){
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
        card = new Card(bitmap,cardDescription,bounds,textSizePx,charactersPerLine,choices);
        return card;
    }

    private Vector<Card> createDeck(String cardsTextFile){
        Vector<Card> deck = new Vector<Card>();
        Vector<Entry> entries = createEntries(cardsTextFile);
        Vector<Choice> choices = new Vector<Choice>();
        String bName="",description="";
        for(int i = 0; i < entries.size(); i++){
            Entry entry = entries.elementAt(i);
            if(entry.type.equalsIgnoreCase("bName"))
            {
                bName = entry.content.substring(1);
                if (bName.isEmpty() || description.isEmpty()||choices.isEmpty()){
                    System.out.println("RCK: Incomplete data group for entry " +entry.type +":"+entry.content );
                }
                else{
                    deck.add(createCard(bName,description,choices));
                    choices.clear();
                }
            }
            if(entry.type.equalsIgnoreCase("description"))
            {
                description = entry.content;
            }
            if(entry.type.contains("choice")){
                int[] probabilities = new int[4];
                String[] destinations = new String[4];
                String choiceDescription = entry.content;

                int arrayCounter=0;
                boolean inChoiceLoop = true;
                String type;
                int p;
                String test = "bName";
                i++;
                entry = entries.elementAt(i);

                while (inChoiceLoop && i < entries.size()){

                        entry = entries.elementAt(i);
                        type= entry.type.substring(0,1);
                        try {
                            p = Integer.parseInt(type);
                            probabilities[arrayCounter]=p;
                            destinations[arrayCounter]=entry.content;
                            arrayCounter++;
                            i++;
                        }
                        catch (NumberFormatException e) {
                            inChoiceLoop = false;
                        }
                }
                choices.add(new Choice(choiceDescription,probabilities,destinations));
            }

        }
        deck.add(createCard(bName,description,choices)); // handles the last card

        return deck;
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
