package com.example.android.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.AnimationDrawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.DragEvent;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;

/**
 * Created by newpouy on 15. 1. 27.
 */
public class FlingImageView extends ImageView {
    final static String TAG = "fiv";
    GestureDetector gestureDetector;

    DrawView drawView;

    public FlingImageView(Context context, final DrawView drawView, final Bitmap bitmap) {
        super(context);
        this.drawView = drawView;
        gestureDetector = new GestureDetector(getContext(), new GestureDetector.OnGestureListener() {
            @Override
            public boolean onDown(MotionEvent e) {
                return true;
            }

            @Override
            public void onShowPress(MotionEvent e) {

            }

            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return false;
            }

            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                return false;
            }

            @Override
            public void onLongPress(MotionEvent e) {
                Log.d(TAG,"onLongPress");
            }

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                Log.d(TAG,"onFling");
                if(velocityY<-10.0f){
                    Log.d(TAG,"h3");
                    drawView.tryFling = true;
                    startAnimation(getFlingAnimation());
                    Thread thread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            drawView.sendImage(bitmap);
                        }
                    });
                    thread.start();
                }else{
                    Log.d(TAG,"too slow");
                }
                return false;
            }
        });
        gestureDetector.setIsLongpressEnabled(false);

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return gestureDetector.onTouchEvent(event);
    }

    public Animation getFlingAnimation(){
        Animation toUp = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, -5.0f);
        toUp.setDuration(500);
        toUp.setInterpolator(new LinearInterpolator());
        return toUp;
    }
}
