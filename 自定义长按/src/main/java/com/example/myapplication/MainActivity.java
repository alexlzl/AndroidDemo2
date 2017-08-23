package com.example.myapplication;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    int startX;
    int startY;
    long startTime;
    Handler mHandler;
    int PRESSREANGE=1000;
    int CLICKRANGE=20;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mHandler=new Handler();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
//                touchView = null;
          Log.e("tag","ACTION_DOWN");
                startX = (int) event.getX();
                startY = (int) event.getY();
                startTime = event.getDownTime();

              /* 长按操作 */
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
//                            longPressedAlertDialog();
                        Toast.makeText(MainActivity.this,"long",Toast.LENGTH_LONG).show();
                    }
                }, PRESSREANGE);
                break;
            case MotionEvent.ACTION_MOVE:
                Log.e("tag","ACTION_MOVE");
                int lastX = (int) event.getX();
                int lastY = (int) event.getY();
                //移动
//                moveView(lastX, lastY);

                if (Math.abs(lastX - startX) > CLICKRANGE || Math.abs(lastY - startY) > CLICKRANGE) {
                    this.mHandler.removeCallbacksAndMessages(null);
                }

                break;
            case MotionEvent.ACTION_UP:
                Log.e("tag","ACTION_UP");
                this.mHandler.removeCallbacksAndMessages(null);
                break;
        }
        return true;


    }
}
