package com.tuanbi97.miniproject1.MainMenu;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;
import android.util.Log;
import android.widget.Toast;

import com.tuanbi97.miniproject1.ImageEx;
import com.tuanbi97.miniproject1.R;

/**
 * Created by User on 6/16/2017.
 */

class MenuCam extends MenuCustom {

    public MenuCam(Context context, int initLeft, int initTop) {
        left = initLeft;
        top = initTop;
        this.context = context;
        init();
    }

    private void init() {
        dw = Resources.getSystem().getDisplayMetrics().widthPixels;
        dh = Resources.getSystem().getDisplayMetrics().heightPixels;
        ims = new ImageEx[5];
        ims[0] = new ImageEx(context, R.drawable.menucambg);
        ims[1] = new ImageEx(context, R.drawable.menucamcamera);
        ims[2] = new ImageEx(context, R.drawable.menucamfg);
        ims[3] = new ImageEx(context, R.drawable.menucamcaption);
        ims[4] = new ImageEx(context, R.drawable.menucamlogo);

        double scaleratio = (((double) dw) / (double)ims[0].getWidth());
        for (int i = 0; i < 5; i++){
            ims[i].setScale(scaleratio);
        }

        ims[1].setMoveAnim(0, dh + ims[1].getHeight(), 0, dh - ims[1].getHeight());
        ims[2].setMoveAnim(dw/4, -ims[2].getHeight(), dw/4, 0);
        ims[3].setMoveAnim(-ims[3].getWidth(), dh/8, dw/4, dh/8);
        ims[3].setTransAnim(0, 255);
        ims[4].setScaleAnim(0, 1);

        ims[0].set(0, 0);
        ims[1].set(0, dh - ims[1].getHeight());
        ims[2].set(dw/4, 0);
        ims[3].set(dw/4, dh/8);
        ims[4].set(dw/2 - ims[4].getWidth()/2, dh/2 - ims[4].getHeight()/2 + ims[4].getHeight()/4);

        for (int i = 0; i < 5; i++){
            ims[i].setCurrent(ims[i].getLeft(), ims[i].getTop());
            //Log.d("ImsCam", Integer.toString(i) + ":" + Integer.toString(ims[i].getWidth()) + " " + Integer.toString(ims[i].getHeight()));
        }
    }


    @Override
    protected void update(int offset) {
        if (left + offset <= 0 && left + offset >= -2*dw) {
            left += offset;
        }
    }

    @Override
    protected void update(double ratio) {
        super.update(ratio);
    }

    @Override
    protected void update(int left, int top) {

    }

    @Override
    protected void draw(Canvas canvas, int offset) {
        super.draw(canvas, offset);
    }

    @Override
    protected int getLeft() {
        return left;
    }

    @Override
    protected int getTop() {
        return top;
    }

    @Override
    protected boolean checkClick(Point p) {
        if (p.x >= ims[4].getLeft() && p.y >= ims[4].getTop() && p.x <= ims[4].getLeft() + ims[4].getWidth() && p.y <= ims[4].getTop() + ims[4].getHeight()){
            //Toast.makeText(context, "MenuCam", Toast.LENGTH_SHORT).show();
            return true;
        }
        return false;
    }
}
