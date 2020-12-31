package com.fear1ess.reyunaditoolcontroller.fragment;

import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class HistoryTaskFragment extends TaskFragment {

    public HistoryTaskFragment(boolean needRegister) {
        super(needRegister);
    }

    public static TaskFragment newInstance() {
        TaskFragment fragment = new HistoryTaskFragment(false);
        return fragment;
    }

}
