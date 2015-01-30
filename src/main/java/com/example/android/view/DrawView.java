package com.example.android.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.text.Layout;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;


import com.example.android.bluetoothchat.BluetoothService;
import com.example.android.bluetoothchat.Constants;
import com.example.android.bluetoothchat.R;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by newpouy on 15. 1. 23.
 */
public class DrawView extends View {
    final static String TAG = "dv";
    LongPressChecker longPressChecker;
    ArrayList<Point> points = new ArrayList<Point>();
    Context context;
    Canvas mCanvas;
    Bitmap bitmap;
    boolean tryFling;
    int pWidth;
    int pHeight;
    final ViewGroup mParentView;
    BluetoothService bluetoothService;
    Drawable drawable;
    public DrawView(final Context context, final RelativeLayout parentView, Activity parentActivity, BluetoothService bluetoothService) {
        super(context);
        this.context = context;
        this.bluetoothService = bluetoothService;
        mParentView = parentView;

/*        DisplayMetrics displayMetrics = new DisplayMetrics();
        parentActivity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;// 가로
        int height = displayMetrics.heightPixels;// 세로*/
        longPressChecker = new LongPressChecker(context);
        longPressChecker.setOnLongPressListener(new LongPressChecker.OnLongPressListener() {
            @Override
            public void onLongPressed(View v) {
/*                int pWidth = mParentView.getWidth();
                int pHeight = mParentView.getHeight();
                bitmap = Bitmap.createBitmap(pWidth/2, pHeight/2, Bitmap.Config.ARGB_8888);*/
                FlingImageView newView = new FlingImageView(getContext(), DrawView.this, bitmap);
                Log.d(TAG, mParentView.getWidth()+"is p's w and "+mParentView.getHeight()+" is p's h");
                newView.setImageBitmap(Bitmap.createScaledBitmap(bitmap, bitmap.getWidth() / 2, bitmap.getHeight() / 2, true));
                newView.setBackgroundColor(Color.argb(255, 248, 230, 124));
                mParentView.removeView(v);
                parentView.setHorizontalGravity(Gravity.CENTER);
                parentView.setVerticalGravity(Gravity.BOTTOM);
                mParentView.addView(newView);

                //bitmap파일로 저장하고 불러오기. file객체와 IO stream사용
                //Handler구현해서 플립으로 보내고 나서 수행할 동작 만들고,
                //onFling이 있는 FlingImageView에서 메시지 핸들링을 하지 않고 여기서 한다.
                //그 이유는 이미지를 주고 받는 곳이 여기이기 때문이다.
                //onFling했을 때 flag를 바꾸고 touch이벤트에 fling 플래그가 참이면 보내기 성공, 등의 이벤트 처리를 하는걸로
                //또 이미지를 받는 곳도 이 뷰로 하고, 이미지가 오면 받을지 말지 결정하고 추가편집도 하고..
            }
        });
        setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        this.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                longPressChecker.deliverMotionEvent( v, event );
                switch (event.getAction()) {
                    case MotionEvent.ACTION_MOVE:
                        points.add(new Point(event.getX(), event.getY(), true));
                        invalidate();
                        break;
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_DOWN:
                        points.add(new Point(event.getX(), event.getY(), false));
                }
                return true;
            }
        });

    }
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        pWidth = mParentView.getWidth();
        pHeight = mParentView.getHeight();
        bitmap = Bitmap.createBitmap(pWidth*9/10, pHeight*9/10, Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas(bitmap);
        Paint p = new Paint();
        p.setColor(Color.MAGENTA);
        p.setStrokeWidth(3);
        for(int i=1; i<points.size(); i++) {
            if(!points.get(i).isDraw) continue;
            canvas.drawLine(points.get(i-1).x, points.get(i-1).y, points.get(i).x, points.get(i).y, p);
            mCanvas.drawLine(points.get(i-1).x, points.get(i-1).y, points.get(i).x, points.get(i).y, p);
        }

    }
    public Canvas getmCanvas (){
        return mCanvas;
    }
    class Point {
        float x;
        float y;
        boolean isDraw;
        public Point(float x, float y, boolean isDraw) {
            this.x = x;
            this.y = y;
            this.isDraw = isDraw;
        }
    }
    public void sendImage(Bitmap bitmap){
        Log.d(TAG, "try fling!!");
        bluetoothService.writeBitmap(bitmap);

    }
    public void setBitmap(Bitmap bitmap){
        mCanvas = new Canvas(bitmap);
        this.draw(mCanvas);
    }
    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Constants.MESSAGE_STATE_CHANGE:
                    switch (msg.arg1) {
                        case BluetoothService.STATE_CONNECTED:
                            break;
                        case BluetoothService.STATE_CONNECTING:
                            break;
                        case BluetoothService.STATE_LISTEN:
                        case BluetoothService.STATE_NONE:
                            break;
                    }
                    break;
                case Constants.BITMAP_WRITE:
                    break;
                case Constants.MESSAGE_READ:
                    break;
                case Constants.MESSAGE_DEVICE_NAME:
                    break;
                case Constants.MESSAGE_TOAST:
                    break;
            }
        }
    };
}
