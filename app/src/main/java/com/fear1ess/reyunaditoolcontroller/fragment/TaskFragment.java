package com.fear1ess.reyunaditoolcontroller.fragment;

import android.app.Application;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Message;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.fear1ess.reyunaditoolcontroller.AdiToolControllerApp;
import com.fear1ess.reyunaditoolcontroller.MainUIHandler;
import com.fear1ess.reyunaditoolcontroller.NewMsgCallBack;
import com.fear1ess.reyunaditoolcontroller.R;
import com.fear1ess.reyunaditoolcontroller.adapter.TabViewPagerAdapter;
import com.fear1ess.reyunaditoolcontroller.model.AppInfo;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.List;

public abstract class TaskFragment extends Fragment {
    protected TabViewPagerAdapter mTabViewPagerAdapter;
    protected boolean mNeedRegister;
    protected ImageView[] mStateImgs = new ImageView[AdiToolControllerApp.getMaxDeviceNum()];

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
        TabLayout tabLayout = v.findViewById(R.id.devicetab);
        ViewPager2 viewPager = v.findViewById(R.id.deviceviewpager);
        mTabViewPagerAdapter = new TabViewPagerAdapter(getActivity(), fragmentList);
        viewPager.setAdapter(mTabViewPagerAdapter);

        new TabLayoutMediator(tabLayout, viewPager, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                View v = getLayoutInflater().inflate(R.layout.device_tab_item, tab.view, false);
                ImageView iv = v.findViewById(R.id.circle_img);
                mStateImgs[position] = iv;
                iv.setImageResource(R.drawable.red_circle);
                TextView tv = v.findViewById(R.id.device_name);
                tv.setText(tabNameList.get(position));
                tab.setCustomView(v);
            }
        }).attach();

        AdiToolControllerApp.getUiHandler().registerMsgCallBack("task_fragment", new NewMsgCallBack() {
            @Override
            public void onNewMessage(Message msg) {
             //   String serverAddress = (String) msg.obj;
             //   TabLayout.Tab tab = tabLayout.getTabAt(msg.arg1);
                if(msg.what == MainUIHandler.WEBSOCKET_CONNECT_SUCCESS) {
                    mStateImgs[msg.arg1].setImageResource(R.drawable.green_circle);
                }else if(msg.what == MainUIHandler.WEBSOCKET_CONNECT_FAILED) {
                    mStateImgs[msg.arg1].setImageResource(R.drawable.red_circle);
                }
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
