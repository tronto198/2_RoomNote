package com.example.roomnote;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentController;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import com.example.roomnote.ui.roomlist.RoomListFragment;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        ApplicationSharedRepository.setAppContext(getApplicationContext());

//        if (savedInstanceState == null) {
//            getSupportFragmentManager().beginTransaction()
//                    .replace(R.id.container, RoomListFragment.newInstance())
//                    .commitNow();
//        }



    }


}
