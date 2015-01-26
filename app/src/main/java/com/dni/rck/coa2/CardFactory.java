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

    private Card createCard(String bitmapName, String cardDescription, Vector<String> choiceStrings){
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
        card = new Card(bitmap,cardDescription,bounds,textSizePx,charactersPerLine,choiceStrings);
        return card;
    }

    private Vector<Card> createDeck(String cardsTextFile){
        Vector<Card> deck = new Vector<Card>();
        InputStream iS;
        Vector<String> bitmapNames = new Vector<String>();
        Vector<String> descriptions = new Vector<String>();
        Vector<Vector<String>> choiceStrings = new Vector<Vector<String>>();
        Vector<String> choiceString = new Vector<String>();
        BufferedReader reader;
        String sCurrentLine;
        //read names and descriptions from text file and add them respectively to vectors.
        try {
            iS = assetManager.open(cardsTextFile);
            reader = new BufferedReader(new InputStreamReader(iS));
            while ((sCurrentLine = reader.readLine()) != null) {
                if (sCurrentLine.equalsIgnoreCase("bName:"))
                {
                    sCurrentLine = reader.readLine();
                    bitmapNames.add(sCurrentLine);

                    if (!choiceString.isEmpty()) {
                        choiceStrings.add(choiceString);
                       // choiceString.clear();
                    }

                }
                if (sCurrentLine.equalsIgnoreCase("description:"))
                {
                    sCurrentLine = reader.readLine();
                    descriptions.add(sCurrentLine);
                }
                String s = "choice:";
                String sub = sCurrentLine.substring(0,s.length());
                if (sCurrentLine.length()>=s.length())
                    if ((sub).equalsIgnoreCase("choice:"))
                    {
                        System.out.println("found choices");
                        choiceString.add(sCurrentLine);
                    }
            }
            if (!choiceString.isEmpty())choiceStrings.add(choiceString);
        }
        catch (IOException e){
            System.out.println("RCK: error loading cardsTextFile " + cardsTextFile);
        }
        //create cards for each name and description if sizes are equal.
        if(bitmapNames.size() == descriptions.size()){
            String bitmapName, description;
            for (int i = 0; i < bitmapNames.size(); i++)
            {
                bitmapName = bitmapNames.elementAt(i);
                description = descriptions.elementAt(i);
                deck.add(createCard(bitmapName,description,choiceStrings.elementAt(i)));
            }

        }
        else
            System.out.println("RCK: names and descriptions are not equal");
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
        while (card == null)
        {
            card = deck.elementAt(rand.nextInt(deck.size()));
            if (card.bitmap == null)
                card = null;
        }
        if (card !=null){
            currentCard = card;
        }
    }
}
