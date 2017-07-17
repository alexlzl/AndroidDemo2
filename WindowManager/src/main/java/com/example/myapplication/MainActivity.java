package com.example.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    LinearLayout linearLayout;
    Button floatview;
    FloatView floatView;
    RecyclerView recyclerView;
    MyAdapter myAdapter;
    ItemTouchHelper itemTouchHelper;
    List<String> listdata=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initRcv();
//        myAdapter=new MyAdapter(this,listdata);

//        recyclerView.setLayoutManager(new GridLayoutManager(this,3));
//        recyclerView.setAdapter(myAdapter);
        linearLayout = (LinearLayout) findViewById(R.id.root);
        floatview = (Button) findViewById(R.id.floatview);
        floatView = new FloatView(this);
        Button button=new Button(this);
        button.setText("测试");
        View view1= LayoutInflater.from(this).inflate(R.layout.float_layout,null);
        ((ViewGroup)floatview.getParent()).removeView(floatview);
        View itemview=recyclerView.getChildAt(0);
//        ((ViewGroup)itemview.getParent()).removeView(itemview);
        floatView.createFloatView(100,floatview);
    }
    private void initRcv() {
        recyclerView= (RecyclerView) findViewById(R.id.rv);
        for(int i=0;i<10;i++){
            listdata.add(i+"");
        }
        myAdapter = new MyAdapter(this,listdata);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL));
        recyclerView.setAdapter(myAdapter);
        MyCallBack myCallBack = new MyCallBack(myAdapter, listdata);
        itemTouchHelper = new ItemTouchHelper(myCallBack);
        itemTouchHelper.attachToRecyclerView(recyclerView);//绑定RecyclerView

        //事件监听
//        recyclerView.addOnItemTouchListener(new OnRecyclerItemClickListener(recyclerView) {
//
//            @Override
//            public void onItemClick(RecyclerView.ViewHolder vh) {
//
//            }
//
//            @Override
//            public void onItemLongClick(RecyclerView.ViewHolder vh) {
//                //如果item不是最后一个，则执行拖拽
//
//            }
//        });

//        myCallBack.setDragListener(new MyCallBack.DragListener() {
//            @Override
//            public void deleteState(boolean delete) {
//
//            }
//
//            @Override
//            public void dragState(boolean start) {
//
//            }
//        });
    }
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
//        if(hasFocus){
//            View itemview=recyclerView.getChildAt(0);
//            ((ViewGroup)itemview.getParent()).removeView(itemview);
//            floatView.createFloatView(100,itemview);
//        }
    }

    public void testclick(View view) {
//        view.setVisibility(View.GONE);
        Toast.makeText(this,"test",Toast.LENGTH_LONG).show();
//        Button button=new Button(this);
//        button.setText("测试");
//        View view1= LayoutInflater.from(this).inflate(R.layout.float_layout,null);

//        floatView.createFloatView(100);
    }
}
