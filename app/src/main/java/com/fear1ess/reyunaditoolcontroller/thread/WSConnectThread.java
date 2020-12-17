package com.fear1ess.reyunaditoolcontroller.thread;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.MenuItem;

import androidx.annotation.NonNull;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;
import com.fear1ess.reyunaditoolcontroller.cmd.WSConnectCmd.ServerCmd;
import com.fear1ess.reyunaditoolcontroller.MainUIHandler;

public class WSConnectThread implements Runnable {

    public static String TAG = "reyunaditoolcontroller_log";
    public static String wsServerUrl = "ws://172.23.6.225:2020";

    private OkHttpClient mOkhttpClient;

    private WebSocket mWebSocket;

    private Handler mainUiHandler;
    private Handler mThreadHandler;

    private boolean isConnected = false;

    public WSConnectThread(Handler handler, OkHttpClient okc){
        mainUiHandler = handler;
        mOkhttpClient = okc;
    }

    public void reconnect(){
        connect();
    }

    public void connect(){
        //connect to server's device
        int tid = (int) Thread.currentThread().getId();
        Log.d(TAG, "tid1: " + tid);
        Request request = new Request.Builder().url(wsServerUrl).build();
        mWebSocket = mOkhttpClient.newWebSocket(request, new WSConnectListener());
       // mOkhttpClient.dispatcher().executorService().shutdown();
    }

    @Override
    public void run() {
        connect();
    }

    public class WSConnectListener extends WebSocketListener{
        @Override
        public void onOpen(@NotNull WebSocket webSocket, @NotNull Response response) {

            isConnected = (response.code() == 101);

            if(!isConnected) {
                Log.d(TAG, "failed to connect websocket server!");
            }

            Log.d(TAG, "success to connect websocket server!");

            sendMessageToMainUI(MainUIHandler.WEBSOCKET_CONNECT_SUCCESS, webSocket);
        }

        public void sendMessageToMainUI(int what, Object obj){
            Message msg = Message.obtain();
            msg.what = what;
            msg.obj = obj;
            mainUiHandler.sendMessage(msg);
        }

        @Override
        public void onMessage(@NotNull WebSocket webSocket, @NotNull String text) {
            try {
                Log.d(TAG, "onMessage: " + text);
                JSONObject jo = new JSONObject(text);
                if(!jo.has("cmd")){
                    webSocket.send("data format error, no cmd param!");
                    return;
                }
                int cmd = jo.getInt("cmd");
                switch (cmd){
                    case ServerCmd.NEW_PUSH_MSG:{
                        sendMessageToMainUI(MainUIHandler.NEW_PUSH_DATA, jo.getJSONObject("data"));
                        break;
                    }
                }

            } catch (JSONException e) {
                webSocket.send("data format error, not a json-format data!");
            }
        }

        @Override
        public void onMessage(@NotNull WebSocket webSocket, @NotNull ByteString bytes) {
            super.onMessage(webSocket, bytes);
        }

        @Override
        public void onFailure(@NotNull WebSocket webSocket, @NotNull Throwable t, @Nullable Response response) {
            Log.d(TAG, "failed to connect websocket server!");
            int tid = (int) Thread.currentThread().getId();
            Log.d(TAG, "tid2: " + tid);
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            reconnect();
        }

        @Override
        public void onClosing(@NotNull WebSocket webSocket, int code, @NotNull String reason) {
            super.onClosing(webSocket, code, reason);
        }

        @Override
        public void onClosed(@NotNull WebSocket webSocket, int code, @NotNull String reason) {
            super.onClosed(webSocket, code, reason);
        }
    }

    public static class WSServerMsg{
    }

}
