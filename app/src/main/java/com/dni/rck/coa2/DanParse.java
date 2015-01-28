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
        createDeck(cardTextFile);
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

    private void createDeck(String cardTextFile){
        //here is where your magicks would go...

        String fileStr = getCardString(cardTextFile);
        Log.i("DGK", fileStr);

        //Pattern p = Pattern.compile("Hello, (\\S+)");
        //Matcher m = p.matcher();
        //while (m.find()) { // Find each match in turn; String can't do this.
        //    String name = m.group(1); // Access a submatch group; String can't do this.
        //}
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
                pile += line;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return pile;
    }
}
