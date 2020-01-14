package com.example.roumeliotis.coen242projectapp;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

public class VolleySingleton {

    private static VolleySingleton INSTANCE = null;
    private static Context mContext;

    public static final String TAG = "VolleySingleton";
    private static final String base_url = "http://3.15.172.12:80/";
    RequestQueue mQueue;

    private VolleySingleton(Context context){
        mContext = context;
        mQueue = getRequestQueue();
    }

    public static synchronized VolleySingleton getInstance(Context context){
        if (INSTANCE == null)
            INSTANCE = new VolleySingleton(context);
        return INSTANCE;
    }

    public RequestQueue getRequestQueue(){
        if (mQueue == null)
            mQueue = Volley.newRequestQueue(mContext.getApplicationContext());
        return mQueue;
    }

    public void addToQueue(JsonObjectRequest req){
        getRequestQueue().add(req);
    }

    //TODO add cancel all pending requests function to be used for when the app is closed
}
