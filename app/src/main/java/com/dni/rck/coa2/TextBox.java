package com.dni.rck.coa2;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

import java.util.Vector;

/**
 * Created by rck on 1/24/2015.
 */
public class TextBox {
    Vector<String> words;
    Vector<String> lines;
    RectF bounds;
    Paint paint;
    int charactersPerLine;

    public TextBox(RectF bounds, String text, int textSize, int charactersPerLine){
        this.bounds = bounds;
        this.charactersPerLine = charactersPerLine;
        words = new Vector<String>();
        lines = new Vector<String>();
        paint = new Paint();
        paint.setTextSize(textSize);
        parseIntoWords(text);
        parseIntoLines();
       // sayWords();
        sayLines();
    }
    public boolean isClicked(float clickX, float clickY){
        if (bounds.contains(clickX,clickY)) return true;
        else return false;
    }
    private void parseIntoWords(String text){
        int startSpace = 0;
        int finishSpace =0;
        int stringLength = text.length();
        boolean cont = true;
        while (text.indexOf(" ",startSpace)!=-1){
            finishSpace=text.indexOf(" ",startSpace);

            String sub = text.substring(startSpace,finishSpace);
            if (sub.length()>charactersPerLine)
                System.out.println("RCK: word longer than line in textBox");
            words.add(sub);
            startSpace = finishSpace+1;
        }
        String sub = text.substring(finishSpace+1,stringLength);
        words.add(sub);
    }
    private void parseIntoLines(){
        int lineSize = 0;
        int wordsPlaced = 0;
        String currentLine = "";
        int nextWordSize;
        while(wordsPlaced < words.size()){
            nextWordSize = words.elementAt(wordsPlaced).length();
            while(lineSize+nextWordSize < charactersPerLine && wordsPlaced < words.size()){
                lineSize += words.elementAt(wordsPlaced).length();
                currentLine+=" " + words.elementAt(wordsPlaced);
                nextWordSize = words.elementAt(wordsPlaced).length();

                wordsPlaced++;
                if(wordsPlaced<words.size())nextWordSize=words.elementAt(wordsPlaced).length();
            }
            lines.add(currentLine);
            lineSize = 0;
            currentLine ="";
        }
    }
    private void sayWords(){
        for (int i = 0; i < words.size(); i++)
            System.out.println("RCK: " + words.elementAt(i));
    }
    private void sayLines(){
        for (int i = 0; i < lines.size(); i++){
            System.out.println("RCK: " + lines.elementAt(i));
        }
    }
    public void paintLines(Canvas canvas) {
        float xPos,yPos;
        for (int i = 0; i < lines.size(); i++) {
            xPos = bounds.left+bounds.width()*.05f;
            yPos = bounds.top +bounds.height()*((i+1)*.05f);
            canvas.drawText(lines.elementAt(i),xPos,yPos,paint);
        }

    }
    public void update(Canvas canvas){
        paintLines(canvas);
        }
}
