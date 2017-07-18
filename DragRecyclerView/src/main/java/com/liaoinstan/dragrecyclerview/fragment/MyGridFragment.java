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
    TextView movied;
    TextView locationd;
    TextView  deleteview;
    boolean isover;// 标记是否松开拖拽


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /////////初始化数据，如果缓存中有就使用缓存中的
        items = (ArrayList<Item>) ACache.get(getActivity()).getAsObject("items");
        if (items != null)
            results.addAll(items);
        else {
            results.add(new Item(0, "收款", R.drawable.j));
            results.add(new Item(1, "转账", R.drawable.j));
            results.add(new Item(2, "余额宝", R.drawable.j));
            results.add(new Item(3, "手机充值", R.drawable.j));
            results.add(new Item(4, "医疗", R.drawable.j));
            results.add(new Item(5, "彩票", R.drawable.j));
            results.add(new Item(6, "电影", R.drawable.j));
            results.add(new Item(7, "游戏", R.drawable.j));
        }
        results.remove(results.size() - 1);
        results.add(new Item(results.size(), "更多", R.drawable.takeout_ic_more));
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
        deleteview= (TextView) view.findViewById(R.id.bottom_tv);
        movied= (TextView) view.findViewById(R.id.movie_d);
        locationd= (TextView) view.findViewById(R.id.location_d);
        overview = (RelativeLayout) view.findViewById(R.id.over_view);
        mposition = (TextView) view.findViewById(R.id.position);
        dragstate = (TextView) view.findViewById(R.id.dragstate);
        deletestate = (TextView) view.findViewById(R.id.deletestate);
        outbunds = (TextView) view.findViewById(R.id.outbunds);
        initoverview();
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
//        overview.setVisibility(View.GONE);
    }

    @Override
    public void deleteState(boolean delete, float dy, int itemh) {
        if (delete) {
            deletestate.setText("到达删除区域" + dy + "item高度" + itemh);
            deleteview.setBackgroundDrawable(getActivity().getResources().getDrawable(R.drawable.delete_yes));
        } else {
            deletestate.setText("未到达删除区域" + dy + "item高度" + itemh);
            deleteview.setBackgroundDrawable(getActivity().getResources().getDrawable(R.drawable.delete_no));
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
    public void isOutBunds(boolean isOut, int dy, int position,int x,int y,int w,int h) {
        if (isOut) {
            /**
             * 超出边界============
             */
            outbunds.setText("拖拽出边界" + "距离边界=" + dy);
            overview.setVisibility(View.VISIBLE);
            updateoverview(x,y,w,h);
            if(!isover){
                /**
                 * 非松开拖拽后的回调，显示遮罩视图
                 */
//                addview(0,0,0,0);
//                overview.setVisibility(View.VISIBLE);
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
            outbunds.setText("未拖拽出边界" + "距离边界=" + dy);
            Log.e("tag","未超出边界");
            overview.setVisibility(View.GONE);
        }
        mposition.setText(position + "");
    }

    @Override
    public void movie(float dx, float dy) {
        movied.setText("X移动=="+dx+"Y移动=="+dy);

    }

    @Override
    public void itemLocation(int x, int y,int w,int h) {
                locationd.setText("item x==="+x+"item y==="+y);
//        addview(x,y,w,h);
    }

    public void updateoverview(int x,int y,int w,int h) {
        overview.findViewById(R.id.my_view).layout(x,y-h+80,x+w,y+80);
//        overview.findViewById(R.id.my_view).layout(200,500,300,600);
    }

    public void initoverview(){
        ImageView imageView = new ImageView(this.getActivity());
        imageView.setId(R.id.my_view);
        imageView.setImageResource(R.drawable.j);
        imageView.setBackgroundColor(getActivity().getResources().getColor(R.color.colorPrimary));
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(360, 360);
        overview.addView(imageView,layoutParams);
    }


}