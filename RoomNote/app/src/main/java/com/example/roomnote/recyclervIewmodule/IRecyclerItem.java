package com.example.roomnote.recyclervIewmodule;

import androidx.annotation.LayoutRes;

import java.util.ArrayList;
import java.util.List;

public interface IRecyclerItem {

    @LayoutRes
    int getLayoutRes();

    String getItemName();

    void onBind(RecyclerAdapter2.ViewHolder holder);

}