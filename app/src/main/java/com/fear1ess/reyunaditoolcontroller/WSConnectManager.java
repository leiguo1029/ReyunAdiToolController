package com.fear1ess.reyunaditoolcontroller;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.fear1ess.reyunaditoolcontroller.cmd.WSConnectCmd;
import com.fear1ess.reyunaditoolcontroller.thread.WSSendDataProceduce;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;

public class WSConnectManager {
    public static String TAG = "aditoolcontroller_log";
    private static WSConnectManager mInstance = new WSConnectManager();
    private WebSocket mWebSocket;
    private int mIndex;
    private OkHttpClient mOkhttpClient;
    private volatile boolean wsConnected = false;
    private volatile boolean wsThreadRunning = false;
    private List<WSConnectThread> mWSConnectThreads = new ArrayList<>(AdiToolControllerApp.getMaxDeviceNum());

    public WSConnectManager() {
        //connect to websocket server...
        try {
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null,new TrustManager[]{new AdiToolControllerApp.TrustAllCertificateManager()},new SecureRandom());
            if(mOkhttpClient == null){
                mOkhttpClient = new OkHttpClient.Builder()
                        .pingInterval(3, TimeUnit.SECONDS)//设置ping帧发送间隔
                        .hostnameVerifier(new AdiToolControllerApp.TrustAllHostnamesVerifier())
                        .sslSocketFactory(sc.getSocketFactory(),new AdiToolControllerApp.TrustAllCertificateManager())
                        .build();
            }
        } catch (KeyManagementException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    public void setWebSocket(WebSocket ws) {
        mWebSocket = ws;
    }

    public WebSocket getWebSocket() {
        return mWebSocket;
    }

    public static WSConnectManager getInstance() {
        return mInstance;
    }

    public void newWSConnect(int index, String address) {
        String ipAddress = "ws://" + address;
        WSConnectThread item = mWSConnectThreads.get(index);
        if(item == null) {
            ExecutorService es = Executors.newSingleThreadExecutor();
            WSConnectThread wct = new WSConnectThread(AdiToolControllerApp.getUiHandler(), mOkhttpClient, ipAddress, index);
            mWSConnectThreads.set(index, wct);
            es.execute(wct);
            es.shutdown();
            wsThreadRunning = true;
        } else {
            item.changeServerAddress(ipAddress);
        }
    }

    private void WSSendData(String data){
        ExecutorService es = Executors.newSingleThreadExecutor();
        es.execute(new WSSendDataProceduce(mWebSocket, data));
        es.shutdown();
    }

    public void doCommand(int cmd){
        if(mWebSocket == null) return;
        JSONObject jo = new JSONObject();
        try {
            jo.put("cmd", cmd);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        WSSendData(jo.toString());
    }


    public boolean isWSConnected() {
        return wsConnected;
    }
    public void setWebSocketConn(WebSocket ws){
        wsConnected = true;
        mWebSocket = ws;
    }

    public class WSConnectThread implements Runnable {

        public String wsServerUrl;
        private OkHttpClient mOkhttpClient;
        private int mIndex;
        private WebSocket mWebSocket;
        private Handler mainUiHandler;
        private boolean isConnected = false;

        public WSConnectThread(Handler handler, OkHttpClient okc, String wsIpAddress, int index){
            mainUiHandler = handler;
            mOkhttpClient = okc;
            wsServerUrl = wsIpAddress;
            mIndex = index;
        }

        public void changeServerAddress(String address) {
            wsServerUrl = address;
        }

        public void reconnect(){
            connect();
        }

        public void connect(){
            //connect to server's device
            synchronized (wsServerUrl){
                Request request = new Request.Builder().url(wsServerUrl).build();
                mWebSocket = mOkhttpClient.newWebSocket(request, new WSConnectListener());
            }
            // mOkhttpClient.dispatcher().executorService().shutdown();
        }

        @Override
        public void run() {

            connect();
        }

        public class WSConnectListener extends WebSocketListener {

            @Override
            public void onOpen(@NotNull WebSocket webSocket, @NotNull Response response) {

                isConnected = (response.code() == 101);

                if(!isConnected) {
                    Log.d(TAG, "failed to connect websocket server!");
                }

                Log.d(TAG, "success to connect websocket server!");

                setWebSocketConn(webSocket);

                sendMessageToMainUI(MainUIHandler.WEBSOCKET_CONNECT_SUCCESS, wsServerUrl);
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
                        case WSConnectCmd.ServerCmd.NEW_APP_PUSH_MSG:{
                            sendMessageToMainUI(MainUIHandler.NEW_APP_PUSH_DATA, jo.getJSONObject("data"));
                            break;
                        }
                        case WSConnectCmd.ServerCmd.NEW_ADS_PUSH_MSG:{
                            sendMessageToMainUI(MainUIHandler.NEW_ADS_PUSH_DATA, jo.getJSONObject("data"));
                            break;
                        }
                        default: break;
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
                sendMessageToMainUI(MainUIHandler.WEBSOCKET_CONNECT_FAILED, null);
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
                Log.d(TAG, "onClosing: ");
            }

            @Override
            public void onClosed(@NotNull WebSocket webSocket, int code, @NotNull String reason) {
                super.onClosed(webSocket, code, reason);
                Log.d(TAG, "onClosed: ");
            }

            public void sendMessageToMainUI(int what, Object obj){
                Message msg = Message.obtain();
                msg.arg1 = mIndex;
                msg.what = what;
                msg.obj = obj;
                mainUiHandler.sendMessage(msg);
            }
        }
    }

}
