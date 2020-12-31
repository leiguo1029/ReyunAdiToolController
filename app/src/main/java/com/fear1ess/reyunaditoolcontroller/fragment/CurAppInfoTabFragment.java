package com.fear1ess.reyunaditoolcontroller.fragment;

import android.os.Bundle;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.fear1ess.reyunaditoolcontroller.AdiToolControllerApp;
import com.fear1ess.reyunaditoolcontroller.MainUIHandler;
import com.fear1ess.reyunaditoolcontroller.NewMsgCallBack;
import com.fear1ess.reyunaditoolcontroller.model.AppInfo;

import org.json.JSONObject;

public class CurAppInfoTabFragment extends AppInfoTabFragment {
    protected NewMsgCallBack mCallBack = new NewMsgCallBack() {
        @Override
        public void onNewMessage(Message msg) {
            if(msg.what != MainUIHandler.NEW_APP_PUSH_DATA || msg.arg1 != mIndex) return;
            if(msg.arg1 != mIndex) return;
            AppInfo appInfo = AppInfo.parseFromJson((JSONObject) msg.obj);
            updateRv(appInfo);
        }
    };

    public CurAppInfoTabFragment(int index) {
        super(index);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = super.onCreateView(inflater, container, savedInstanceState);
        registerToUICallback(mCallBack);
        return v;
    }

    public void registerToUICallback(NewMsgCallBack cb) {
        AdiToolControllerApp.getUiHandler().registerPushMsgCallBack(mIndex, cb);
    }

    public void unRegisterToUICallback(NewMsgCallBack cb) {
        AdiToolControllerApp.getUiHandler().unRegisterPushMsgCallBack(mIndex);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unRegisterToUICallback(mCallBack);
    }
}
