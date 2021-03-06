package com.example.roomnote.ui.chattingFragment;

import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.roomnote.ApplicationSharedRepository;
import com.example.roomnote.R;
import com.example.roomnote.recyclervIewmodule.RecyclerAdapter;

import org.json.JSONArray;
import org.json.JSONObject;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class ChattingFragment extends Fragment {

    private ChattingViewModel mViewModel;
    RecyclerAdapter adapter;
    RecyclerView rv;
    Socket msocket;//, chatSocket;
    int roomId;
    int chatpointer = 0;

    public static ChattingFragment newInstance() {
        return new ChattingFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.chatting_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(ChattingViewModel.class);
        // TODO: Use the ViewModel
        ChattingFragmentArgs args =ChattingFragmentArgs.fromBundle(getArguments());
        roomId = args.getRoomId();
        String title = args.getTitle();
        String nickname = args.getNickname();

        rv = getView().findViewById(R.id.chat_chattingList);
        rv.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, true));
        adapter = new RecyclerAdapter();
        rv.setAdapter(adapter);

        mViewModel.setChattingList(adapter.getItemList());


        final EditText et_chat = getView().findViewById(R.id.chat_et_chatting);
        Button bt_chat = getView().findViewById(R.id.chat_bt_enter);
        bt_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String chat = et_chat.getText().toString();
                et_chat.setText(null);
                Chatted(chat);
            }
        });

        ((TextView)getView().findViewById(R.id.chat_header_title)).setText(title);
        ((TextView)getView().findViewById(R.id.chat_header_nickname)).setText(nickname);


        SocketBuild();
    }

    //소켓 on, emit 지정 후 연결
    void SocketBuild(){
        //emit configure
        //emit load
        //emit chat
        //on userList
        //on chatList
        //on chat

        try {
            msocket = IO.socket("http://13.125.224.21:55252");
            //chatSocket = IO.socket("http://13.125.224.21:55252/chat/" + roomId);
            msocket.on("userlist", new Emitter.Listener() {
                @Override
                public void call(Object... args) {
//                    Toast.makeText(getActivity(), "load user", Toast.LENGTH_SHORT).show();
                    JSONObject data = (JSONObject) args[0];
                    try{
                        JSONArray userlist = data.getJSONArray("userList");
                        for(int i = 0; i < userlist.length(); i++){
                            mViewModel.addUserinRoom(userlist.getJSONObject(i));
                        }
                        Load();
                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }
                }
            });

            msocket.on("chatlist", new Emitter.Listener() {
                @Override
                public void call(Object... args) {
//                    Toast.makeText(getActivity(), "load chat", Toast.LENGTH_SHORT).show();
                    JSONObject data = (JSONObject) args[0];
                    try{
                        JSONArray chatlist = data.getJSONArray("chatList");
                        final int length = chatlist.length();
                        for(int i = length - 1; i >= 0; i--){
                            mViewModel.addChatting(chatlist.getJSONObject(i));
                        }
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if(adapter.getItemCount() < 3){
                                    adapter.notifyDataSetChanged();
                                }
                                else{
                                    adapter.notifyItemInserted(chatpointer);
                                }

                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        rv.scrollToPosition(chatpointer);
                                        chatpointer += length;
                                    }
                                },200);
                            }
                        });
                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }
                }
            });

            msocket.on("chatted", new Emitter.Listener() {
                @Override
                public void call(Object... args) {
//                    Toast.makeText(getActivity(), "chat", Toast.LENGTH_SHORT).show();
                    Log.d("hj", "call: chatting received");
                    JSONObject data = (JSONObject) args[0];
                    mViewModel.addFrontChatting(data);
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            // TODO: 2019-12-16 첫번째 채팅이 보이지 않아
                            if(adapter.getItemCount() < 3){
                                adapter.notifyDataSetChanged();
                            }
                            else{
                                adapter.notifyItemInserted(0);
                            }
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    chatpointer++;
                                }
                            },200);
                        }
                    });
                }
            });

            msocket.on("error", new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getActivity(), "error", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });
            msocket.connect();
            Toast.makeText(getActivity(), "소켓 연결됨", Toast.LENGTH_SHORT).show();
            Configure();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
    //서버와 통신하여 설정
    void Configure(){
        try{
            JSONObject json = new JSONObject();
            json.put("roomId", roomId);
            json.put("userId", ApplicationSharedRepository.getId());

            msocket.emit("configure", json);
        }
        catch (Exception e){
            e.printStackTrace();
            Toast.makeText(getActivity(), "error", Toast.LENGTH_SHORT).show();
        }
    }
    //채팅내역 로드
    void Load(){
        try{
            JSONObject json = new JSONObject();
            json.put("roomId", roomId);
            json.put("loadNo", chatpointer);
            msocket.emit("load", json);

        }
        catch (Exception e){
            e.printStackTrace();
            Toast.makeText(getActivity(), "error", Toast.LENGTH_SHORT).show();
        }
    }
    //채팅
    void Chatted(String chat){
        try{
                JSONObject json = new JSONObject();
                json.put("roomId", roomId);
                json.put("userId", ApplicationSharedRepository.getId());
                json.put("contents", chat);
                msocket.emit("chatting", json);

        }
        catch (Exception e){
            e.printStackTrace();
            Toast.makeText(getActivity(), "error", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        msocket.disconnect();
    }
}
