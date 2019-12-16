package com.example.roomnote.ui.roomlist;

import androidx.lifecycle.ViewModel;

import com.example.roomnote.items.ChatRoomItem;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class RoomListViewModel extends ViewModel {
    // TODO: Implement the ViewModel
    List roomList = new ArrayList<>();

    public void setRoomList(List list){
        roomList = list;
    }
    public List<ChatRoomItem> getRoomList() {
        return roomList;
    }

    public void addRoomList(JSONArray array){
        int length = array.length();
        try {
            for (int i = 0; i < length; i++) {
                JSONObject json = array.getJSONObject(i);
                addRoom(json);
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public void addRoom(JSONObject json){
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            Date fetchTime = dateFormat.parse(json.getString("last_fetch_date").substring(0,20));
            Date last_modifyed = dateFormat.parse(json.getString("last_modifyed"));
            Boolean notify = false;

            if(last_modifyed.after(fetchTime))
                notify = true;
            roomList.add(new ChatRoomItem(json.getInt("room_id"),
                    json.getString("room_title"), json.getString("user_nickname"), notify));
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
    public void addRoom(int roomId, String roomTitle, String nickname, boolean notify){
        roomList.add(new ChatRoomItem(roomId, roomTitle, nickname, notify));

    }

    public ChatRoomItem getItem(int index){
        return (ChatRoomItem)roomList.get(index);
    }
}
