package com.example.roomnote.recyclervIewmodule;

import android.content.Context;
import android.util.Log;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class RecyclerGridAdapter extends RecyclerAdapter2 {
    private String TAG = "RecyclerGridAdapter";

    private final int mSpanCount;

    public RecyclerGridAdapter(Context context, RecyclerView rView){
        this(context, rView, 6);
    }
    public RecyclerGridAdapter(Context context, RecyclerView rView, int spanCount){
        mSpanCount = spanCount;

        rView.setAdapter(this);
        GridLayoutManager layoutManager = new GridLayoutManager(context, mSpanCount);
        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup(){
            @Override
            public int getSpanSize(int position) {
                if(itemList.get(position) instanceof IRecyclerGridItem){
                    int rowCount = ((IRecyclerGridItem) itemList.get(position)).getRowCount();
                    return mSpanCount / rowCount;
                }
                else{
                    Log.d(TAG, "getSpanSize: " + "is IRecyclerItem");
                    return 6;
                }
            }
        });

    }

}
