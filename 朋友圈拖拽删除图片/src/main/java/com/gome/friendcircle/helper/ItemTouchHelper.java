package com.gome.friendcircle.helper;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.gome.app.R;
import com.gome.friendcircle.adapter.RecyclerAdapter;
import com.gome.friendcircle.entity.ItemEntity;

import java.util.List;

 /**
   * @ Describe: 拖拽事件控制类
   *
   * @ Author: LZL
   *
   * @ Time: 2017/7/19 18:05
   */

public class ItemTouchHelper extends android.support.v7.widget.helper.ItemTouchHelper.Callback {

    private ItemMoveCallbackAdapter itemMoveCallbackAdapter;
    private boolean isDragOver;//标记手指是否放开
    private List<ItemEntity> mDataList;
    private ViewGroup layoutRoot;
    private Drawable background = null;
    private int bgColor = -1;
    private OnDragListener onDragListener;
    private boolean isLoosenOnDelete;//当在删除区域，松开手后，数据删除，标记onChildDraw是否需要继续刷新位置(防止遮罩层view出现位置回滚)

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
     * 当长按选中item的时候（拖拽开始的时候）调用
     */
    @Override
    public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {

        if (actionState != android.support.v7.widget.helper.ItemTouchHelper.ACTION_STATE_IDLE) {
            Log.e("tag", "onSelectedChanged===ACTION_STATE_IDLE");
            if (background == null && bgColor == -1) {
                Drawable drawable = viewHolder.itemView.getBackground();
                if (drawable == null) {
                    bgColor = 0;
                } else {
                    background = drawable;
                }
            }
            viewHolder.itemView.setBackgroundColor(Color.LTGRAY);
        }
        if (android.support.v7.widget.helper.ItemTouchHelper.ACTION_STATE_DRAG == actionState && onDragListener != null) {
            Log.e("tag", "onSelectedChanged===ACTION_STATE_DRAG");
            onDragListener.isStartDrag(true);
        }
        super.onSelectedChanged(viewHolder, actionState);
    }

    /**
     * 当用户从item原来的位置拖动可以拖动的item到新位置的过程中调用
     */
    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {

        int fromPosition = viewHolder.getAdapterPosition();//得到拖动ViewHolder的position
        int toPosition = target.getAdapterPosition();//得到目标ViewHolder的position
        itemMoveCallbackAdapter.onMove(fromPosition, toPosition);//回调adapter进行数据刷新
        Log.e("tag", "onMove===="+"fromPosition=="+fromPosition+"toPosition==="+toPosition);
        Toast.makeText(recyclerView.getContext(),"fromPosition=="+fromPosition+"toPosition==="+toPosition,Toast.LENGTH_SHORT).show();
//        ((RecyclerAdapter) itemMoveCallbackAdapter).notifyDataSetChanged();
        return true;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
    }


