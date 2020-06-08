package com.example.roomnote.recyclervIewmodule;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class RecyclerAdapter2 extends RecyclerView.Adapter<RecyclerAdapter2.ViewHolder> {

    public class ViewHolder extends RecyclerView.ViewHolder{
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    public interface OnItemClickListener{
        void onClick(View view, int position);
    }

    List<IRecyclerItem> itemList = new ArrayList<>();
    public OnItemClickListener mItemClickListener = null;

    protected RecyclerAdapter2(){}

    public RecyclerAdapter2(Context context, RecyclerView rView){
        rView.setAdapter(this);
        rView.setLayoutManager(new LinearLayoutManager(context));
    }
    public RecyclerAdapter2(Context context, RecyclerView rView, int orientation, boolean reverseLayout){
        rView.setAdapter(this);
        rView.setLayoutManager(new LinearLayoutManager(context, orientation, reverseLayout));
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        @LayoutRes int res = RecyclerDistributor.getItemLayout(viewType);
        View v = LayoutInflater.from(parent.getContext()).inflate(res, parent, false);
        ViewHolder vh = new ViewHolder(v);

        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        final IRecyclerItem item = itemList.get(position);
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
        return RecyclerDistributor.getItemtype(itemList.get(position));
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

    public void add(IRecyclerItem item){
        itemList.add(item);
    }
    public void addFront(IRecyclerItem item){
        itemList.add(0, item);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        this.mItemClickListener = listener;
    }
}
