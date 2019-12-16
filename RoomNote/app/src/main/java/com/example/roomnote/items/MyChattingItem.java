package com.example.roomnote.items;

import android.widget.TextView;

import com.example.roomnote.R;
import com.example.roomnote.recyclervIewmodule.RecyclerAdapter;

import java.util.Date;

public class MyChattingItem extends ChattingItem {

    public MyChattingItem(String nickname, String chat, Date time) {
        super(nickname, chat, time);
    }

    @Override
    public int getLayoutRes() {
        return R.layout.ritem_mychat;
    }

    @Override
    public void onBind(RecyclerAdapter.ViewHolder holder) {
        TextView tv = holder.itemView.findViewById(R.id.my_chat);
        tv.setText(chat);
    }
}