    /**
     * RecyclerView调用onDraw时调用，如果想自定义item对用户互动的响应,可以重写该方法
     */
    @Override
    public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        Log.e("tag", "onChildDraw==");
        if (null == onDragListener) {
            return;
        }
        if (isLoosenOnDelete) {
            /**
             * 如果在删除区域并且松开手，刷新完数据，后面的onChildDraw事件就不处理
             */
            Log.e("tag", "删除数据后onChildDraw==");
            return;
        }
        /**
         *  一旦拖拽产生位移，获取ITEM坐标，传递给遮罩层显示遮罩层中的视图=========1
         */
        int[] itemViewPosition = new int[2];
        viewHolder.itemView.getLocationOnScreen(itemViewPosition);
        onDragListener.showOverView(itemViewPosition[0], itemViewPosition[1], viewHolder.itemView);
        /**
         * 判断是否拖拽到删除区域===========2
         */
        if (isMoveToDelete(viewHolder.itemView, dY)) {
            /**
             * 到达删除区域====
             */
            Log.e("tag", "onChildDraw==到达删除区域");
            onDragListener.isCanDelete(true);
            if (isDragOver) {
                /**
                 * 在删除区域松开手，删除ITEM数据，刷新UI
                 */
                if (viewHolder.getAdapterPosition() == -1)
                    return;
                viewHolder.itemView.setVisibility(View.INVISIBLE);//先设置不可见，如果不设置的话，会看到viewHolder返回到原位置时才消失，因为remove会在viewHolder动画执行完成后才将viewHolder删除
                mDataList.remove(viewHolder.getAdapterPosition());
                /**
                 * 隐藏窗口层
                 */
                onDragListener.isShowWindow(false);
                Log.e("tag", "删除数据位置==" + viewHolder.getAdapterPosition());
                Log.e("tag", "剩余数据==" + mDataList.size() + "===" + mDataList.toString());
                ((RecyclerAdapter) itemMoveCallbackAdapter).notifyItemRemoved(viewHolder.getAdapterPosition());
                isLoosenOnDelete = true;
                initData();
                return;
            }
        } else {
            /**
             * 未到达删除区域====
             */
//            Log.e("tag", "onChildDraw==未到达删除区域");
            onDragListener.isCanDelete(false);
        }


    }

    /**
     * 手指放开回调
     */
    @Override
    public long getAnimationDuration(RecyclerView recyclerView, int animationType, float animateDx, float animateDy) {
        Log.e("tag", "getAnimationDuration手指松开====isDragOver = true");
        isDragOver = true;
        return super.getAnimationDuration(recyclerView, animationType, animateDx, animateDy);
    }
    /**
     * 当用户与item的交互结束并且item也完成了动画时调用
     */
    @Override
    public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        super.clearView(recyclerView, viewHolder);
        Log.e("tag", "交互完成clearView");
        viewHolder.itemView.setAlpha(1.0f);
        if (background != null) viewHolder.itemView.setBackgroundDrawable(background);
        if (bgColor != -1) viewHolder.itemView.setBackgroundColor(bgColor);
        if (onDragListener != null) {
            onDragListener.onDragFinished(viewHolder.itemView);
        }
        isLoosenOnDelete = false;
        if(isNotify(recyclerView)){
            ((RecyclerAdapter) itemMoveCallbackAdapter).notifyDataSetChanged();
        }

//        specialUpdate();
    }
    private boolean isNotify(RecyclerView recyclerView){
        boolean isNotify=false;
        if(recyclerView.getScrollState()==RecyclerView.SCROLL_STATE_IDLE&&!recyclerView.isComputingLayout()){
            isNotify=true;
        }
        return  isNotify;
    }
     private void specialUpdate() {
         Handler handler = new Handler();
         final Runnable r = new Runnable() {
             public void run() {
                 ((RecyclerAdapter) itemMoveCallbackAdapter).notifyDataSetChanged();
             }
         };
         handler.post(r);
     }

    /**
     * @ Describe: 判断是否已经到达底部删除区域
     * @ Author: LZL
     * @ Time: 2017/7/19 16:45
     */

    public boolean isMoveToDelete(View itemView, float dy) {
        /**
         * 向上拖拽
         */
        if (dy < 0)
            return false;
        /**
         * 获取ITEM在屏幕中的坐标
         */
        int[] itemLocation = new int[2];
        itemView.getLocationOnScreen(itemLocation);
        int itemViewY = itemLocation[1];
        /**
         * 获取删除视图在屏幕中的坐标
         */
        int[] deleteView = new int[2];
        ImageView imageView = (ImageView) layoutRoot.findViewById(R.id.bottom_tv);
        imageView.getLocationOnScreen(deleteView);
        int deleteViewY = deleteView[1];
        int viewDistance = deleteViewY - itemViewY-itemView.getHeight();//ITEM到底部删除视图的距离
        Log.e("tag", "底部删除视图Y坐标==" + deleteViewY + "==ITEM=Y坐标==" + itemViewY);
        Log.e("tag", "视图拖动距离==" + dy + "距离删除区域距离==" + viewDistance);
        if (viewDistance <= 0) {
            /**
             * 在ITEM底部距离删除视图小于50像素时，达到删除条件
             */
            return true;
        }
        return false;

    }




    /**
     * 状态重置
     */
    private void initData() {
        if (onDragListener != null) {
            onDragListener.isCanDelete(false);
            onDragListener.isStartDrag(false);
        }
        isDragOver = false;
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
         * 开始显示遮罩层
         */
        void showOverView(int l, int t, View itemView);

        /**
         * 是否显示窗口层
         */
        void isShowWindow(boolean isShow);

    }

    /**
     * ITEM移动后Adapter回调数据
     */
    public interface ItemMoveCallbackAdapter {
        void onMove(int fromPosition, int toPosition);

    }
}
