package com.example.android.view;

import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.example.android.bluetoothchat.BluetoothFragment;
import com.example.android.bluetoothchat.BluetoothService;

/**
 * Created by newpouy on 15. 1. 22.
 */
public class SendPhoneNumGestureListener implements View.OnDragListener, View.OnTouchListener {

    private TextView touchX;
    private TextView touchY;
    private BluetoothService mChatService;
    private String phoneNum;
    private BluetoothFragment bluetoothFragment;

    float firstTouchY = 0;
    float lastTouchY = 0;

    @Override
    public boolean onDrag(View v, DragEvent event) {
        return false;
    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {
        int pointerIndex = event.getActionIndex();
        int pointerID = event.getPointerId(pointerIndex);
        float touchXValue = event.getX(pointerIndex);
        float touchYValue = event.getY(pointerIndex);
        touchX.setText(String.valueOf(touchXValue));
        touchY.setText(String.valueOf(touchYValue));


        if(touchYValue<=0){
            if(event.getAction()==MotionEvent.ACTION_UP){//제스쳐 한번에 한번의 데이터 전송만 하기 위해.
                //sendSignal(phoneNum;);
            }
        }
        return false;
    }

}
