package com.fear1ess.reyunaditoolcontroller.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.fear1ess.reyunaditoolcontroller.AdiToolControllerApp;
import com.fear1ess.reyunaditoolcontroller.MainUIHandler;
import com.fear1ess.reyunaditoolcontroller.NewMsgCallBack;
import com.fear1ess.reyunaditoolcontroller.R;
import com.fear1ess.reyunaditoolcontroller.SharedPreferenceUtils;
import com.fear1ess.reyunaditoolcontroller.WSConnectManager;
import com.fear1ess.reyunaditoolcontroller.adapter.TabViewPagerAdapter;
import com.fear1ess.reyunaditoolcontroller.cmd.WSConnectCmd.ClientCmd;
import com.fear1ess.reyunaditoolcontroller.fragment.CurTaskFragment;
import com.fear1ess.reyunaditoolcontroller.fragment.HistoryTaskFragment;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.List;

import okhttp3.WebSocket;

public class MainActivity extends AppCompatActivity {
    public WebSocket mWebSocket;
    public boolean isWSConnected = false;
    public AdiToolControllerApp mApp;

    private Handler mainUiHandler;
    private TabViewPagerAdapter mTabViewPagerAdapter;

    private String[] mTabNames = new String[]{"当前任务","历史任务"};

 //   public Button mStartBtn;
    public MenuItem mStartMenuItem;
    public MenuItem mStopMenuItem;

    public TabViewPagerAdapter getTabViewPagerAdapter(){
        return mTabViewPagerAdapter;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

         mApp = (AdiToolControllerApp) getApplication();

        ActionBar actionBar = this.getSupportActionBar();
        actionBar.setTitle("Happy New Year 2021");

        //create tab view
        ArrayList<String> tabNameList = new ArrayList<>();
        for(String name : mTabNames) tabNameList.add(name);
        TabLayout tabLayout = findViewById(R.id.maintab);
        ViewPager2 viewPager = findViewById(R.id.mainviewpager);
        //禁止viewpager滑动
        viewPager.setUserInputEnabled(false);
        List<Fragment> fragmentList = new ArrayList<>();
        fragmentList.add(CurTaskFragment.newInstance());
        fragmentList.add(HistoryTaskFragment.newInstance());
        mTabViewPagerAdapter = new TabViewPagerAdapter(this, fragmentList);
        viewPager.setAdapter(mTabViewPagerAdapter);
        new TabLayoutMediator(tabLayout, viewPager, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                tab.setText(tabNameList.get(position));
            }
        }).attach();
    }

    @Override
    protected void onStart() {
        super.onStart();
        startWSConnect();
    }

    public void startWSConnect() {
        for (int i = 0; i < AdiToolControllerApp.getMaxDeviceNum(); ++i) {
            String address = SharedPreferenceUtils.getServerConfig(i);
            if(address != null && !address.equals("")) {
                WSConnectManager.getInstance().newWSConnect(i, address);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        mStartMenuItem = menu.add(Menu.NONE, 1, 1, "启动任务");
        mStopMenuItem = menu.add(Menu.NONE, 2, 2, "结束任务");
        MenuItem mi = menu.add(Menu.NONE, 3, 3, "修改服务端配置");

        mStopMenuItem.setEnabled(false);

        mStartMenuItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
               // wcm.doCommand(ClientCmd.START_OPERATE_APP);
                return true;
            }
        });

        mi.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                Intent intent = new Intent(MainActivity.this, ServerConfigActivity.class);
                startActivityForResult(intent, 100);
                return true;
            }
        });
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 100) {
            startWSConnect();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}