package com.example.roomnote.items;

import com.example.roomnote.recyclervIewmodule.RecyclerItem;

import java.text.SimpleDateFormat;
import java.util.Date;

public abstract class ChattingItem extends RecyclerItem {
    protected String nickname;
    protected String chat;
    protected Date time;

    public ChattingItem(String nickname, String chat, Date time) {
        super();
        this.chat = chat;
        this.nickname = nickname;
        this.time = time;
    }

    public String getChat(){
        return chat;
    }
    public String getNickname(){
        return nickname;
    }
    public String getDate(){
        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
        return format.format(time);
    }
}
