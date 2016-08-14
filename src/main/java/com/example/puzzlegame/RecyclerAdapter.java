package com.example.puzzlegame;

import android.graphics.Bitmap;
import android.support.v4.widget.ViewDragHelper;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;

import java.util.List;

/**
 * Created by liuxian on 2016/8/11.
 */

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {
    private List<Bitmap> mData;

    public RecyclerAdapter(List<Bitmap> data){
        mData = data;
    }

    public OnItemClickListener itemClickListener;

   public void setOnItemClickListener(OnItemClickListener itemClickListener){
        this.itemClickListener = itemClickListener;
    }

    public interface OnItemClickListener{
        void onItemClick(View view , int position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public ImageView imageView;

        public ViewHolder(View itemView){
            super(itemView);
            imageView = (ImageView) itemView;
            imageView.setOnClickListener(this);
        }
        public void onClick(View v){
            if(itemClickListener != null){
                itemClickListener.onItemClick(v , getPosition());
            }
        }
    }

    public ViewHolder onCreateViewHolder(ViewGroup viewGroup , int i){
        //将布局转化为View 并传递给RecyclerView封装好的ViewHolder
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recyc_item , viewGroup , false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.imageView.setImageBitmap(mData.get(position));
    }

    public int getItemCount(){
        return mData.size();
    }

}
