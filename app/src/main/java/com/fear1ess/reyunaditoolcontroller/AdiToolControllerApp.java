package com.fear1ess.reyunaditoolcontroller;

import android.app.Application;
import android.content.Context;
import android.os.Handler;

public class AdiToolControllerApp extends Application {
    private static Context mAppContext = null;

    private Handler uiHandler = null;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        synchronized (this){
            if(mAppContext == null) mAppContext = base;
        }
    }

    public static Context getAppContext(){
        return mAppContext;
    }
}
