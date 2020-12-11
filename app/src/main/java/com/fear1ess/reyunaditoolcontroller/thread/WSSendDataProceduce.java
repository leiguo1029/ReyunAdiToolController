package com.fear1ess.reyunaditoolcontroller.thread;

import android.util.Log;

import okhttp3.WebSocket;

public class WSSendDataProceduce implements Runnable {
    private String mData;
    private WebSocket mWebSocket;
    public static String TAG = "reyunaditoolcontroller_log";

    public WSSendDataProceduce(WebSocket ws, String data){
        mData = data;
        mWebSocket = ws;
    }
    @Override
    public void run() {
        Log.d(TAG, "websocket send data: " + mData);
        mWebSocket.send(mData);
    }
}
