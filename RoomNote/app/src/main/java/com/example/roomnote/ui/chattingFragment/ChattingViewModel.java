package com.example.roomnote.ui.chattingFragment;

import androidx.lifecycle.ViewModel;

import com.example.roomnote.ApplicationSharedRepository;
import com.example.roomnote.items.ChattingItem;
import com.example.roomnote.items.MyChattingItem;
import com.example.roomnote.items.OtherChattingItem;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.socket.client.Socket;

public class ChattingViewModel extends ViewModel {
    // TODO: Implement the ViewModel

    Map<Integer, String> nicknameMap = new HashMap<>();
    List<ChattingItem> chattingList;

    public void setChattingList(List list){
        chattingList = list;
    }

    public void addChatting(JSONObject json){
        try{
            int userId = json.getInt("user_id");
            String nickname = nicknameMap.get(userId);
            String contents = json.getString("contents");
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            Date chattime = format.parse(json.getString("chat_time").substring(0,20));

            if(userId == ApplicationSharedRepository.getId()){
                chattingList.add(new MyChattingItem(nickname, contents, chattime));
            }
            else{
                chattingList.add(new OtherChattingItem(nickname, contents, chattime));
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public void addFrontChatting(JSONObject json){
        try{
            int userId = json.getInt("user_id");
            String nickname = nicknameMap.get(userId);
            String contents = json.getString("contents");
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            Date chattime = format.parse(json.getString("chat_time").substring(0,20));

            if(userId == ApplicationSharedRepository.getId()){
                chattingList.add(0, new MyChattingItem(nickname, contents, chattime));
            }
            else{
                chattingList.add(0, new OtherChattingItem(nickname, contents, chattime));
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }

    }

    public void addUserinRoom(JSONObject json){
        try{
            int id = json.getInt("user_id");
            String nickname = json.getString("user_nickname");
            nicknameMap.put(id, nickname);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

//    void BuildConnnection(String roomNo){
//        try{
//            socket = IO.socket("");
//            socket.connect();
//        }
//        catch (Exception e){
//            e.printStackTrace();
//        }
//
//        socket.on("configure", new Emitter.Listener() {
//            @Override
//            public void call(Object... args) {
//
//            }
//        });
//    }


}
