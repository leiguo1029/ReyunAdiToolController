package com.fear1ess.reyunaditoolcontroller.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Message;
import android.util.Base64;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.fear1ess.reyunaditoolcontroller.AdiToolControllerApp;
import com.fear1ess.reyunaditoolcontroller.AdsSdkExistsFlag;
import com.fear1ess.reyunaditoolcontroller.MainUIHandler;
import com.fear1ess.reyunaditoolcontroller.NewMsgCallBack;
import com.fear1ess.reyunaditoolcontroller.R;
import com.fear1ess.reyunaditoolcontroller.WSConnectManager;
import com.fear1ess.reyunaditoolcontroller.adapter.AdsInfoAdapter;
import com.fear1ess.reyunaditoolcontroller.cmd.WSConnectCmd.ClientCmd;
import com.fear1ess.reyunaditoolcontroller.model.AdsInfo;
import com.fear1ess.reyunaditoolcontroller.model.AdsInfo.AdsFindingState;
import com.fear1ess.reyunaditoolcontroller.model.AppInfo;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class AppDetailActivity extends AppCompatActivity implements NewMsgCallBack {
    private AdsInfoAdapter mAdapter;
    private AdiToolControllerApp mApp;


    public void createAppInfoView(String appInfo) {
        try {
            JSONObject jo = new JSONObject(appInfo);
            String packageName = jo.getString("package_name");
            String appName = jo.getString("app_name");
            String iconB64 = jo.getString("icon");
            byte[] iconBuffer = Base64.decode(iconB64,Base64.NO_WRAP);
            Bitmap bm = BitmapFactory.decodeByteArray(iconBuffer,0,iconBuffer.length);
            TextView tv1 = findViewById(R.id.detail_package_name);
            tv1.setText(packageName);
            TextView tv2 = findViewById(R.id.detail_app_name);
            tv2.setText(appName);
            ImageView iv = findViewById(R.id.detail_app_icon);
            iv.setImageBitmap(bm);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_detail);

        mApp = (AdiToolControllerApp) getApplication();

        ActionBar actionBar = this.getSupportActionBar();
        actionBar.setTitle("任务详情");
        actionBar.setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        String appInfo =  intent.getStringExtra("appInfo");
        createAppInfoView(appInfo);

        RecyclerView rv = findViewById(R.id.detail_rv);
        List<AdsInfo> adsInfoList = new ArrayList<>();
        adsInfoList.add(new AdsInfo(null, 0, 0));

        adsInfoList.add(new AdsInfo("admob", AdsFindingState.FINDING, AdsFindingState.FINDING));
        adsInfoList.add(new AdsInfo("unity", AdsFindingState.FINDING, AdsFindingState.FINDING));
        adsInfoList.add(new AdsInfo("vungle", AdsFindingState.FINDING, AdsFindingState.FINDING));
        adsInfoList.add(new AdsInfo("facebook", AdsFindingState.FINDING, AdsFindingState.FINDING));
        mAdapter = new AdsInfoAdapter(adsInfoList);
        rv.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(mAdapter);

        // register callback
       // AdiToolControllerApp.getUiHandler().registerCallBack();

        //request newest ads state
      //  WSConnectManager.newInstance(0).doCommand(ClientCmd.REQ_APP_ADS_STATE);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
     //   AdiToolControllerApp.getUiHandler().unRegisterCallBack(this);
    }

    @Override
    public void onNewMessage(Message msg) {
        if(msg.what != MainUIHandler.NEW_ADS_PUSH_DATA) return;
        JSONObject jo = (JSONObject) msg.obj;
        if(!jo.has("package_name")) return;
        try {
            int sdkState = jo.getInt("ads_sdk_state");
            int dataState = jo.getInt("ads_data_state");

            mAdapter.updateRvItemState(sdkState, dataState);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}