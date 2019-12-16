package com.example.roomnote.ui.addroom;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.roomnote.ApplicationSharedRepository;
import com.example.roomnote.HttpConnecter;
import com.example.roomnote.R;
import com.example.roomnote.ui.roomlist.RoomListViewModel;

import org.json.JSONException;
import org.json.JSONObject;

public class AddRoomFragment extends Fragment {
    private AddRoomViewModel mViewModel;

    EditText et_RoomTitle;
    EditText et_RoomPassword;
    EditText et_NickName;
    HttpConnecter httpConnecter;

    public static AddRoomFragment newInstance() {
        return new AddRoomFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.addroom_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(AddRoomViewModel.class);
        // TODO: Use the ViewModel

        httpConnecter = HttpConnecter.getinstance(R.string.server_ip, R.string.server_port);

        et_RoomTitle = getView().findViewById(R.id.addroom_roomtitle);
        et_RoomPassword = getView().findViewById(R.id.addroom_roompassword);
        et_NickName = getView().findViewById(R.id.addroom_nickname);

        Button bt_JoinRoom = getView().findViewById(R.id.addroom_roomenter);
        Button bt_CreateRoom = getView().findViewById(R.id.addroom_roomcreate);

        bt_JoinRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JSONObject data = getInfo();
                httpConnecter.Post("/JoinRoom", data, new HttpConnecter.ResponseRecivedCallback() {
                    @Override
                    public void DataReceived(JSONObject data) throws JSONException {

                    }

                    @Override
                    public void DataInvoked(JSONObject data) throws JSONException {
                        if(data.getBoolean(KEY_RESULT)){
                            joinRoom(data.getInt("roomId"), data.getString("roomTitle"), data.getString("nickname"));
                        }
                        else{
                            Toast.makeText(getActivity(), data.getString("description"), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void ConnectionFailed(Exception e) {
                        e.printStackTrace();
                        Toast.makeText(getActivity(), "서버와 연결중 오류가 발생했어요.", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        bt_CreateRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JSONObject data = getInfo();
                httpConnecter.Post("/CreateRoom", data, new HttpConnecter.ResponseRecivedCallback() {
                    @Override
                    public void DataReceived(JSONObject data) throws JSONException {

                    }

                    @Override
                    public void DataInvoked(JSONObject data) throws JSONException {
                        if(data.getBoolean(KEY_RESULT)){
                            joinRoom(data.getInt("roomId"), data.getString("roomTitle"), data.getString("nickname"));
                        }
                        else{
                            Toast.makeText(getActivity(), data.getString("description"), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void ConnectionFailed(Exception e) {
                        e.printStackTrace();
                        Toast.makeText(getActivity(), "서버와 연결중 오류가 발생했어요.", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    JSONObject getInfo(){
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(et_NickName.getWindowToken(), 0);
        int id = ApplicationSharedRepository.getId();
        String RoomTitle = et_RoomTitle.getText().toString();
        String Passwd = et_RoomPassword.getText().toString();
        String Nickname = et_NickName.getText().toString();

        JSONObject json = new JSONObject();
        try{
            json.put("id", id);
            json.put("title", RoomTitle);
            json.put("password", Passwd);
            json.put("nickname", Nickname);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return json;
    }

    void joinRoom(int roomId, String roomTitle, String nickname){
        RoomListViewModel roomListViewModel = ViewModelProviders.of(getActivity()).get(RoomListViewModel.class);
        roomListViewModel.addRoom(roomId, roomTitle, nickname, false);
        NavController navController = Navigation.findNavController(getView());
        AddRoomFragmentDirections.ActionAddRoomFragmentToChattingFragment action =
                AddRoomFragmentDirections.actionAddRoomFragmentToChattingFragment(
                        roomId, roomTitle, nickname);
        //action.setRoomId(roomId);
        navController.navigate(action);
    }
}
