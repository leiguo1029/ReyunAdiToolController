package com.fear1ess.reyunaditoolcontroller;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;

import com.fear1ess.reyunaditoolcontroller.cmd.WSConnectCmd;
import com.fear1ess.reyunaditoolcontroller.thread.WSConnectThread;
import com.fear1ess.reyunaditoolcontroller.thread.WSSendDataProceduce;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.WebSocket;

public class MainActivity extends AppCompatActivity {
    public OkHttpClient mOkhttpClient;
    public WebSocket mWebSocket;
    public boolean isWSConnected = false;

    private Handler mainUiHandler;

    public Button mStartBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //create ui handler
        mainUiHandler = new MainUIHandler(this);

        if(mOkhttpClient == null){
            mOkhttpClient = new OkHttpClient.Builder()
                    .pingInterval(3, TimeUnit.SECONDS)//设置ping帧发送间隔
                    .build();
        }

        //connect to websocket server...
        WSConnect();

        mStartBtn = findViewById(R.id.start_service_btn);
        if(!isWSConnected) mStartBtn.setEnabled(false);
        mStartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JSONObject jo = new JSONObject();
                try {
                    jo.put("cmd", WSConnectCmd.START_OPERATE_APP);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                WSSendData(jo.toString());
            }
        });
    }

    public void setWebSocketConn(WebSocket ws){
        isWSConnected = true;
        mStartBtn.setEnabled(true);
        mWebSocket = ws;
    }

    public void WSConnect(){
        ExecutorService es = Executors.newSingleThreadExecutor();
        es.execute(new WSConnectThread(mainUiHandler, mOkhttpClient));
        es.shutdown();
    }

    public void WSSendData(String data){
        ExecutorService es = Executors.newSingleThreadExecutor();
        es.execute(new WSSendDataProceduce(mWebSocket, data));
        es.shutdown();
    }
}