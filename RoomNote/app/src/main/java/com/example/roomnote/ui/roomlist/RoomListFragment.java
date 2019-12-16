package com.example.roomnote.ui.roomlist;

import androidx.lifecycle.ViewModelProviders;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.roomnote.ApplicationSharedRepository;
import com.example.roomnote.HttpConnecter;
import com.example.roomnote.R;
import com.example.roomnote.recyclervIewmodule.RecyclerAdapter;
import com.example.roomnote.items.ChatRoomItem;
import com.example.roomnote.items.addroomItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static android.content.Context.MODE_PRIVATE;

public class RoomListFragment extends Fragment {

    HttpConnecter httpConnecter;
    RecyclerAdapter adapter;
    private RoomListViewModel mViewModel;

    public static RoomListFragment newInstance() {
        return new RoomListFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.roomlist_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(getActivity()).get(RoomListViewModel.class);


        httpConnecter = HttpConnecter.getinstance(R.string.server_ip, R.string.server_port);

        SharedPreferences pref = getActivity().getSharedPreferences("save", MODE_PRIVATE);
        if(!pref.contains("id")){
            FirstConnect(pref);
        }
        else{
            autoLogin(pref.getInt("id", 0));
        }

        RecyclerView rv = getView().findViewById(R.id.main_roomList);
        adapter = new RecyclerAdapter();
        rv.setLayoutManager(new LinearLayoutManager(getContext()));//, LinearLayoutManager.VERTICAL, true));
        rv.setAdapter(adapter);

        mViewModel.setRoomList(adapter.getItemList());
        adapter.add(new addroomItem());
        //adapter.add(new ChatRoomItem(1, "2", "3", true));
        adapter.notifyDataSetChanged();

        adapter.setOnItemClickListener(new RecyclerAdapter.OnItemClickListener() {
            NavController navController = Navigation.findNavController(getView());
            @Override
            public void onClick(View view, int position) {
                if(position == 0){
                    navController.navigate(RoomListFragmentDirections.actionRoomListFragmentToAddRoomFragment());
                }
                else {
                    ChatRoomItem room = mViewModel.getItem(position);

                    RoomListFragmentDirections.ActionRoomListFragmentToChattingFragment action =
                            RoomListFragmentDirections.actionRoomListFragmentToChattingFragment(
                                    room.getRoomId(), room.getRoomTitle(), room.getMyNickname());
                    //action.setRoomId(roomId);
                    navController.navigate(action);
                }
            }
        });

    }


    void FirstConnect(final SharedPreferences pref){
        httpConnecter.Get("/FirstConnect", null, new HttpConnecter.ResponseRecivedCallback() {
            @Override
            public void DataReceived(JSONObject data) throws JSONException {

            }

            @Override
            public void DataInvoked(JSONObject data) throws JSONException {
                if(data.getBoolean(KEY_RESULT)){
                    //첫 접속 성공
                    int id = data.getInt("id");
                    pref.edit().putInt("id", id).apply();
                    Toast.makeText(getActivity(), "id : " + id + " 받아옴", Toast.LENGTH_SHORT).show();
                    autoLogin(id);
                }
                else{
                    Toast.makeText(getActivity(), data.getString("description"), Toast.LENGTH_SHORT).show();
                    getActivity().finish();
                }
            }

            @Override
            public void ConnectionFailed(Exception e) {
                e.printStackTrace();
                Toast.makeText(getActivity(), "서버와 연결할 수 없어요.", Toast.LENGTH_SHORT).show();
                getActivity().finish();
            }
        });
    }


    void autoLogin(final int id){
        JSONObject json = new JSONObject();
        try {
            json.put("id", id);
            httpConnecter.Post("/Connect", json, new HttpConnecter.ResponseRecivedCallback() {
                @Override
                public void DataReceived(JSONObject data) throws JSONException {
                    if(data.getBoolean(KEY_RESULT)){
                        //로그인 성공
                        //룸 리스트를 받음
                        //mviewmodel.set();

                        try {
                            JSONArray RoomArray = data.getJSONArray("roomlist");
                            mViewModel.addRoomList(RoomArray);
                        }
                        catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void DataInvoked(JSONObject data) throws JSONException {
                    if(data.getBoolean(KEY_RESULT)){
                        //로그인 성공
                        //룸 리스트를 받음
                        //mviewmodel.set();
                        adapter.notifyDataSetChanged();
                        ApplicationSharedRepository.setId(id);
                        Toast.makeText(getActivity(), "id : " + id + " 로그인 성공", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Toast.makeText(getActivity(), data.getString("description"), Toast.LENGTH_SHORT).show();
                        getActivity().finish();
                    }
                }

                @Override
                public void ConnectionFailed(Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity(), "서버와 연결할 수 없어요.", Toast.LENGTH_SHORT).show();
                    getActivity().finish();
                }
            });
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
