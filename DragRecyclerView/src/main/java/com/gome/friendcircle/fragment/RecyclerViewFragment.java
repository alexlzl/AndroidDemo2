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
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gome.app.R;
import com.gome.friendcircle.entity.ItemEntity;
import com.gome.friendcircle.adapter.RecyclerAdapter;
import com.gome.friendcircle.helper.ItemTouchHelper;
import com.gome.friendcircle.helper.RecyclerViewItemTouchListener;
import com.gome.friendcircle.helper.WindowViewManager;
import com.gome.friendcircle.utils.ACache;
import com.gome.friendcircle.utils.VibratorUtil;

import java.util.ArrayList;
import java.util.List;

public class RecyclerViewFragment extends Fragment implements ItemTouchHelper.OnDragListener {

    private List<ItemEntity> results = new ArrayList<ItemEntity>();
    private ArrayList<ItemEntity> items;
    private TextView deletestate;
    private TextView dragstate;
    private TextView outbunds;
    private TextView mposition;
    private TextView movied;
    private TextView locationd;
    private TextView deleteview;
    private boolean isover;// 标记是否松开拖拽
    public WindowViewManager windowSet;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();

    }

    private void initData(){
        items = (ArrayList<ItemEntity>) ACache.get(getActivity()).getAsObject("items");
        if (items != null)
            results.addAll(items);
        else {
            results.add(new ItemEntity(0, R.drawable.one));
            results.add(new ItemEntity(1,  R.drawable.two));
            results.add(new ItemEntity(2,  R.drawable.three));
            results.add(new ItemEntity(3,  R.drawable.four));
            results.add(new ItemEntity(4,  R.drawable.five));
            results.add(new ItemEntity(5,  R.drawable.six));
            results.add(new ItemEntity(6,  R.drawable.seven));
            results.add(new ItemEntity(7,  R.drawable.eight));
        }
        results.remove(results.size() - 1);
        results.add(new ItemEntity(results.size(), R.drawable.add));
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        return new RecyclerView(container.getContext());
        return inflater.inflate(R.layout.grid_layout, container, false);
    }

    private RecyclerView recyclerView;
    private android.support.v7.widget.helper.ItemTouchHelper itemTouchHelper;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        deleteview = (TextView) view.findViewById(R.id.bottom_tv);
        movied = (TextView) view.findViewById(R.id.movie_d);
        locationd = (TextView) view.findViewById(R.id.location_d);
//        overview = (RelativeLayout) view.findViewById(R.id.over_view);
        mposition = (TextView) view.findViewById(R.id.position);
        dragstate = (TextView) view.findViewById(R.id.dragstate);
        deletestate = (TextView) view.findViewById(R.id.deletestate);
        outbunds = (TextView) view.findViewById(R.id.outbunds);
//        initoverview();
        initWidowView();
        RecyclerAdapter adapter = new RecyclerAdapter(R.layout.item_grid, results);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 4));
//        recyclerView.addItemDecoration(new DividerGridItemDecoration(getActivity()));

        itemTouchHelper = new android.support.v7.widget.helper.ItemTouchHelper(new ItemTouchHelper(adapter, results, (ViewGroup) view).setOnDragListener(this));
        itemTouchHelper.attachToRecyclerView(recyclerView);
        /**
         * 如果你想自定义触摸view，那么就使用startDrag(ViewHolder)方法。

         原来如此，我们可以在item的长按事件中得到当前item的ViewHolder ，然后调用ItemTouchHelper.startDrag(ViewHolder vh)就可以实现拖拽了，那就这么办：
         */
        recyclerView.addOnItemTouchListener(new RecyclerViewItemTouchListener(recyclerView) {
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
                ItemEntity item = results.get(vh.getLayoutPosition());
                Toast.makeText(getActivity(), item.getId() + " " , Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onFinishDrag() {
        //存入缓存
//        Toast.makeText(this.getActivity(), "finish", Toast.LENGTH_LONG).show();
        Log.e("tag", "完成拖拽");
        isover = true;
        ACache.get(getActivity()).put("items", (ArrayList<ItemEntity>) results);
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
    public void isOutBunds(boolean isOut, int dy, int position, int x, int y, int w, int h) {
        if (isOut) {
            /**
             * 超出边界============
             */
            outbunds.setText("拖拽出边界" + "距离边界=" + dy);
//            overview.setVisibility(View.VISIBLE);
            updateoverview(x, y, w, h);
            if (!isover) {
                /**
                 * 非松开拖拽后的回调，显示遮罩视图
                 */
//                addview(0,0,0,0);
//                overview.setVisibility(View.VISIBLE);
                isover = false;
                Log.e("tag", " isover=false;");
            } else {
                /**
                 * 处理松开拖拽后的回调，isover置为false，下次拖拽仍然可以显示遮罩视图
                 */
                isover = false;
                Log.e("tag", " isover=true;");
            }


//            Toast.makeText(this.getActivity(), "show", Toast.LENGTH_LONG).show();

        } else {
            outbunds.setText("未拖拽出边界" + "距离边界=" + dy);
            Log.e("tag", "未超出边界");
//            overview.setVisibility(View.GONE);
        }
        mposition.setText(position + "");
    }

    @Override
    public void movie(float dx, float dy) {
        movied.setText("X移动==" + dx + "Y移动==" + dy);

    }

    @Override
    public void itemLocation(int x, int y, int w, int h) {
        locationd.setText("item x===" + x + "item y===" + y);
//        addview(x,y,w,h);
    }

    public void updateoverview(int x, int y, int w, int h) {
//        overview.findViewById(R.id.my_view).layout(x,y-h+80,x+w,y+80);
//        overview.findViewById(R.id.my_view).layout(200,500,300,600);
        windowSet.updateOverViewLayout(x, y - getStatusBarHeight(), x + w, y + 80);
    }

    /**
     * 初始化遮罩层视图位置
     */
    public void initoverview() {
        ImageView imageView = new ImageView(this.getActivity());
        imageView.setId(R.id.my_view);
        imageView.setImageResource(R.drawable.j);
        imageView.setBackgroundColor(getActivity().getResources().getColor(R.color.colorPrimary));
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(360, 360);
//        overview.addView(imageView,layoutParams);
    }

    public void initWidowView() {
        ImageView imageView = new ImageView(this.getActivity());
        imageView.setId(R.id.my_view);
        imageView.setImageResource(R.drawable.j);
        imageView.setBackgroundColor(getActivity().getResources().getColor(R.color.colorPrimary));
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        windowSet = new WindowViewManager(getActivity());
        imageView.setVisibility(View.GONE);
        windowSet.initOverView(imageView);
    }

    public int getStatusBarHeight() {
        /**
         * 获取状态栏高度——方法1
         * */
        int statusBarHeight1 = -1;
//获取status_bar_height资源的ID
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            //根据资源ID获取响应的尺寸值
            statusBarHeight1 = getResources().getDimensionPixelSize(resourceId);
        }
        return statusBarHeight1;
    }

}