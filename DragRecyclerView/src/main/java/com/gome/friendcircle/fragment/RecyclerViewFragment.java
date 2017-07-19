package com.gome.friendcircle.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.gome.app.R;
import com.gome.friendcircle.entity.ItemEntity;
import com.gome.friendcircle.adapter.RecyclerAdapter;
import com.gome.friendcircle.helper.ItemTouchHelper;
import com.gome.friendcircle.helper.RecyclerViewItemTouchListener;
import com.gome.friendcircle.helper.WindowViewManager;
import com.gome.friendcircle.utils.CommonUtil;

import java.util.ArrayList;
import java.util.List;

public class RecyclerViewFragment extends Fragment implements ItemTouchHelper.OnDragListener {

    private List<ItemEntity> dataList = new ArrayList();
    private ImageView deleteview;
    private boolean isOver;// 标记是否松开后产生的拖拽
    public WindowViewManager windowViewManager;
    private RecyclerView recyclerView;
    private android.support.v7.widget.helper.ItemTouchHelper itemTouchHelper;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();

    }

    private void initData() {
        dataList.add(new ItemEntity(0, R.drawable.one));
        dataList.add(new ItemEntity(1, R.drawable.two));
        dataList.add(new ItemEntity(2, R.drawable.three));
        dataList.add(new ItemEntity(3, R.drawable.four));
        dataList.add(new ItemEntity(4, R.drawable.five));
        dataList.add(new ItemEntity(5, R.drawable.six));
        dataList.add(new ItemEntity(6, R.drawable.seven));
        dataList.add(new ItemEntity(7, R.drawable.eight));
        dataList.add(new ItemEntity(8, R.drawable.nine));
        dataList.add(new ItemEntity(dataList.size(), R.drawable.add));
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_layout, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        deleteview = (ImageView) view.findViewById(R.id.bottom_tv);
        RecyclerAdapter adapter = new RecyclerAdapter(R.layout.item_layout, dataList);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 4));
        itemTouchHelper = new android.support.v7.widget.helper.ItemTouchHelper(new ItemTouchHelper(adapter, dataList, (ViewGroup) view).setOnDragListener(this));
        itemTouchHelper.attachToRecyclerView(recyclerView);
        initWidowView();
        setRecyclerViewItemListener();
    }

    /**
     * 初始化WINDOW层位置
     */
    public void initWidowView() {
        ImageView imageView = new ImageView(this.getActivity());
        imageView.setId(R.id.recycler_item_view);
        imageView.setBackgroundColor(getActivity().getResources().getColor(R.color.colorPrimary));
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        windowViewManager = new WindowViewManager(getActivity());
        windowViewManager.initOverView(imageView);
    }

    /**
     * 如果你想自定义触摸view，那么就使用startDrag(ViewHolder)方法。
     */
    public void setRecyclerViewItemListener() {
        recyclerView.addOnItemTouchListener(new RecyclerViewItemTouchListener(recyclerView) {
            @Override
            public void onLongClick(RecyclerView.ViewHolder vh) {
                if (vh.getLayoutPosition() != dataList.size() - 1) {
                    /**
                     * 非最后一个位置的视图相应拖拽事件
                     */
                    itemTouchHelper.startDrag(vh);
                    CommonUtil.Vibrate(getActivity(), 70);
                }
            }

            @Override
            public void onItemClick(RecyclerView.ViewHolder vh) {
                ItemEntity item = dataList.get(vh.getLayoutPosition());
                Toast.makeText(getActivity(), item.getId() + " ", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 拖拽是否松开
     */
    @Override
    public void onDragFinished(View itemView) {
        //存入缓存
        Log.e("tag", "完成拖拽");
        isOver = true;
        deleteview.setVisibility(View.GONE);
        itemView.setVisibility(View.VISIBLE);
        hideWindowView();
    }

    /**
     * 监听是否到达可删除区域
     *
     * @param isCanDelete
     */
    @Override
    public void isCanDelete(boolean isCanDelete) {
        if (isCanDelete) {
            deleteview.setImageResource(R.drawable.delete_yes);
        } else {
            deleteview.setImageResource(R.drawable.delete_no);
        }

    }

    /**
     * 是否开始拖拽
     *
     * @param isStartDrag
     */
    @Override
    public void isStartDrag(boolean isStartDrag) {
        if (isStartDrag) {
            deleteview.setVisibility(View.VISIBLE);
        } else {
            deleteview.setVisibility(View.GONE);
        }
    }

    /**
     * 拖拽是否超出RecyclerView边界
     */
    @Override
    public void isBeyondBounds(boolean isBeyond, int l, int t, int r, int b, View itemView) {
        if (isBeyond) {
            /**
             * 拖拽超出边界，显示遮罩层
             */
            Log.e("tag", "超出边界");
            showWindowView(l, t, (ImageView) itemView.findViewById(R.id.item_img));
            itemView.setVisibility(View.GONE);

            if (!isOver) {
                /**
                 * 非松开拖拽后的回调，显示遮罩视图
                 */
                isOver = false;
            } else {
                /**
                 * 处理松开拖拽后的回调，isOver置为false，下次拖拽仍然可以显示遮罩视图
                 */
                isOver = false;
                hideWindowView();
            }

        } else {
            Log.e("tag", "未超出边界");
        }
    }

    @Override
    public void isShowWindow(boolean isShow) {
                if(!isShow){
                  hideWindowView();
                }
    }


    /**
     * 根据拖拽ITEM位置更新Window层视图位置
     */
    public void showWindowView(int x, int y, ImageView imageView) {
        windowViewManager.showOverViewLayout(x, y - CommonUtil.getStatusBarHeight(getActivity()), imageView.getDrawable());
    }

    public void hideWindowView() {
        windowViewManager.hideOverViewLayout();
    }


}