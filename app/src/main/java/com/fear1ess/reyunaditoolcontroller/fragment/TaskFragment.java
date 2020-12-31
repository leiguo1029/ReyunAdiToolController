package com.fear1ess.reyunaditoolcontroller.fragment;

import android.app.Application;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.fear1ess.reyunaditoolcontroller.AdiToolControllerApp;
import com.fear1ess.reyunaditoolcontroller.NewMsgCallBack;
import com.fear1ess.reyunaditoolcontroller.R;
import com.fear1ess.reyunaditoolcontroller.adapter.TabViewPagerAdapter;
import com.fear1ess.reyunaditoolcontroller.model.AppInfo;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.List;

public abstract class TaskFragment extends Fragment {
    protected String[] mTabNames = {"设备1", "设备2", "设备3", "设备4"};
    protected TabViewPagerAdapter mTabViewPagerAdapter;
    protected boolean mNeedRegister;

    public TaskFragment(boolean needRegister) {
        mNeedRegister = needRegister;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.device_tab, container, false);

        int deviceNum = AdiToolControllerApp.getMaxDeviceNum();

        //create tab view
        List<Fragment> fragmentList = new ArrayList<>();
        List<String> tabNameList = new ArrayList<>();
        for(int i = 0; i < deviceNum; ++i) {
            tabNameList.add("设备" + (i + 1));
            AppInfoTabFragment f = AppInfoTabFragment.newInstance(i, mNeedRegister);
            fragmentList.add(f);
        }
        for(String name : mTabNames) {
            tabNameList.add(name);
        }
        TabLayout tabLayout = v.findViewById(R.id.devicetab);
        ViewPager2 viewPager = v.findViewById(R.id.deviceviewpager);
        mTabViewPagerAdapter = new TabViewPagerAdapter(getActivity(), fragmentList);
        viewPager.setAdapter(mTabViewPagerAdapter);
        new TabLayoutMediator(tabLayout, viewPager, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                tab.setText(tabNameList.get(position));
            }
        }).attach();

        AdiToolControllerApp.getUiHandler().registerMsgCallBack("task_fragment", new NewMsgCallBack() {
            @Override
            public void onNewMessage(Message msg) {
                String serverAddress = (String) msg.obj;
                TabLayout.Tab tab = tabLayout.getTabAt(msg.arg1);
                String oriText = tab.getText().toString();
                String connectedStr = oriText += "(已连接)";
                tab.setText(connectedStr);
            }
        });

        return v;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        AdiToolControllerApp.getUiHandler().unRegisterMsgCallBack("task_fragment");
    }
}
