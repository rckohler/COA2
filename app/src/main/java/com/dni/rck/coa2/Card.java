package com.dni.rck.coa2;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.RectF;

import java.util.Vector;

/**
 * Created by rck on 1/24/2015.
 */
public class Card {
    int storyID;
    String description;
    Bitmap bitmap;
    RectF bounds;
    TextBox textBox;
    Vector<Choice> choices = new Vector<Choice>();

    public Card(Bitmap bitmap, String description, RectF bounds, int textSize, int charactersPerLine, Vector<String> choiceStrings){
        this.description = description;
        this.bitmap = bitmap;
        this.bounds = bounds;
        this.choices = choices;
        RectF descriptionBounds = new RectF(bounds.left,bounds.top+bounds.height()*.1f, bounds.right, bounds.height()*.5f);
        textBox = new TextBox(descriptionBounds,description,textSize, charactersPerLine);
    }
    private void updateChoices(Canvas canvas){
        for (int i = 0; i < choices.size(); i++){
            choices.elementAt(i).update(canvas);
        }
    }
    public int processClick(float clickX, float clickY){
        int ret = -1;
        for (int i = 0; i < choices.size(); i++ ){
        if (choices.elementAt(i).isClicked(clickX,clickY))
            ret = choices.elementAt(i).processClick(clickX,clickY);
        }
        return ret;
    }

    public void update(Canvas canvas){
        if (bitmap != null)
            canvas.drawBitmap(bitmap,null,bounds,null);
        textBox.update(canvas);
        updateChoices(canvas);

    }
}
