package com.example.roomnote;

import android.content.Context;

public class ApplicationSharedRepository {
    static Context appContext = null;
    static int id;

    public static void setAppContext(Context context){
        appContext = context;
    }

    public static Context getAppContext(){
        return appContext;
    }

    public static void setId(int _id){
        id = _id;
    }

    public static int getId(){
        return id;
    }
}
