package com.gome.friendcircle.helper;

import android.content.Context;
import android.graphics.PixelFormat;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

/**
 * Created by liuzhouliang on 2017/7/18.
 * 管理遮罩层视图
 */

public class WindowViewManager {
    private Context mContext;
    private WindowManager wm;
    private ImageView mChildView;
    private WindowManager.LayoutParams params;

    public WindowViewManager(Context context) {
        this.mContext = context;
    }

    public void initOverView(final ImageView childView) {
        mChildView = childView;
        int w = 360;// 大小
        wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        params = new WindowManager.LayoutParams();
        params.type = WindowManager.LayoutParams.TYPE_BASE_APPLICATION;// 所有程序窗口的“基地”窗口，其他应用程序窗口都显示在它上面。
        params.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        params.format = PixelFormat.TRANSLUCENT;// 不设置这个弹出框的透明遮罩显示为黑色
//        params.format = PixelFormat.RGBA_8888;
        params.width = w;
        params.height = w;
//        params.gravity = Gravity.CENTER;
        int screenWidth = mContext.getResources().getDisplayMetrics().widthPixels;
        int screenHeight = mContext.getResources().getDisplayMetrics().heightPixels;
//        params.x = -100;
//        params.y =-100;
//        childView.setBackgroundColor(Color.TRANSPARENT);
//        childView.setBackgroundColor(Color.RED);
//        childView.getBackground().setAlpha(100);
        childView.setVisibility(View.VISIBLE);
        params.alpha= 0.0f;
        params.gravity=Gravity.LEFT|Gravity.TOP;
        wm.addView(childView, params);
//        childView.setVisibility(View.GONE);
//        childView.setOnTouchListener(new View.OnTouchListener() {
//            // 触屏监听
//            float lastX, lastY;
//            int oldOffsetX, oldOffsetY;
//            int tag = 0;// 悬浮球 所需成员变量
//
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                final int action = event.getAction();
//                float x = event.getX();
//                float y = event.getY();
//                if (tag == 0) {
//                    oldOffsetX = params.x; // 偏移量
//                    oldOffsetY = params.y; // 偏移量
//                }
//                if (action == MotionEvent.ACTION_DOWN) {
//                    lastX = x;
//                    lastY = y;
//                } else if (action == MotionEvent.ACTION_MOVE) {
//                    params.x += (int) (x - lastX) / 3; // 减小偏移量,防止过度抖动
//                    params.y += (int) (y - lastY) / 3; // 减小偏移量,防止过度抖动
//                    tag = 1;
//                    wm.updateViewLayout(childView, params);
//                } else if (action == MotionEvent.ACTION_UP) {
//                    int newOffsetX = params.x;
//                    int newOffsetY = params.y;
//                    // 只要按钮一动位置不是很大,就认为是点击事件
//                    if (Math.abs(oldOffsetX - newOffsetX) <= 20
//                            && Math.abs(oldOffsetY - newOffsetY) <= 20) {
//                    } else {
//                        tag = 0;
//                    }
//                }
//                return true;
//            }
//        });
    }

    public void updateOverViewLayout(int x, int y, int w, int h) {
        mChildView.setVisibility(View.VISIBLE);
        params.alpha= 1f;
        params.x = x;
        params.y = y;
        wm.updateViewLayout(mChildView, params);
    }
    /**
     * 将悬浮View从WindowManager中移除，需要与createFloatView()成对出现
     */
    public void removeFloatView() {
        if (wm != null && mChildView != null) {
            wm.removeViewImmediate(mChildView);
//          wm.removeView(view);//不要调用这个，WindowLeaked
            mChildView = null;
            wm = null;
        }
    }

    /**
     * 隐藏悬浮View
     */
    public void hideFloatView() {
        if (wm != null && mChildView != null && mChildView.isShown()) {
            mChildView.setVisibility(View.GONE);
        }
    }

    /**
     * 显示悬浮View
     */
    public void showFloatView() {
        if (wm != null && mChildView != null && !mChildView.isShown()) {
            mChildView.setVisibility(View.VISIBLE);
        }
    }
}
