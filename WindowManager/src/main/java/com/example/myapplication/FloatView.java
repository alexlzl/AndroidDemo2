package com.example.myapplication;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Toast;

/**
 * Created by liuzhouliang on 2017/7/17.
 */

public class FloatView {
    private View view;// 浮动按钮
    private WindowManager wm;
    private Context mContext;

    public FloatView(Context mContext) {
        this.mContext = mContext;
    }

    /**
     * 添加悬浮View
     *
     * @param paddingBottom 悬浮View与屏幕底部的距离
     */
    protected void createFloatView(int paddingBottom,final View view) {
        int w = 200;// 大小
        wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
//        view = LayoutInflater.from(mContext).inflate(R.layout.float_layout, null);
        final WindowManager.LayoutParams params = new WindowManager.LayoutParams();
        params.type = WindowManager.LayoutParams.TYPE_BASE_APPLICATION;// 所有程序窗口的“基地”窗口，其他应用程序窗口都显示在它上面。
        params.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        params.format = PixelFormat.TRANSLUCENT;// 不设置这个弹出框的透明遮罩显示为黑色
        params.width = w;
        params.height = w;
        params.gravity = Gravity.TOP | Gravity.LEFT;
        int screenWidth = mContext.getResources().getDisplayMetrics().widthPixels;
        int screenHeight = mContext.getResources().getDisplayMetrics().heightPixels;
        params.x = screenWidth - w;
        params.y = screenHeight - w - paddingBottom;
//        view.setBackgroundColor(Color.TRANSPARENT);
        view.setBackgroundColor(Color.BLUE);
        view.setVisibility(View.VISIBLE);
        view.setOnTouchListener(new View.OnTouchListener() {
            // 触屏监听
            float lastX, lastY;
            int oldOffsetX, oldOffsetY;
            int tag = 0;// 悬浮球 所需成员变量

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int action = event.getAction();
                float x = event.getX();
                float y = event.getY();
                if (tag == 0) {
                    oldOffsetX = params.x; // 偏移量
                    oldOffsetY = params.y; // 偏移量
                }
                if (action == MotionEvent.ACTION_DOWN) {
                    lastX = x;
                    lastY = y;
                } else if (action == MotionEvent.ACTION_MOVE) {
                    params.x += (int) (x - lastX) / 3; // 减小偏移量,防止过度抖动
                    params.y += (int) (y - lastY) / 3; // 减小偏移量,防止过度抖动
                    tag = 1;
                    wm.updateViewLayout(view, params);
                } else if (action == MotionEvent.ACTION_UP) {
                    int newOffsetX = params.x;
                    int newOffsetY = params.y;
                    // 只要按钮一动位置不是很大,就认为是点击事件
                    if (Math.abs(oldOffsetX - newOffsetX) <= 20
                            && Math.abs(oldOffsetY - newOffsetY) <= 20) {
                        onFloatViewClick();
                    } else {
                        tag = 0;
                    }
                }
                return true;
            }
        });
//        ((ViewGroup) view.getParent()).removeView(view);
        wm.addView(view, params);
    }

    /**
     * 点击浮动按钮触发事件，需要override该方法
     */
    protected void onFloatViewClick() {
        Toast.makeText(mContext,"click",Toast.LENGTH_SHORT).show();
    }

    /**
     * 将悬浮View从WindowManager中移除，需要与createFloatView()成对出现
     */
    protected void removeFloatView() {
        if (wm != null && view != null) {
            wm.removeViewImmediate(view);
//          wm.removeView(view);//不要调用这个，WindowLeaked
            view = null;
            wm = null;
        }
    }

    /**
     * 隐藏悬浮View
     */
    protected void hideFloatView() {
        if (wm != null && view != null && view.isShown()) {
            view.setVisibility(View.GONE);
        }
    }

    /**
     * 显示悬浮View
     */
    protected void showFloatView() {
        if (wm != null && view != null && !view.isShown()) {
            view.setVisibility(View.VISIBLE);
        }
    }
}
