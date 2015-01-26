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
    Vector<Choice> choices = new Vector<>();
    Vector<TextBox> choiceTextBoxes = new Vector<>();

    public Card(Bitmap bitmap, String description, RectF bounds, int textSize, int charactersPerLine, Vector<Choice> choices){
        this.description = description;
        this.bitmap = bitmap;
        this.bounds = bounds;
        this.choices = choices;
        RectF descriptionBounds = new RectF(bounds.left,bounds.top+bounds.height()*.1f, bounds.right, bounds.height()*.5f);
        textBox = new TextBox(descriptionBounds,description,textSize, charactersPerLine);
        createChoiceTextBoxes();
    }
    private void createChoiceTextBoxes(){
        String description;
        RectF choiceBounds;
        int charactersPerLine = 25, textSize = 25;
        float left, top, right, bottom, spacer = bounds.width()*.05f;
        for (int i = 0; i < choices.size(); i++){
            description = choices.elementAt(i).description;
            left = bounds.left+spacer;
            right = bounds.right-spacer;
            top = bounds.height()*.75f;
            bottom = top + 2*spacer;
            choiceBounds = new RectF(left,top,right,bottom);
            choiceTextBoxes.add(new TextBox(choiceBounds,description,textSize,charactersPerLine));
        }
    }

    public String processClick(float clickX, float clickY){
        String ret = "";
        for (int i = 0; i < choiceTextBoxes.size(); i++ ){
        if (choiceTextBoxes.elementAt(i).isClicked(clickX,clickY))
            ret = choices.elementAt(i).processClick(clickX,clickY);
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
            canvas.drawBitmap(bitmap,null,bounds,null);
        textBox.update(canvas);
        drawChoiceTextBoxes(canvas);

    }
}
