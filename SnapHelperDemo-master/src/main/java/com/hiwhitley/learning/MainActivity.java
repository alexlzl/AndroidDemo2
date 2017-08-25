package com.hiwhitley.learning;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private List<Integer> mImagesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        mImagesList = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            mImagesList.add(R.drawable.ic_item1);
            mImagesList.add(R.drawable.ic_item2);
            mImagesList.add(R.drawable.ic_item3);
        }
        mRecyclerView = (RecyclerView) findViewById(R.id.rv_main);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        mRecyclerView.setAdapter(new MyAdapter(mImagesList));

        LinearSnapHelper mLinearSnapHelper = new LinearSnapHelper();
        mLinearSnapHelper.attachToRecyclerView(mRecyclerView);

        mRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(mRecyclerView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Toast.makeText(view.getContext(), "position" + position, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        }));
        /**
         * LinearSnapHelper & PagerSnapHelper
         上面讲了 SnapHelper 的几个重要的方法和作用，SnapHelper 是一个抽象类，要使用SnapHelper，需要实现它的几个方法。而 Google 内置了两个默认实现类，LinearSnapHelper和PagerSnapHelper ，LinearSnapHelper 可以使 RecyclerView 的当前 Item 居中显示（横向和竖向都支持），PagerSnapHelper  看名字可能就能猜到，使RecyclerView 像ViewPager 一样的效果，每次只能滑动一页（LinearSnapHelper 支持快速滑动）, PagerSnapHelper 也是 Item 居中对齐。接下来看一下使用方法和效果。
         */
//        MySnapHelper mMySnapHelper = new MySnapHelper();
//        mMySnapHelper.attachToRecyclerView(mRecyclerView);
//        PagerSnapHelper pagerSnapHelper=new PagerSnapHelper();
//        pagerSnapHelper.attachToRecyclerView(mRecyclerView);
    }
}
