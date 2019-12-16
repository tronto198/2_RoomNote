package com.example.roomnote.items;

import android.widget.ImageView;

import com.example.roomnote.R;
import com.example.roomnote.recyclervIewmodule.RecyclerAdapter;
import com.example.roomnote.recyclervIewmodule.RecyclerItem;

public class addroomItem extends RecyclerItem {
    @Override
    public int getLayoutRes() {
        return R.layout.ritem_addroom;
    }

    @Override
    public void onBind(RecyclerAdapter.ViewHolder holder) {
        ImageView img = holder.itemView.findViewById(R.id.imageView);
        img.setAlpha(50);
    }
}
