package com.dni.rck.coa2;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.view.MotionEvent;
import android.view.View;

import java.util.Random;

/**
 * Created by rck on 1/24/2015.
 */
public class CardView extends View {
    MainActivity main;
    CardFactory cardFactory;
    Random rand = new Random();
    Typeface typeface;
    public CardView(Context context) {
        super(context);
        main = (MainActivity)context;

        cardFactory = main.cardFactory;
        cardFactory.currentCard = cardFactory.deck.elementAt(0);

    }
    public boolean onTouchEvent(MotionEvent event) {
        int eventaction = event.getAction();
        switch (eventaction) {
            case MotionEvent.ACTION_DOWN:
                //cardFactory.chooseRandomCard();
                float clickX = event.getX();
                float clickY = event.getY();
                processClick(clickX,clickY);

                // finger touches the screen
        }
        return true;
    }
    private void processClick(float clickX, float clickY) {
        String destination="";
        for (int i = 0; i < cardFactory.currentCard.choices.size(); i++) {
            if (cardFactory.currentCard.choiceTextBoxes.elementAt(i).isClicked(clickX, clickY)) {
                destination = cardFactory.currentCard.choices.elementAt(i).processClick();
            }

        }
        if (destination != "")
            cardFactory.setCurrentCardToEventId(destination);
    }

    private void drawEach(Canvas canvas){
        int r = rand.nextInt(cardFactory.deck.size());
        Card card = cardFactory.deck.elementAt(r);
        card.update(canvas);
    }
    protected void onDraw(Canvas canvas) {
       //drawEach(canvas);
        if(cardFactory.currentCard.bitmap!=null){
            cardFactory.currentCard.update(canvas);
        }
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) { }
        invalidate();

    }
}
