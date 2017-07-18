package com.liaoinstan.dragrecyclerview.helper;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.liaoinstan.dragrecyclerview.R;
import com.liaoinstan.dragrecyclerview.adapter.RecyclerAdapter;
import com.liaoinstan.dragrecyclerview.entity.Item;

import java.util.List;

/**
 * //设置item是否处理拖拽事件和滑动事件，以及拖拽和滑动操作的方向
 * public int getMovementFlags (RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder)
 * <p>
 * <p>
 * 当用户从item原来的位置拖动可以拖动的item到新位置的过程中调用
 *
 * @recyclerView
 * @viewHolder 拖动的 item
 * @target 目标 item
 * <p>
 * public boolean onMove (RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target)
 * <p>
 * RecyclerView调用onDraw时调用，如果想自定义item对用户互动的响应,可以重写该方法
 * @dx item 滑动的距离
 * <p>
 * public void onChildDraw (Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive)
 */
public class MyItemTouchCallback extends ItemTouchHelper.Callback {

    private ItemTouchAdapter itemTouchAdapter;
    private boolean up;
    List<Item> mresults;
    ViewGroup mparent;

    public MyItemTouchCallback(ItemTouchAdapter itemTouchAdapter, List<Item> results, ViewGroup parent) {
        this.itemTouchAdapter = itemTouchAdapter;
        mresults = results;
        mparent = parent;
    }

    /**
     * 上面的代码完成了基本功能，但实际的产品需要往往可能会有些不一样，比如说，产品希望，有一些item可以拖拽，一些item无法拖拽，就如上图的“更多”是无法拖拽的。这个咋办呢？
     *
     * @return
     */
    @Override
    public boolean isLongPressDragEnabled() {
        return false;
    }

    @Override
    public boolean isItemViewSwipeEnabled() {
        return true;
    }

