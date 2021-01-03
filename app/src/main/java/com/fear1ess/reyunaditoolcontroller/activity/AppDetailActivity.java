package com.fear1ess.reyunaditoolcontroller.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import com.fear1ess.reyunaditoolcontroller.AdiToolControllerApp;
import com.fear1ess.reyunaditoolcontroller.R;
import com.fear1ess.reyunaditoolcontroller.adapter.AdsInfoAdapter;
import com.fear1ess.reyunaditoolcontroller.model.AdsInfo;
import com.fear1ess.reyunaditoolcontroller.model.AdsInfo.AdsFindingState;
import com.fear1ess.reyunaditoolcontroller.model.AppInfo;

import java.util.ArrayList;
import java.util.List;

public class AppDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_detail);

        ActionBar actionBar = this.getSupportActionBar();
        actionBar.setTitle("任务详情");
        actionBar.setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        AppInfo appInfo = (AppInfo) intent.getSerializableExtra("appInfo");
        TextView name = findViewById(R.id.text2);
        name.setText(appInfo.getPackageName());

        RecyclerView rv = findViewById(R.id.detail_rv);
        List<AdsInfo> adsInfoList = new ArrayList<>();
        adsInfoList.add(new AdsInfo("admob", AdsFindingState.FOUND, AdsFindingState.NOT_FOUND));
        adsInfoList.add(new AdsInfo("unity", AdsFindingState.NOT_FOUND, AdsFindingState.FOUND));
        AdsInfoAdapter adsInfoAdapter = new AdsInfoAdapter(adsInfoList);
        rv.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(adsInfoAdapter);

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}