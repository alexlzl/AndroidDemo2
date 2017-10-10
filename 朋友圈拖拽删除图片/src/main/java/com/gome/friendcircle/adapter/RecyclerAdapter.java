package com.gome.friendcircle.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.gome.app.R;
import com.gome.friendcircle.entity.ItemEntity;
import com.gome.friendcircle.helper.ItemTouchHelper;

import java.util.Collections;
import java.util.List;


public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MyViewHolder> implements ItemTouchHelper.ItemMoveCallbackAdapter {

    private Context context;
    private int layoutRes;
    private List<ItemEntity> results;

    public RecyclerAdapter(int src,List<ItemEntity> results){
        this.results = results;
        this.layoutRes = src;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        this.context = parent.getContext();
        View itemView = LayoutInflater.from(parent.getContext()).inflate(layoutRes, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
//        holder.imageView.setImageResource(results.get(position).getImg());
        holder.imageView.setImageDrawable(context.getResources().getDrawable(results.get(position).getImg()));
    }

    @Override
    public int getItemCount() {
        return results.size();
    }

    /**
     * 位置进行交换，移动
     * @param fromPosition
     * @param toPosition
     */
    @Override
    public void onMove(int fromPosition, int toPosition) {

        if (fromPosition==results.size()-1 || toPosition==results.size()-1){
            return;
        }
        if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(results, i, i + 1);
            }
        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(results, i, i - 1);
            }
        }
        notifyItemMoved(fromPosition, toPosition);
//        notifyDataSetChanged();
    }



    public class MyViewHolder extends RecyclerView.ViewHolder{

        public ImageView imageView;

        public MyViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(v.getContext(),"test",Toast.LENGTH_LONG).show();
                }
            });
            WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            int width = wm.getDefaultDisplay().getWidth();
            ViewGroup.LayoutParams layoutParams = itemView.getLayoutParams();
            layoutParams.height = width/4;
            itemView.setLayoutParams(layoutParams);
            imageView = (ImageView) itemView.findViewById(R.id.item_img);
        }
    }
}
