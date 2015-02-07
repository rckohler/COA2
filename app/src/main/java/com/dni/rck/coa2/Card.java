package com.dni.rck.coa2;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.RectF;
import android.graphics.Typeface;

import java.util.Vector;

/**
 * Created by rck on 1/24/2015.
 */
public class Card {
    int storyID;
    String description;
    Bitmap bitmap;
    String eventID;
    RectF bounds;
    TextBox textBox;
    Vector<Choice> choices = new Vector<>();
    Vector<TextBox> choiceTextBoxes = new Vector<>();
    int textSize;
    public Card(Bitmap bitmap, String description, RectF bounds, int textSize, int charactersPerLine, Vector<Choice> choices, String eventID,Typeface typeface){
        this.description = description;
        this.bitmap = bitmap;
        this.bounds = bounds;
        this.choices = choices;
        this.textSize = textSize;
        RectF descriptionBounds = new RectF(bounds.left,bounds.top+bounds.height()*.1f, bounds.right, bounds.height()*.5f);
        textBox = new TextBox(descriptionBounds,description,textSize, charactersPerLine,typeface);
        createChoiceTextBoxes(typeface);
        this.eventID = eventID;
    }

    public void setStoryID(int storyID){
        this.storyID = storyID;
    }
    private void createChoiceTextBoxes(Typeface typeface){
        String description;
        RectF choiceBounds;
        TextBox textBox;
        int charactersPerLine = 32;
        float left, top, right, bottom, spacer = bounds.width()*.05f;
        for (int i = 0; i < choices.size(); i++){
            description = choices.elementAt(i).description;
            left = bounds.left+spacer;
            right = bounds.right-spacer;
            if(i == 0) {
                top = bounds.height() * .65f;
            }
            else{
                top = choiceTextBoxes.elementAt(i-1).bounds.bottom+.5f*textSize;
            }
            choiceBounds = new RectF(left, top, right, 0);
            textBox = new TextBox(choiceBounds, description, textSize, charactersPerLine, typeface);
            bottom = top + (textBox.numberOfLines)*textSize*1.5f;
            textBox.bounds.set(left, top, right, bottom);
            choiceTextBoxes.add(textBox);

        }
    }

    public String processClick(float clickX, float clickY){
        String ret = "";
        for (int i = 0; i < choiceTextBoxes.size(); i++ ){
        if (choiceTextBoxes.elementAt(i).isClicked(clickX,clickY))
            ret = choices.elementAt(i).processClick();
        }
        return ret;
    }
    private void drawChoiceTextBoxes(Canvas canvas){
        for (int i = 0; i < choiceTextBoxes.size(); i++){
            choiceTextBoxes.elementAt(i).update(canvas);
        }
    }
    public void update(Canvas canvas){
        if (bitmap != null)
        {
            canvas.drawBitmap(bitmap,null,bounds,null);
        }
        textBox.update(canvas);
        drawChoiceTextBoxes(canvas);
    }
}
