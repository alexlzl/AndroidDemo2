package com.handsome.app3;

import android.app.Activity;
import android.content.Context;
import android.media.Image;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * =====作者=====
 * 许英俊
 * =====时间=====
 * 2016/9/7.
 */
public class RecycleViewAdapter extends RecyclerView.Adapter<RecycleViewAdapter.MyViewHolder> {

    private List<Message> list;

    public RecycleViewAdapter(List<Message> list) {
        this.list = list;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_username, tv_time, tv_message;
        private ImageView iv_icon;

        public MyViewHolder(View itemView) {
            super(itemView);
            tv_username = (TextView) itemView.findViewById(R.id.tv_username);
            tv_time = (TextView) itemView.findViewById(R.id.tv_time);
            tv_message = (TextView) itemView.findViewById(R.id.tv_message);
            iv_icon = (ImageView) itemView.findViewById(R.id.iv_icon);
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycle_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Message msg = list.get(position);
        holder.tv_username.setText(msg.getUsername());
        holder.tv_time.setText(msg.getTime());
        holder.tv_message.setText(msg.getMessage());
        holder.iv_icon.setBackgroundResource(msg.getImg_id());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
