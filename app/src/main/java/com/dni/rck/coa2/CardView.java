package com.dni.rck.coa2;

import android.content.Context;
import android.graphics.Canvas;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by rck on 1/24/2015.
 */
public class CardView extends View {
    MainActivity main;
    CardFactory cardFactory;
    public CardView(Context context) {
        super(context);
        main = (MainActivity)context;
        cardFactory = main.cardFactory;
        cardFactory.chooseRandomCard();

    }
    public boolean onTouchEvent(MotionEvent event) {
        int eventaction = event.getAction();
        switch (eventaction) {
            case MotionEvent.ACTION_DOWN:
                cardFactory.chooseRandomCard();
                float clickX = event.getX();
                float clickY = event.getY();

                // finger touches the screen
        }
        return true;
    }
    private void processClick(float clickX, float clickY){

    }
    protected void onDraw(Canvas canvas) {
        if(cardFactory.currentCard!=null)
            if (cardFactory.currentCard.bitmap != null)
                cardFactory.currentCard.update(canvas);
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) { }
        invalidate();
    }
}
