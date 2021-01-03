package com.fear1ess.reyunaditoolcontroller;

import android.os.Handler;
import android.os.Message;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainUIHandler extends Handler {
    public final static int WEBSOCKET_CONNECT_SUCCESS = 1;
    public final static int NEW_APP_PUSH_DATA = 2;
    public final static int NEW_ADS_PUSH_DATA = 3;
    public final static int WEBSOCKET_CONNECT_FAILED = 4;

    private NewMsgCallBack[] pushMsgCallBacks = new NewMsgCallBack[AdiToolControllerApp.getMaxDeviceNum()];
    private Map<String, NewMsgCallBack> msgCallBacks = new HashMap<>();

    public void registerPushMsgCallBack(int index, NewMsgCallBack cb) {
        pushMsgCallBacks[index] = cb;
    }

    public void registerMsgCallBack(String name, NewMsgCallBack cb) {
        msgCallBacks.put(name, cb);
    }

    public void unRegisterMsgCallBack(String name) {
        msgCallBacks.remove(name);
    }
    public void unRegisterPushMsgCallBack(int index) {
        pushMsgCallBacks[index] = null;
    }

    public void dispatchMsg(NewMsgCallBack cb, Message msg) {
        if(cb != null) {
            cb.onNewMessage(msg);
        }
    }

    @Override
    public void handleMessage(@NonNull Message msg) {
        switch (msg.what) {
            case MainUIHandler.WEBSOCKET_CONNECT_SUCCESS:
            case MainUIHandler.WEBSOCKET_CONNECT_FAILED:
                dispatchMsg(msgCallBacks.get("task_fragment"), msg);
                break;

            case MainUIHandler.NEW_APP_PUSH_DATA:
                dispatchMsg(pushMsgCallBacks[msg.arg1], msg);
                break;
            default: break;

        }
    }
}
