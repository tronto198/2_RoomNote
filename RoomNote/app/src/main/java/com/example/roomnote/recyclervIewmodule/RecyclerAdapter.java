package com.example.roomnote.recyclervIewmodule;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

    public class ViewHolder extends RecyclerView.ViewHolder{
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    public interface OnItemClickListener{
        void onClick(View view, int position);
    }

    List<RecyclerItem> itemList = new ArrayList<>();
    public OnItemClickListener mItemClickListener = null;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        @LayoutRes int res = RecyclerItem.getLayoutRes(viewType);
        View v = LayoutInflater.from(parent.getContext()).inflate(res, parent, false);
        ViewHolder vh = new ViewHolder(v);

        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        final RecyclerItem item = itemList.get(position);
        if(mItemClickListener != null)
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        mItemClickListener.onClick(view, position);
                    }
                    catch (Exception e){
                        //ignore
                    }
                }
            });
        item.onBind(holder);
    }

    @Override
    public int getItemViewType(int position) {
        return itemList.get(position).getItemType();
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public List getItemList(){
        return itemList;
    }
//    public void setItemList(List list){
//        this.itemList = list;
//    }

    public void add(RecyclerItem item){
        itemList.add(item);
    }
    public void addFront(RecyclerItem item){
        itemList.add(0, item);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        this.mItemClickListener = listener;
    }
}
