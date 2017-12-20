package com.tuanbi97.miniproject1.MainMenu;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Point;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

import static java.lang.Math.abs;

/**
 * Created by User on 6/16/2017.
 */

public class MainMenu extends View{
    private Context context;
    private MenuCustom[] menu;
    private int dw, dh;
    private int currentMenu = 1;
    private TimerTask timerTask;
    private Timer timer;
    private OnTouchListener touchListener;
    private OnListener Listener;
    private int oldLeft;
    ValueAnimator animator;

    public MainMenu(Context context) {
        super(context);
        this.context = context;
        dw = Resources.getSystem().getDisplayMetrics().widthPixels;
        dh = Resources.getSystem().getDisplayMetrics().heightPixels;
        //Toast.makeText(context, Integer.toString(dw) + " " + Integer.toString(dh), Toast.LENGTH_SHORT).show();
        initMenu();
        setTimerTask();
        setListener();
        setAnimator();
    }

    private void setAnimator() {
        animator = new ValueAnimator();
        animator.setDuration(600l);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float newLeft = (float) animation.getAnimatedValue();
                float diff = newLeft - menu[currentMenu].getLeft();
                for (int i = 0; i < 3; i++){
                    //Log.d("Animatef", Integer.toString(i));
                    //Log.d("AnimateUpdate", Double.toString(abs((1.0 * menu[i].getLeft())/(1.0 * dw))));
                    menu[i].update(abs((1.0 * menu[i].getLeft())/(1.0 * dw)));
                    //Log.d("AnimateValue", Float.toString(newLeft));
                    menu[i].update((int)diff);
                    //Log.d("AnimateValue", Float.toString(menu[i].getLeft()));
                }
            }
        });
    }

    private void setListener() {
        touchListener = new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        oldLeft = (int)event.getX();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        int newLeft = (int) event.getX();
                        if (newLeft != oldLeft){
                            for (int i = 0; i < 3; i++){
                                menu[i].update(abs((1.0*menu[i].getLeft())/(1.0*dw)));
                                menu[i].update(newLeft - oldLeft);
                            }
                            oldLeft = newLeft;
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        if (menu[currentMenu].getLeft() != 0){
                            int mLeft = menu[currentMenu].getLeft();
                            if (mLeft > dw / 4){
                                currentMenu -= 1;
                            }
                            else
                            if (mLeft < -dw / 4){
                                currentMenu += 1;
                            }
                            animator.setFloatValues(menu[currentMenu].getLeft(), 0);
                            animator.start();
                        }
                        else{
                            //click action
                            if (menu[currentMenu].checkClick(new Point((int) event.getX(), (int) event.getY()))) {
                                Listener.onMenuClick(currentMenu);
                            }
                        }
                        break;
                }
                return true;
            }
        };
        setOnTouchListener(touchListener);
    }

    @Override
    protected void onDraw(Canvas canvas) {

        for (int i = 0; i < 3; i++){
            menu[i].draw(canvas, menu[i].getLeft());
        }
    }

    private void setTimerTask() {
        timerTask = new TimerTask() {
            @Override
            public void run() {
                postInvalidate();
            }
        };
        timer = new Timer();
        timer.schedule(timerTask, 0, 40);
    }

    private void initMenu() {
        menu = new MenuCustom[3];
        menu[0] = new MenuCam(context, -dw, 0);
        menu[1] = new MenuMap(context, 0, 0);
        menu[2] = new MenuShare(context, dw, 0);
    }

    public void setListener(OnListener listener){
        Listener = listener;
    }
}
