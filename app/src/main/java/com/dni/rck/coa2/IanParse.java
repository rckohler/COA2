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
 * Created by rck on 1/27/2015.
 */
public class IanParse {
    private static IanParse instance = null;
    private static final float MYTEXTSIZE = 18.0f;
    MainActivity main;
    AssetManager assetManager;
    Random rand = new Random();
    Vector<Card> deck;
    RectF bounds;
    Card currentCard;
    private IanParse(Context context, String cardTextFile){
        main = (MainActivity)context;
        bounds = new RectF(0,0,main.screenWidth,main.screenHeight);
        assetManager = main.getAssets();

        createMetaStrings(createString(cardTextFile));
    }
    public static IanParse getInstance(Context context, String cardTextFile){
        if (instance == null)
            instance = new IanParse(context, cardTextFile);
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

    private void createMetaStrings(String textFileAsString){
        Vector<String>metaStrings = new Vector<>();
        Pattern p = Pattern.compile("eventID: (.+?)endEvent");//.+ grab all stuff  //w stands for word characters //? grabs shortest sequence
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
        String eventId, description;

    }

}
