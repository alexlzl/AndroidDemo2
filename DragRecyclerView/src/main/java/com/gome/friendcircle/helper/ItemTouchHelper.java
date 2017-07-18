package com.gome.friendcircle.helper;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gome.app.R;
import com.gome.friendcircle.adapter.RecyclerAdapter;
import com.gome.friendcircle.entity.ItemEntity;

import java.util.List;


public class ItemTouchHelper extends android.support.v7.widget.helper.ItemTouchHelper.Callback {

    private ItemMoveCallbackAdapter itemMoveCallbackAdapter;
    private boolean up=true;
    private List<ItemEntity> mDataList;
    private ViewGroup layoutRoot;
    private Drawable background = null;
    private int bkcolor = -1;
    private OnDragListener onDragListener;

    public ItemTouchHelper(ItemMoveCallbackAdapter itemTouchAdapter, List<ItemEntity> dataList, ViewGroup parent) {
        this.itemMoveCallbackAdapter = itemTouchAdapter;
        mDataList = dataList;
        layoutRoot = parent;
    }

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
     */
    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        Log.e("tag", "getMovementFlags");
        if (recyclerView.getLayoutManager() instanceof GridLayoutManager) {
            final int dragFlags = android.support.v7.widget.helper.ItemTouchHelper.UP | android.support.v7.widget.helper.ItemTouchHelper.DOWN | android.support.v7.widget.helper.ItemTouchHelper.LEFT | android.support.v7.widget.helper.ItemTouchHelper.RIGHT;
            final int swipeFlags = 0;
            return makeMovementFlags(dragFlags, swipeFlags);
        } else {
            final int dragFlags = android.support.v7.widget.helper.ItemTouchHelper.UP | android.support.v7.widget.helper.ItemTouchHelper.DOWN;
            final int swipeFlags = 0;
            return makeMovementFlags(dragFlags, swipeFlags);
        }
    }

    /**
     * 当用户从item原来的位置拖动可以拖动的item到新位置的过程中调用
     */
    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        int fromPosition = viewHolder.getAdapterPosition();//得到拖动ViewHolder的position
        int toPosition = target.getAdapterPosition();//得到目标ViewHolder的position
        itemMoveCallbackAdapter.onMove(fromPosition, toPosition);//回调adapter进行数据刷新
        Log.e("tag", "onMove");
        return true;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        Log.e("tag", "onSwiped");
    }

    /**
     * RecyclerView调用onDraw时调用，如果想自定义item对用户互动的响应,可以重写该方法
     */
    @Override
    public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        Log.e("tag", "onChildDraw");
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        if (null == onDragListener) {
            return;
        }
        //开始拖拽显示删除视图
