package com.fear1ess.reyunaditoolcontroller.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fear1ess.reyunaditoolcontroller.AdiToolControllerApp;
import com.fear1ess.reyunaditoolcontroller.R;
import com.fear1ess.reyunaditoolcontroller.SharedPreferenceUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

public class ServerConfigActivity extends AppCompatActivity {

    private List<EditText> mEditTexts = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewGroup v = (ViewGroup) getLayoutInflater().inflate(R.layout.activity_server_config, null, false);

        //create text view and edit text
        int deviceNum = AdiToolControllerApp.getMaxDeviceNum();

        for(int i = 0; i < deviceNum; ++i) {
            View itemView = getLayoutInflater().inflate(R.layout.config_item, v, false);
            TextView tv = itemView.findViewById(R.id.config_tv);
            EditText et = itemView.findViewById(R.id.config_et);
            et.setText(SharedPreferenceUtils.getServerConfig(i));
            mEditTexts.add(et);
            tv.setText("设备" + (i + 1));
            v.addView(itemView);
        }

        Button btn = new Button(this);
        btn.setText("确定");
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for(int i = 0; i < mEditTexts.size(); ++i) {
                    String value = mEditTexts.get(i).getText().toString();
                    if(value == "") value = null;
                    SharedPreferenceUtils.setServerConfig(i, value);
                }
                startActivity(new Intent(ServerConfigActivity.this, MainActivity.class));
                finish();
            }
        });

        btn.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT));

        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        linearLayout.setLayoutDirection(ViewGroup.LAYOUT_DIRECTION_RTL);
        linearLayout.addView(btn);

        v.addView(linearLayout);
        setContentView(v);
    }
}