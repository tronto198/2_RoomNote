package com.example.roomnote.recyclervIewmodule;

import androidx.annotation.LayoutRes;

import java.util.ArrayList;
import java.util.List;

public abstract class RecyclerItem {
    private static List<Integer> layoutList = new ArrayList<>();
    private int itemType;

    public RecyclerItem() {
        if (layoutList.contains(this.getLayoutRes())) {
            this.itemType = layoutList.indexOf(this.getLayoutRes());
        } else {
            this.itemType = layoutList.size();
            layoutList.add(this.getLayoutRes());
        }
    }

    int getItemType() {
        return itemType;
    }
    static int getLayoutRes(int itemType){
        return layoutList.get(itemType);
    }

    @LayoutRes
    public abstract int getLayoutRes();

    public abstract void onBind(RecyclerAdapter.ViewHolder holder);

}