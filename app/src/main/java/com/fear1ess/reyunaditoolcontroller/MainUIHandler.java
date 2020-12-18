package com.fear1ess.reyunaditoolcontroller;

import android.os.Handler;
import android.os.Message;

import androidx.annotation.NonNull;

import com.fear1ess.reyunaditoolcontroller.activity.MainActivity;
import com.fear1ess.reyunaditoolcontroller.adapter.AppInfoAdapter;
import com.fear1ess.reyunaditoolcontroller.fragment.TabFragment;
import com.fear1ess.reyunaditoolcontroller.model.AppInfo;

import org.json.JSONObject;

import java.lang.ref.WeakReference;

import okhttp3.WebSocket;

public class MainUIHandler extends Handler {
    public final static int WEBSOCKET_CONNECT_SUCCESS = 1;
    public final static int NEW_PUSH_DATA = 2;

    private WeakReference<MainActivity> mActivityRef;
    private AppInfoAdapter mCurAppInfoAdapter;
    private AppInfoAdapter mHistoryAppInfoAdapter;
    public MainUIHandler(MainActivity act){
        mActivityRef = new WeakReference<>(act);
    }

    @Override
    public void handleMessage(@NonNull Message msg) {
        MainActivity act = mActivityRef.get();
        switch (msg.what){
            case WEBSOCKET_CONNECT_SUCCESS:{
                act.setWebSocketConn((WebSocket) msg.obj);
                break;
            }
            case NEW_PUSH_DATA: {
                if (mCurAppInfoAdapter == null) {
                    mCurAppInfoAdapter = ((TabFragment) act.getTabViewPagerAdapter().createFragment(0)).getRecycleViewAdapter();
                }
                if (mHistoryAppInfoAdapter == null) {
                    mHistoryAppInfoAdapter = ((TabFragment) act.getTabViewPagerAdapter().createFragment(1)).getRecycleViewAdapter();
                }

                JSONObject jo = (JSONObject) msg.obj;
                AppInfo appInfo = AppInfo.parseFromJson(jo);
                mCurAppInfoAdapter.update(appInfo);
                break;
            }
            default: break;
        }
    }
}
