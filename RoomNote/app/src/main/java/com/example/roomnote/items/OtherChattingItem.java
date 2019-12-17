package com.example.roomnote.items;

import android.widget.TextView;

import androidx.databinding.DataBindingUtil;

import com.example.roomnote.R;
import com.example.roomnote.databinding.RitemOtherchatBinding;
import com.example.roomnote.recyclervIewmodule.RecyclerAdapter;

import java.util.Date;

public class OtherChattingItem extends ChattingItem {

    public OtherChattingItem(String nickname, String chat, Date time) {
        super(nickname, chat, time);
    }

    @Override
    public int getLayoutRes() {
        return R.layout.ritem_otherchat;
    }

    @Override
    public void onBind(RecyclerAdapter.ViewHolder holder) {
//        TextView tv = holder.itemView.findViewById(R.id.other_chat);
//        tv.setText(chat);
        RitemOtherchatBinding binding = DataBindingUtil.bind(holder.itemView);
        binding.setModel(this);
    }
}
