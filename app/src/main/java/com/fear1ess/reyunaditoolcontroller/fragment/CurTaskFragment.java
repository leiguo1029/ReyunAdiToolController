package com.fear1ess.reyunaditoolcontroller.fragment;

import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.fear1ess.reyunaditoolcontroller.MainUIHandler;
import com.fear1ess.reyunaditoolcontroller.NewMsgCallBack;
import com.fear1ess.reyunaditoolcontroller.R;
import com.fear1ess.reyunaditoolcontroller.model.AppInfo;

import org.json.JSONObject;

public class CurTaskFragment extends TaskFragment {

    public CurTaskFragment(boolean needRegister) {
        super(needRegister);
    }

    public static TaskFragment newInstance() {
        TaskFragment fragment = new CurTaskFragment(true);
        return fragment;
    }
}
