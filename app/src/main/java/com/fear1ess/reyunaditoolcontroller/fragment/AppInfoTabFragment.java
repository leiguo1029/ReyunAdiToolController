package com.fear1ess.reyunaditoolcontroller.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fear1ess.reyunaditoolcontroller.AdiToolControllerApp;
import com.fear1ess.reyunaditoolcontroller.MainUIHandler;
import com.fear1ess.reyunaditoolcontroller.NewMsgCallBack;
import com.fear1ess.reyunaditoolcontroller.R;
import com.fear1ess.reyunaditoolcontroller.adapter.AppInfoAdapter;
import com.fear1ess.reyunaditoolcontroller.model.AppInfo;

import org.json.JSONObject;

public class AppInfoTabFragment extends Fragment {
    protected AppInfoAdapter mAdapter;
    protected int mIndex;
    public static String detail_activity_name = "com.fear1ess.reyunaditoolcontroller.activity.AppDetailActivity";

    public AppInfoAdapter getRecycleViewAdapter(){
        return mAdapter;
    }

    public AppInfoTabFragment(int index) {
        mIndex = index;
    }

    public static AppInfoTabFragment newInstance(int index, boolean needRegister) {
        if(needRegister) return new CurAppInfoTabFragment(index);
        return new AppInfoTabFragment(index);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //create rv
        Context context = AdiToolControllerApp.getAppContext();
        View v =  inflater.inflate(R.layout.tab_recyclerview, container, false);
        RecyclerView rv = v.findViewById(R.id.recycler_view);
        rv.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.VERTICAL));
        rv.setLayoutManager(new LinearLayoutManager(context));
        mAdapter = new AppInfoAdapter(rv);
        mAdapter.setOnRecyclerViewItemClickListener(new AppInfoAdapter.OnRecyclerviewItemClickListener() {
            @Override
            public void onItemClick(View view, int pos) {
                Intent intent = new Intent();
                intent.putExtra("appInfo", AppInfo.parseToJsonString(mAdapter.getItem(pos)));
                intent.setClassName(AdiToolControllerApp.getAppContext(), detail_activity_name);
                startActivity(intent);
            }
        });
        rv.setAdapter(mAdapter);
        return v;
    }

    public void updateRv(AppInfo appInfo) {
        mAdapter.update(appInfo);
    }

    public void removeRv(int pos){
        mAdapter.remove(pos);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
