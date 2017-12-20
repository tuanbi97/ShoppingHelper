package com.tuanbi97.miniproject1;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.Log;
import android.widget.Toast;

import static java.lang.Math.abs;
import static java.lang.Math.max;

/**
 * Created by User on 6/16/2017.
 */

public class ImageEx {

    Bitmap bmp;
    Point pStart, pEnd;
    double scaleStart, scaleEnd;
    int tStart, tEnd;
    int cw, ch;
    int left, top;
    int cleft, ctop;
    int w, h;
    double cscale = 1;
    int opacity = 255;
    int dw, dh;
    boolean isMove = false, isScale = false, isTrans = false, isBeat = false;

    public ImageEx(Context context, int RID) {
        bmp = BitmapFactory.decodeResource(context.getResources(), RID);
        w = bmp.getWidth();
        h = bmp.getHeight();
        dw = Resources.getSystem().getDisplayMetrics().widthPixels;
        dh = Resources.getSystem().getDisplayMetrics().heightPixels;
    }

    public void setMoveAnim(int x, int y, int u, int v){
        pStart = new Point(x, y);
        pEnd = new Point(u, v);
        isMove = true;
    }

    public void setScaleAnim(double start, double end) {
        scaleStart = start;
        scaleEnd = end;
        isScale = true;
    }

    public void setTransAnim(int start, int end){
        tStart = start;
        tEnd = end;
        isTrans = true;
    }

    public int getHeight() {
        return h;
    }

    public int getWidth() {
        return w;
    }

    public void set(int x, int y) {
        left = x; top = y;
    }

    public void set(int x, int y, int opacity){
        left = x; top = y; this.opacity = opacity;
    }

    public void set(int x, int y, int opacity, double scale){
        this.cscale = scale;
        left = x; top = y;
        this.opacity = opacity;
    }

    public void set(int opacity, double scale){
        this.cscale = scale;
        this.opacity = opacity;
    }

    public void draw(Context context, Canvas canvas, int offset) {
        Paint p = new Paint();
        p.setAlpha(opacity);

        if (isScale){
            //Log.d("isScale", Double.toString((int)cscale*bmp.getWidth()));
            //Log.d("Animate", Double.toString(cscale));
            Bitmap tmp = Bitmap.createScaledBitmap(bmp, max((int)(cscale*bmp.getWidth()), 1), max((int)(cscale*bmp.getHeight()), 1), false);
            //Toast.makeText(context, Double.toString(cscale) + " " + Integer.toString(tmp.getWidth()) + " " + Integer.toString(tmp.getHeight()), Toast.LENGTH_SHORT).show();
            Rect r = new Rect(offset + cleft, ctop, offset + cleft + tmp.getWidth(), ctop + tmp.getHeight());
            if (outside(r, 0, 0, dw, dh)) return;
            canvas.drawBitmap(tmp, offset + cleft, ctop, p);
        }
        else {
            Rect r = new Rect(offset + cleft, ctop, offset + cleft + bmp.getWidth(), ctop + bmp.getHeight());
            if (outside(r, 0, 0, dw, dh)) return;
            canvas.drawBitmap(bmp, offset + cleft, ctop, p);
        }
    }

    private boolean outside(Rect r, int xs, int ys, int xt, int yt) {
        if (r.right < xs || r.bottom < ys || r.left > xt || r.top > yt) return true;
        return false;
    }

    public int getLeft() {
        return left;
    }

    public int getTop() {
        return top;
    }

    public int getCLeft() {
        return cleft;
    }

    public int getCTop() {
        return ctop;
    }

    public void setCurrent(int left, int top) {
        cleft = left;
        ctop = top;
    }

    public void update(double ratio) {
        if (isMove){
            int len = pEnd.x - pStart.x;
            cleft = (int) (pEnd.x - ratio*len);
            len = pEnd.y - pStart.y;
            ctop = (int) (pEnd.y - ratio*len);
        }
        if (isScale){
            cscale = max(scaleStart + abs((1 - ratio)*(scaleEnd - scaleStart)), 0.1);
            //Log.d("AnimateIsScale", Double.toString(cscale) + " " + Double.toString(ratio));
            cleft = left + (int) ((1 - cscale) * (w/2));
            ctop = top + (int) ((1 - cscale) * (h/2));
        }
        if (isTrans){
            opacity = (int) (tEnd - ratio*(tEnd - tStart));
        }
    }

    public void setScale(double scale) {
        bmp = Bitmap.createScaledBitmap(bmp, (int) (scale*bmp.getWidth()), (int) (scale*bmp.getHeight()), false);
        w = bmp.getWidth();
        h = bmp.getHeight();
    }
}
