package com.example.android.view;

import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by newpouy on 15. 1. 22.
 */
public class SendDrawGestureListener implements View.OnDragListener, View.OnTouchListener {
    @Override
    public boolean onDrag(View v, DragEvent event) {
        return false;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return false;
    }
}
