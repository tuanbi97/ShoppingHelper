package com.tuanbi97.miniproject1.MainMenu;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Point;

import com.tuanbi97.miniproject1.ImageEx;

/**
 * Created by User on 6/16/2017.
 */

abstract class MenuCustom {
    protected int left, top;
    protected Context context;
    protected ImageEx[] ims;
    protected int dw, dh;

    protected abstract void update(int offset);
    protected void update(double ratio){
        for (int i = 0; i < ims.length; i++){
            ims[i].update(ratio);
        }
    }
    protected abstract void update(int left, int top);
    protected void draw(Canvas canvas, int offset){
        for (int i = 0; i < ims.length; i++){
            ims[i].draw(context, canvas, offset);
        }
    }
    protected abstract int getLeft();
    protected abstract int getTop();

    protected abstract boolean checkClick(Point p);
}