    /**
     * getMovementFlags用于设置是否处理拖拽事件和滑动事件，以及拖拽和滑动操作的方向，比如如果是列表类型的RecyclerView，拖拽只有UP、DOWN两个方向，而如果是网格类型的则有UP、DOWN、LEFT、RIGHT四个方向：
     *
     * @param recyclerView
     * @param viewHolder
     * @return
     */
    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        Log.e("tag","getMovementFlags");
        if (recyclerView.getLayoutManager() instanceof GridLayoutManager) {
            final int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
            final int swipeFlags = 0;
            return makeMovementFlags(dragFlags, swipeFlags);
        } else {
            final int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
            //final int swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END;
            final int swipeFlags = 0;
            return makeMovementFlags(dragFlags, swipeFlags);
        }
    }

    /**
     * 当用户从item原来的位置拖动可以拖动的item到新位置的过程中调用
     * @param recyclerView
     * @param viewHolder
     * @param target
     * @return
     */
    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        int fromPosition = viewHolder.getAdapterPosition();//得到拖动ViewHolder的position
        int toPosition = target.getAdapterPosition();//得到目标ViewHolder的position
        itemTouchAdapter.onMove(fromPosition, toPosition);
        Log.e("tag","onMove");
        return true;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        int position = viewHolder.getAdapterPosition();
        itemTouchAdapter.onSwiped(position);
        Log.e("tag","onSwiped");
    }

    /**
     * RecyclerView调用onDraw时调用，如果想自定义item对用户互动的响应,可以重写该方法
     */
    @Override
    public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        Log.e("tag","onChildDraw");
        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
            //滑动时改变Item的透明度
            final float alpha = 1 - Math.abs(dX) / (float) viewHolder.itemView.getWidth();
            viewHolder.itemView.setAlpha(alpha);
            viewHolder.itemView.setTranslationX(dX);
        } else {

            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            onDragListener.dragState(true);//显示删除区域
            /**
             * 获取当前item移动位置
             */
            onDragListener.movie(dX,dY);
            /**
             * 获取ITEM坐标
             */
            int[] itemposition=new int[2];
            viewHolder.itemView.getLocationOnScreen(itemposition);
//            onDragListener.itemLocation(itemposition[0],itemposition[1],viewHolder.itemView.getWidth(),viewHolder.itemView.getHeight());
            onDragListener.itemLocation(itemposition[0],itemposition[1],viewHolder.itemView.getWidth(),viewHolder.itemView.getHeight());
            /**
             * 判断是否item拖拽出下边界======
             */
            int recyclerh = mparent.findViewById(R.id.recycler).getHeight();
            //itemview高度
            int itemh = viewHolder.itemView.getHeight();
            if (isOutBund(dY, viewHolder.itemView)) {
                onDragListener.isOutBunds(true, recyclerh - itemh, viewHolder.getLayoutPosition(),itemposition[0],itemposition[1],viewHolder.itemView.getWidth(),viewHolder.itemView.getHeight());
            } else {
                onDragListener.isOutBunds(false, recyclerh - itemh, viewHolder.getLayoutPosition(),itemposition[0],itemposition[1],viewHolder.itemView.getWidth(),viewHolder.itemView.getHeight());
            }
            if (isToBottom(viewHolder.itemView, dY)) {//拖到删除处
                onDragListener.deleteState(true, dY, viewHolder.itemView.getHeight());
                if (up) {//在删除处放手，则删除item
                    viewHolder.itemView.setVisibility(View.INVISIBLE);//先设置不可见，如果不设置的话，会看到viewHolder返回到原位置时才消失，因为remove会在viewHolder动画执行完成后才将viewHolder删除
                    mresults.remove(viewHolder.getAdapterPosition());
                    ((RecyclerAdapter) itemTouchAdapter).notifyItemRemoved(viewHolder.getAdapterPosition());
                    initData();
                    return;
                }
            } else {//没有到删除处
                if (View.INVISIBLE == viewHolder.itemView.getVisibility()) {//如果viewHolder不可见，则表示用户放手，重置删除区域状态
                    onDragListener.dragState(false);
                }
                onDragListener.deleteState(false, dY, viewHolder.itemView.getHeight());
            }
        }
        if (null == onDragListener) {
            return;
        }

    }

    /**
     * \判断是否拖拽到底部删除区域
     *
     * @param itemView
     * @param dy
     * @return
     */
    public boolean isToBottom(View itemView, float dy) {
        TextView bottomtv = (TextView) mparent.findViewById(R.id.bottom_tv);
        //底部到父视图距离
        int height = bottomtv.getTop();
//        int height=mparent.findViewById(R.id.ll_parent).getTop();
        //item到recyclerview顶部距离
        int itemTop = itemView.getBottom();
        int distance = height - itemTop - 30;
        if (dy >= distance) {
            return true;
        }
        return false;
    }

    public boolean isOutBund(float dy, View itemview) {
        //recyclerview高度
        int recyclerh = mparent.findViewById(R.id.recycler).getHeight();
        //itemview高度
        int itemh = itemview.getBottom();
        int dis=recyclerh - itemh -5;
//        int d= (int) dy;
        if (dy>=dis) {
            return true;
        }
        return false;

    }

    /**
     * 当长按选中item的时候（拖拽开始的时候）调用
     * @param viewHolder
     * @param actionState
     */
    @Override
    public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
        Log.e("tag","onSelectedChanged");
        if (actionState != ItemTouchHelper.ACTION_STATE_IDLE) {
            if (background == null && bkcolor == -1) {
                Drawable drawable = viewHolder.itemView.getBackground();
                if (drawable == null) {
                    bkcolor = 0;
                } else {
                    background = drawable;
                }
            }
            viewHolder.itemView.setBackgroundColor(Color.LTGRAY);
        }
        if (ItemTouchHelper.ACTION_STATE_DRAG == actionState && onDragListener != null) {
            onDragListener.dragState(true);
        }
        super.onSelectedChanged(viewHolder, actionState);
    }

    /**
     * 当用户与item的交互结束并且item也完成了动画时调用
     * @param recyclerView
     * @param viewHolder
     */
    @Override
    public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        super.clearView(recyclerView, viewHolder);
        Log.e("tag","clearView");
        viewHolder.itemView.setAlpha(1.0f);
        if (background != null) viewHolder.itemView.setBackgroundDrawable(background);
        if (bkcolor != -1) viewHolder.itemView.setBackgroundColor(bkcolor);
        //viewHolder.itemView.setBackgroundColor(0);

        if (onDragListener != null) {
            onDragListener.onFinishDrag();
        }
    }

    /**
     * 重置
     */
    private void initData() {
        if (onDragListener != null) {
            onDragListener.deleteState(false, 0, 0);
            onDragListener.dragState(false);
        }
        up = false;
    }

    private Drawable background = null;
    private int bkcolor = -1;

    private OnDragListener onDragListener;

    public MyItemTouchCallback setOnDragListener(OnDragListener onDragListener) {
        this.onDragListener = onDragListener;
        return this;
    }

    public interface OnDragListener {
        void onFinishDrag();

        /**
         * 用户是否将 item拖动到删除处，根据状态改变颜色
         *
         * @param delete
         */
        void deleteState(boolean delete, float dy, int itemh);

        /**
         * 是否于拖拽状态
         *
         * @param start
         */
        void dragState(boolean start);

        void isOutBunds(boolean isOut, int height, int position,int x,int y,int w,int h);

        void  movie(float dx,float dy);

        void itemLocation(int x,int y,int w,int h);
    }

    public interface ItemTouchAdapter {
        void onMove(int fromPosition, int toPosition);

        void onSwiped(int position);
    }
}
