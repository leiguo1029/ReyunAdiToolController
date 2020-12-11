package com.fear1ess.reyunaditoolcontroller;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;

import androidx.annotation.NonNull;

import java.lang.ref.WeakReference;

import okhttp3.WebSocket;

public class MainUIHandler extends Handler {
    public final static int WEBSOCKET_CONNECT_SUCCESS = 1;

    private WeakReference<MainActivity> mActivityRef;
    public MainUIHandler(MainActivity act){
        mActivityRef = new WeakReference<>(act);
    }

    @Override
    public void handleMessage(@NonNull Message msg) {
        switch (msg.what){
            case WEBSOCKET_CONNECT_SUCCESS:{
                MainActivity act = mActivityRef.get();
                act.setWebSocketConn((WebSocket) msg.obj);
                break;
            }
            default: break;
        }
    }
}
