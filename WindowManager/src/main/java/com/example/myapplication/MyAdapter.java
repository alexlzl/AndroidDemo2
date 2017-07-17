package com.example.myapplication;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.List;

/**
 * Created by liuzhouliang on 2017/7/17.
 */

public class MyAdapter extends RecyclerView.Adapter {
    private Context mcontex;
    private LayoutInflater layoutInflater;
    private List<String> data;

    public MyAdapter(Context mcontex, List<String> list) {
        this.mcontex = mcontex;
        layoutInflater=LayoutInflater.from(mcontex);
        data=list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View item=layoutInflater.inflate(R.layout.item_layout,parent,false);
        MyViewHolder myViewHolder=new MyViewHolder(item);

        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((MyViewHolder)holder).button1.setText(position+"position");
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        Button button1;

        public MyViewHolder(View itemView) {
            super(itemView);
            button1= (Button) itemView.findViewById(R.id.button1);
        }
    }
}