//        onDragListener.isStartDrag(true);

        /**
         * 获取ITEM坐标
         */
        int[] itemViewPosition = new int[2];
        viewHolder.itemView.getLocationOnScreen(itemViewPosition);
        /**
         * 判断是否item拖拽出下边界======1
         */
        if (isBeyondRecyclerView(dY, viewHolder.itemView)) {
            onDragListener.isBeyondBounds(true, itemViewPosition[0], itemViewPosition[1], viewHolder.itemView.getWidth(), viewHolder.itemView.getHeight(),viewHolder.itemView);
        } else {
            onDragListener.isBeyondBounds(false, itemViewPosition[0], itemViewPosition[1], viewHolder.itemView.getWidth(), viewHolder.itemView.getHeight(),viewHolder.itemView);
        }
        /**
         * 判断是否拖拽到删除区域=======2
         */
        if (isMoveToBottom(viewHolder.itemView, dY)) {
            /**
             * 到达删除区域====
             */
            onDragListener.isCanDelete(true);
            if (up) {//在删除处放手，则删除item
                viewHolder.itemView.setVisibility(View.INVISIBLE);//先设置不可见，如果不设置的话，会看到viewHolder返回到原位置时才消失，因为remove会在viewHolder动画执行完成后才将viewHolder删除
                mDataList.remove(viewHolder.getAdapterPosition());
                ((RecyclerAdapter) itemMoveCallbackAdapter).notifyItemRemoved(viewHolder.getAdapterPosition());
                initData();
                return;
            }
        } else {
            /**
             * 未到达删除区域====
             */
            if (View.INVISIBLE == viewHolder.itemView.getVisibility()) {//如果viewHolder不可见，则表示用户放手，重置删除区域状态
                onDragListener.isStartDrag(false);
            }
            onDragListener.isCanDelete(false);
        }


    }

    /**
     * 当长按选中item的时候（拖拽开始的时候）调用
     */
    @Override
    public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
        Log.e("tag", "onSelectedChanged");
        if (actionState != android.support.v7.widget.helper.ItemTouchHelper.ACTION_STATE_IDLE) {
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
        if (android.support.v7.widget.helper.ItemTouchHelper.ACTION_STATE_DRAG == actionState && onDragListener != null) {
            onDragListener.isStartDrag(true);
        }
        super.onSelectedChanged(viewHolder, actionState);
    }

    /**
     * 判断是否拖拽到底部删除区域
     */
    public boolean isMoveToBottom(View itemView, float dy) {
        TextView bottomView = (TextView) layoutRoot.findViewById(R.id.bottom_tv);
        //底部到父视图距离
        int height = bottomView.getTop();
        //item底部到recyclerView顶部距离
        int itemTop = itemView.getBottom();
        int distance = height - itemTop - 30;
        if (dy >= distance) {
            return true;
        }
        return false;
    }

    /**
     * 判断ITEM是否移动超出RecyclerView界限
     */
    public boolean isBeyondRecyclerView(float dy, View itemView) {
        //recyclerView高度
        int recyclerViewHeight = layoutRoot.findViewById(R.id.recycler).getHeight();
        //itemView底部到父视图顶部高度
        int itemBottomToTop = itemView.getBottom();
        int dis = recyclerViewHeight - itemBottomToTop - 5;
        if (dy >= dis) {
            return true;
        }
        return false;

    }


    /**
     * 当用户与item的交互结束并且item也完成了动画时调用
     */
    @Override
    public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        super.clearView(recyclerView, viewHolder);
        Log.e("tag", "clearView");
        viewHolder.itemView.setAlpha(1.0f);
        if (background != null) viewHolder.itemView.setBackgroundDrawable(background);
        if (bkcolor != -1) viewHolder.itemView.setBackgroundColor(bkcolor);
        //viewHolder.itemView.setBackgroundColor(0);
        if (onDragListener != null) {
            onDragListener.onDragFinished(viewHolder.itemView);
        }
    }

    /**
     * 重置
     */
    private void initData() {
        if (onDragListener != null) {
            onDragListener.isCanDelete(false);
            onDragListener.isStartDrag(false);
        }
        up = false;
    }


    /**
     * 设置拖拽监听回调
     *
     * @param onDragListener
     * @return
     */
    public ItemTouchHelper setOnDragListener(OnDragListener onDragListener) {
        this.onDragListener = onDragListener;
        return this;
    }

    /**
     * ITEM拖拽时监听回调接口
     */
    public interface OnDragListener {
        /**
         * 拖拽结束
         */
        void onDragFinished(View itemView);

        /**
         * 用户是否将 item拖动到删除处，根据状态改变颜色
         */
        void isCanDelete(boolean isCanDelete);

        /**
         * 是否开始拖拽
         */
        void isStartDrag(boolean start);

        /**
         * 是否拖拽超出RecyclerView边界
         */
        void isBeyondBounds(boolean isBeyond, int l, int t, int r, int b,View itemView);

    }

    /**
     * ITEM移动后Adapter回调数据
     */
    public interface ItemMoveCallbackAdapter {
        void onMove(int fromPosition, int toPosition);

    }
}