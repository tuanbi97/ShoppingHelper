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

class MenuMap extends MenuCustom {
    public MenuMap(Context context, int initLeft, int initTop) {
        left = initLeft;
        top = initTop;
        this.context = context;
        init();
    }

    private void init() {
        dw = Resources.getSystem().getDisplayMetrics().widthPixels;
        dh = Resources.getSystem().getDisplayMetrics().heightPixels;
        ims = new ImageEx[7];
        ims[0] = new ImageEx(context, R.drawable.menumapbg);
        ims[1] = new ImageEx(context, R.drawable.menumapfooter);
        ims[2] = new ImageEx(context, R.drawable.menumapfooter);
        ims[3] = new ImageEx(context, R.drawable.menumapcaption);
        ims[4] = new ImageEx(context, R.drawable.menumaptitle);
        ims[5] = new ImageEx(context, R.drawable.menumapcircle);
        ims[6] = new ImageEx(context, R.drawable.menumapmarker);

        double scaleratio = (((double) dw) / (double)ims[0].getWidth());
        for (int i = 0; i < 7; i++){
            ims[i].setScale(scaleratio);
        }
        /*for (int i = 0; i < 2; i++){
            Toast.makeText(context, Integer.toString(ims[i].getWidth()) + " " + Integer.toString(ims[i].getHeight()), Toast.LENGTH_SHORT).show();
        }*/

        ims[1].setMoveAnim(0, -ims[1].getHeight(), 0, -2*ims[1].getHeight()/5);
        ims[1].setTransAnim(255, 150);
        ims[2].setMoveAnim(0, dh, 0, 2*dh/3);
        ims[2].setTransAnim(255, 150);
        ims[3].setTransAnim(0, 255);
        ims[4].setMoveAnim(dw/2 - ims[4].getWidth()/2, -ims[4].getHeight(), dw/2 - ims[4].getWidth()/2, 0);
        ims[5].setScaleAnim(0, 1);
        ims[6].setScaleAnim(0, 1);

        ims[0].set(0, 0);
        ims[1].set(0, -2*ims[1].getHeight()/5, 150);
        ims[2].set(0, 2*dh/3, 150);
        ims[3].set(dw/2 - ims[3].getWidth()/2, 4*dh/5);
        ims[4].set(dw/2 - ims[4].getWidth()/2, 0);
        ims[5].set(dw/2 - ims[5].getWidth()/2, dh/2 - ims[5].getHeight()/2 + ims[5].getHeight()/14);
        ims[6].set(dw/2 - ims[6].getWidth()/2, dh/2 - ims[6].getHeight()/2 + ims[5].getHeight()/14);

        for (int i = 0; i < 7; i++){
            ims[i].setCurrent(ims[i].getLeft(), ims[i].getTop());
            //Log.d("Ims", Integer.toString(i) + ":" + Integer.toString(ims[i].getWidth()) + " " + Integer.toString(ims[i].getHeight()));
        }

    }

    @Override
    protected void update(int offset) {
        if (left + offset <= dw && left + offset >= -dw) {
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
        if (p.x >= ims[6].getLeft() && p.y >= ims[6].getTop() && p.x <= ims[6].getLeft() + ims[6].getWidth() && p.y <= ims[6].getTop() + ims[6].getHeight()){
            //Toast.makeText(context, "MenuMap", Toast.LENGTH_SHORT).show();
            return true;
        }
        return false;
    }
}
