package com.example.roomnote.items;

import androidx.databinding.DataBindingUtil;

import com.example.roomnote.R;
import com.example.roomnote.recyclervIewmodule.RecyclerAdapter;
import com.example.roomnote.recyclervIewmodule.RecyclerItem;
import com.example.roomnote.databinding.RitemRoomBinding;

public class ChatRoomItem extends RecyclerItem {
    int roomId;
    String roomTitle;
    String myNickname;
    boolean notify;

    public ChatRoomItem(int roomId, String roomTitle, String nickName, boolean isNotify) {
        super();
        this.roomId = roomId;
        this.roomTitle = roomTitle;
        this.myNickname = nickName;
        this.notify = isNotify;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.ritem_room;
    }

    @Override
    public void onBind(RecyclerAdapter.ViewHolder holder) {
        RitemRoomBinding binding = DataBindingUtil.bind(holder.itemView);
        binding.setModel(this);
    }

    public int getRoomId(){
        return roomId;
    }
    public String getRoomTitle(){
        return roomTitle;
    }
    public String getMyNickname(){
        return myNickname;
    }
    public boolean getNotify(){
        return notify;
    }
}
