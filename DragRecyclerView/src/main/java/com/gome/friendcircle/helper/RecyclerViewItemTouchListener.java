package com.gome.friendcircle.helper;

import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;


public class RecyclerViewItemTouchListener implements RecyclerView.OnItemTouchListener {
    //将OnItemTouchListener的TOUCH事件传递给GestureDetectorCompat处理
    private GestureDetectorCompat mGestureDetector;
    private RecyclerView recyclerView;

    public RecyclerViewItemTouchListener(RecyclerView recyclerView) {
        this.recyclerView = recyclerView;
        mGestureDetector = new GestureDetectorCompat(recyclerView.getContext(), new ItemTouchHelperGestureListener());
    }

    @Override
    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
        mGestureDetector.onTouchEvent(e);
        return false;
    }

    @Override
    public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        mGestureDetector.onTouchEvent(e);
    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

    }

    private class ItemTouchHelperGestureListener extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            /**
             * 已经获取到了RecyclerView的点击事件和触摸事件数据MotionEvent ，那么我们怎么知道点击的是哪一个item呢？RecyclerView已经为我们提供了这样的方法：findChildViewUnder()，我们可以通过这个方法获得点击的item，同时我们调用RecyclerView的另一个方法getChildViewHolder()，可以获得该item的ViewHolder，最后再回调我们定义的虚方法onItemClick()就ok了，这样我们就可以在外部实现该方法来获得item的点击事件了：
             */
            View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
            if (child != null) {
                RecyclerView.ViewHolder vh = recyclerView.getChildViewHolder(child);
                onItemClick(vh);
            }
            return true;
        }

        @Override
        public boolean onDoubleTap(MotionEvent e) {
            return super.onDoubleTap(e);
        }

        @Override
        public void onLongPress(MotionEvent e) {
            View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
            if (child != null) {
                RecyclerView.ViewHolder vh = recyclerView.getChildViewHolder(child);
                onLongClick(vh);
            }
        }
    }

    /**
     * item长按事件
     * @param vh
     */
    public void onLongClick(RecyclerView.ViewHolder vh) {
    }

    /**
     * item点击事件
     * @param vh
     */
    public void onItemClick(RecyclerView.ViewHolder vh) {
    }
}
