package com.gome.friendcircle.helper;

import android.content.Context;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.util.Log;
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
//        Log.e("tag", "初始化窗口");
        mChildView = childView;
        int w = 360;// 大小
        wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        params = new WindowManager.LayoutParams();
//        params.type = WindowManager.LayoutParams.TYPE_BASE_APPLICATION;// 所有程序窗口的“基地”窗口，其他应用程序窗口都显示在它上面。
        params.type = WindowManager.LayoutParams.TYPE_APPLICATION  ;
        params.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        params.format = PixelFormat.TRANSLUCENT;// 不设置这个弹出框的透明遮罩显示为黑色
        params.width = WindowManager.LayoutParams.WRAP_CONTENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        params.alpha = 0.0f;
        params.gravity = Gravity.LEFT | Gravity.TOP;
        wm.addView(childView, params);
    }

    public void showOverViewLayout(int x, int y, Drawable drawable,View itemView) {
//        Log.e("tag", "显示窗口");
        mChildView.setBackgroundDrawable(drawable);
        mChildView.setVisibility(View.VISIBLE);
        params.alpha = 1f;
        params.x = x;
        params.y = y;
        params.width=itemView.getWidth();
        params.height=itemView.getHeight();
        wm.updateViewLayout(mChildView, params);
    }
    /**
     * 隐藏悬浮View
     */
    public void hideOverViewLayout() {
//        Log.e("tag", "隐藏窗口");
        if (wm != null && mChildView != null && mChildView.isShown()) {
            params.gravity = Gravity.LEFT | Gravity.TOP;
            mChildView.setVisibility(View.GONE);
            params.alpha = 0f;
            wm.updateViewLayout(mChildView, params);
        }
    }
    /**
     * 将悬浮View从WindowManager中移除，需要与createFloatView()成对出现
     */
    public void removeWindowView() {
        if (wm != null && mChildView != null) {
            wm.removeViewImmediate(mChildView);
            mChildView = null;
            wm = null;
        }
    }




}
