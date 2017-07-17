package com.liaoinstan.dragrecyclerview.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.liaoinstan.dragrecyclerview.common.DividerGridItemDecoration;
import com.liaoinstan.dragrecyclerview.entity.Item;
import com.liaoinstan.dragrecyclerview.R;
import com.liaoinstan.dragrecyclerview.adapter.RecyclerAdapter;
import com.liaoinstan.dragrecyclerview.helper.MyItemTouchCallback;
import com.liaoinstan.dragrecyclerview.helper.OnRecyclerItemClickListener;
import com.liaoinstan.dragrecyclerview.utils.ACache;
import com.liaoinstan.dragrecyclerview.utils.VibratorUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/4/12.
 */
public class MyGridFragment extends Fragment implements MyItemTouchCallback.OnDragListener {

    private List<Item> results = new ArrayList<Item>();
    ArrayList<Item> items;
    TextView deletestate;
    TextView dragstate;
    TextView outbunds;
    TextView mposition;
    RelativeLayout overview;
    boolean isover;// 标记是否松开拖拽


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ////////////////////////////////////////////////////////
        /////////初始化数据，如果缓存中有就使用缓存中的
        items = (ArrayList<Item>) ACache.get(getActivity()).getAsObject("items");
        if (items != null)
            results.addAll(items);
        else {
//            for (int i = 0; i < 3; i++) {
//                results.add(new Item(i * 8 + 0, "收款", R.drawable.takeout_ic_category_brand));
//                results.add(new Item(i * 8 + 1, "转账", R.drawable.takeout_ic_category_flower));
//                results.add(new Item(i * 8 + 2, "余额宝", R.drawable.takeout_ic_category_fruit));
//                results.add(new Item(i * 8 + 3, "手机充值", R.drawable.takeout_ic_category_medicine));
//                results.add(new Item(i * 8 + 4, "医疗", R.drawable.takeout_ic_category_motorcycle));
//                results.add(new Item(i * 8 + 5, "彩票", R.drawable.takeout_ic_category_public));
//                results.add(new Item(i * 8 + 6, "电影", R.drawable.takeout_ic_category_store));
//                results.add(new Item(i * 8 + 7, "游戏", R.drawable.takeout_ic_category_sweet));
//            }
            results.add(new Item(0, "收款", R.drawable.takeout_ic_category_brand));
            results.add(new Item(1, "转账", R.drawable.takeout_ic_category_flower));
            results.add(new Item(2, "余额宝", R.drawable.takeout_ic_category_fruit));
            results.add(new Item(3, "手机充值", R.drawable.takeout_ic_category_medicine));
            results.add(new Item(4, "医疗", R.drawable.takeout_ic_category_motorcycle));
            results.add(new Item(5, "彩票", R.drawable.takeout_ic_category_public));
            results.add(new Item(6, "电影", R.drawable.takeout_ic_category_store));
            results.add(new Item(7, "游戏", R.drawable.takeout_ic_category_sweet));
        }
        results.remove(results.size() - 1);
        results.add(new Item(results.size(), "更多", R.drawable.takeout_ic_more));
        ////////////////////////////////////////////////////////
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        return new RecyclerView(container.getContext());
        return inflater.inflate(R.layout.grid_layout, container, false);
    }

    private RecyclerView recyclerView;
    private ItemTouchHelper itemTouchHelper;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        overview = (RelativeLayout) view.findViewById(R.id.over_view);
        mposition = (TextView) view.findViewById(R.id.position);
        dragstate = (TextView) view.findViewById(R.id.dragstate);
        deletestate = (TextView) view.findViewById(R.id.deletestate);
        outbunds = (TextView) view.findViewById(R.id.outbunds);
        RecyclerAdapter adapter = new RecyclerAdapter(R.layout.item_grid, results);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 4));
        recyclerView.addItemDecoration(new DividerGridItemDecoration(getActivity()));

        itemTouchHelper = new ItemTouchHelper(new MyItemTouchCallback(adapter, results, (ViewGroup) view).setOnDragListener(this));
        itemTouchHelper.attachToRecyclerView(recyclerView);
        /**
         * 如果你想自定义触摸view，那么就使用startDrag(ViewHolder)方法。

         原来如此，我们可以在item的长按事件中得到当前item的ViewHolder ，然后调用ItemTouchHelper.startDrag(ViewHolder vh)就可以实现拖拽了，那就这么办：
         */
        recyclerView.addOnItemTouchListener(new OnRecyclerItemClickListener(recyclerView) {
            @Override
            public void onLongClick(RecyclerView.ViewHolder vh) {
                if (vh.getLayoutPosition() != results.size() - 1) {
                    /**
                     * 非最后一个位置的视图相应拖拽事件
                     */
                    itemTouchHelper.startDrag(vh);
                    VibratorUtil.Vibrate(getActivity(), 70);   //震动70ms
                }
            }

            @Override
            public void onItemClick(RecyclerView.ViewHolder vh) {
                Item item = results.get(vh.getLayoutPosition());
                Toast.makeText(getActivity(), item.getId() + " " + item.getName(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onFinishDrag() {
        //存入缓存
//        Toast.makeText(this.getActivity(), "finish", Toast.LENGTH_LONG).show();
        Log.e("tag","完成拖拽");
        isover=true;
        ACache.get(getActivity()).put("items", (ArrayList<Item>) results);
        overview.setVisibility(View.GONE);
    }

    @Override
    public void deleteState(boolean delete, float dy, int itemh) {
        if (delete) {
            deletestate.setText("到达删除区域" + dy + "item高度" + itemh);
        } else {
            deletestate.setText("未到达删除区域" + dy + "item高度" + itemh);
        }

    }

    @Override
    public void dragState(boolean start) {
        if (start) {
            dragstate.setText("拖动状态" + start + "");
        } else {
            dragstate.setText("非拖动状态" + start + "");
        }

    }

    @Override
    public void isOutBunds(boolean isOut, int h, int position) {
        if (isOut) {
            outbunds.setText("拖拽出边界" + "距离边界=" + h);
            if(!isover){
                /**
                 * 非松开拖拽后的回调，显示遮罩视图
                 */
                addview();
                overview.setVisibility(View.VISIBLE);
                isover=false;
                Log.e("tag"," isover=false;");
            }else {
                /**
                 * 处理松开拖拽后的回调，isover置为false，下次拖拽仍然可以显示遮罩视图
                 */
                isover=false;
                Log.e("tag"," isover=true;");
            }


//            Toast.makeText(this.getActivity(), "show", Toast.LENGTH_LONG).show();

        } else {
            outbunds.setText("未拖拽出边界" + "距离边界=" + h);
            Log.e("tag","未超出边界");
            overview.setVisibility(View.GONE);
        }
        mposition.setText(position + "");
    }

    public void addview() {
        ImageView imageView = new ImageView(this.getActivity());
        imageView.setImageResource(R.drawable.item_img);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);

        overview.addView(imageView,layoutParams);

    }
}