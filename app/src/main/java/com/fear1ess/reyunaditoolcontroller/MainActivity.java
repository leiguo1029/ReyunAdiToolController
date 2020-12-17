package com.fear1ess.reyunaditoolcontroller;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;

import com.fear1ess.reyunaditoolcontroller.adapter.AppInfoAdapter;
import com.fear1ess.reyunaditoolcontroller.adapter.TabViewPagerAdapter;
import com.fear1ess.reyunaditoolcontroller.cmd.WSConnectCmd.ClientCmd;
import com.fear1ess.reyunaditoolcontroller.fragment.TabFragment;
import com.fear1ess.reyunaditoolcontroller.model.AppInfo;
import com.fear1ess.reyunaditoolcontroller.thread.WSConnectThread;
import com.fear1ess.reyunaditoolcontroller.thread.WSSendDataProceduce;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.WebSocket;

public class MainActivity extends AppCompatActivity {
    public OkHttpClient mOkhttpClient;
    public WebSocket mWebSocket;
    public boolean isWSConnected = false;

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

        //create tab view
        ArrayList<String> tabNameList = new ArrayList<>();
        for(String name : mTabNames) tabNameList.add(name);
        TabLayout tabLayout = findViewById(R.id.maintab);
        ViewPager2 viewPager = findViewById(R.id.mainviewpager);
        List<Fragment> fragmentList = new ArrayList<>();
        fragmentList.add(TabFragment.newInstance("1"));
        fragmentList.add(TabFragment.newInstance("2"));
        mTabViewPagerAdapter = new TabViewPagerAdapter(this, fragmentList);
        viewPager.setAdapter(mTabViewPagerAdapter);
        new TabLayoutMediator(tabLayout, viewPager, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                tab.setText(tabNameList.get(position));
            }
        }).attach();

        //create ui handler
        mainUiHandler = new MainUIHandler(this);

        //connect to websocket server...
        if(mOkhttpClient == null){
            mOkhttpClient = new OkHttpClient.Builder()
                    .pingInterval(3, TimeUnit.SECONDS)//设置ping帧发送间隔
                    .build();
        }
        WSConnect();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        mStartMenuItem = menu.add(Menu.NONE, 1, 1, "启动任务");
        mStopMenuItem = menu.add(Menu.NONE, 2, 2, "结束任务");
        mStartMenuItem.setEnabled(false);
        mStopMenuItem.setEnabled(false);
        mStartMenuItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                JSONObject jo = new JSONObject();
                try {
                    jo.put("cmd", ClientCmd.START_OPERATE_APP);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                WSSendData(jo.toString());
                return true;
            }
        });
        return true;
    }

    public void setWebSocketConn(WebSocket ws){
        isWSConnected = true;
     //   mStartBtn.setEnabled(true);
        mStartMenuItem.setEnabled(true);
        mWebSocket = ws;
    }

    public void WSConnect(){
        ExecutorService es = Executors.newSingleThreadExecutor();
        es.execute(new WSConnectThread(mainUiHandler, mOkhttpClient));
        es.shutdown();
    }

    public void WSSendData(String data){
        ExecutorService es = Executors.newSingleThreadExecutor();
        es.execute(new WSSendDataProceduce(mWebSocket, data));
        es.shutdown();
    }
}