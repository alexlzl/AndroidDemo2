package com.hiwhitley.learning;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * attachToRecyclerView: 将 SnapHelper attach 到指定的 RecyclerView 上。

 calculateDistanceToFinalSnap: 复写这个方法计算对齐到 TargetView 或容器指定点的距离，这是一个抽象方法，由子类自己实现，返回的是一个长度为 2 的 int 数组 out，out[0] 是 x 方向对齐要移动的距离，out[1] 是 y 方向对齐要移动的距离。

 calculateScrollDistance: 根据每个方向给定的速度估算滑动的距离，用于 Fling 操作。

 findSnapView:提供一个指定的目标 View 来对齐,抽象方法，需要子类实现

 findTargetSnapPosition:提供一个用于对齐的 Adapter 目标 position,抽象方法，需要子类自己实现。

 onFling:根据给定的 x 和 y 轴上的速度处理 Fling。
 */
public class MySnapHelper extends LinearSnapHelper {

    private OrientationHelper mHorizontalHelper;

    @Nullable
    @Override
    public int[] calculateDistanceToFinalSnap(RecyclerView.LayoutManager layoutManager, View targetView) {
        int[] out = new int[2];
        if (layoutManager.canScrollHorizontally()) {
            out[0] = distanceToStart(targetView, getHorizontalHelper(layoutManager));
        } else {
            out[0] = 0;
        }

        return out;
    }

    private int distanceToStart(View targetView, OrientationHelper helper) {
        return helper.getDecoratedStart(targetView) - helper.getStartAfterPadding();
    }

    @Nullable
    @Override
    public View findSnapView(RecyclerView.LayoutManager layoutManager) {
        return findStartView(layoutManager, getHorizontalHelper(layoutManager));
    }

    private View findStartView(RecyclerView.LayoutManager layoutManager,
                               OrientationHelper helper) {

        if (layoutManager instanceof LinearLayoutManager) {
            int firstChild = ((LinearLayoutManager) layoutManager).findFirstVisibleItemPosition();
            int lastChild = ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition();
            if (firstChild == RecyclerView.NO_POSITION) {
                return null;
            }
            if (lastChild == layoutManager.getItemCount() - 1) {
                return layoutManager.findViewByPosition(lastChild);
            }

            View child = layoutManager.findViewByPosition(firstChild);

            if (helper.getDecoratedEnd(child) >= helper.getDecoratedMeasurement(child) / 2
                    && helper.getDecoratedEnd(child) > 0) {
                return child;
            } else {
                return layoutManager.findViewByPosition(firstChild + 1);
            }
        }

        return super.findSnapView(layoutManager);
    }


    private OrientationHelper getHorizontalHelper(
            @NonNull RecyclerView.LayoutManager layoutManager) {
        if (mHorizontalHelper == null) {
            mHorizontalHelper = OrientationHelper.createHorizontalHelper(layoutManager);
        }
        return mHorizontalHelper;
    }
}
