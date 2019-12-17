package com.example.roomnote.items;

import com.example.roomnote.recyclervIewmodule.RecyclerItem;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public abstract class ChattingItem extends RecyclerItem {
    protected String nickname;
    protected String chat;
    protected Date date;

    public ChattingItem(String nickname, String chat, Date time) {
        this.chat = chat;
        this.nickname = nickname;

        Calendar cal = Calendar.getInstance();
        cal.setTime(time);
        cal.add(Calendar.HOUR, 9);
        this.date = cal.getTime();
    }

    public String getChat(){
        return chat;
    }
    public String getNickname(){
        return nickname;
    }
    public String getDate(){
        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
        return format.format(date);
    }
}
