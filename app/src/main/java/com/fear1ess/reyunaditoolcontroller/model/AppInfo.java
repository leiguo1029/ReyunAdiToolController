package com.fear1ess.reyunaditoolcontroller.model;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Base64;

import com.fear1ess.reyunaditoolcontroller.AdiToolControllerApp;
import com.fear1ess.reyunaditoolcontroller.state.AppState;

import java.io.Serializable;

public class AppInfo implements Serializable {
    public AppInfo(String pkgName, String app_name, Drawable pkgIcon, int app_state){
        icon=pkgIcon;
        appName=app_name;
        if(appName == null) appName = "未知APP";
        packageName=pkgName;
        state = app_state;
    }
    private Drawable icon;
    private String packageName;
    private String appName;
    private int state;

    public Drawable getIcon(){return icon;}
    public String getPackageName(){return packageName;}
    public String getAppName(){return appName;}
    public int getState(){ return state; }

    public static AppInfo parseFromJson(JSONObject jo){
        try {
            if(!jo.has("package_name") || !(jo.has("state"))) return null;
            String pkgName = jo.getString("package_name");
            int state = jo.getInt("state");
            String appName = null;
            Drawable iconDrawable = null;
            if(jo.has("app_name")){
                appName = jo.getString("app_name");
            }
            if(jo.has("icon")){
                String iconB64 = jo.getString("icon");
                byte[] iconBuffer = Base64.decode(iconB64,Base64.NO_WRAP);
                Bitmap bm = BitmapFactory.decodeByteArray(iconBuffer,0,iconBuffer.length);
                Resources resources = AdiToolControllerApp.getAppContext().getResources();
                iconDrawable = new BitmapDrawable(resources, bm);
            }
            return new AppInfo(pkgName, appName, iconDrawable, state);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public int updateAppInfo(AppInfo appInfo) {
        int needUpdatePkgName = 1;
        int needUpdateAppName = 2;
        int needUpdateIcon = 4;
        int needUpdateState = 8;
        int updateVal = 0;
        
        switch(appInfo.state){
            case AppState.APP_DOWNLOADING:
                packageName = appInfo.packageName;
                state = appInfo.state;
                updateVal |= needUpdatePkgName;
                updateVal |= needUpdateState;
                break;
            case AppState.APP_DOWNLOADED_AND_PARPARE_TO_INSTALL:
                state = appInfo.state;
                updateVal |= needUpdateState;
                break;
            case AppState.APP_INSTALLING:
                state = appInfo.state;
                updateVal |= needUpdateState;
                break;
            case AppState.APP_INSTALLED_AND_OPEN:
                appName = appInfo.appName;
                icon = appInfo.icon;
                state = appInfo.state;
                updateVal |= needUpdateAppName;
                updateVal |= needUpdateIcon;
                updateVal |= needUpdateState;
                break;
            case AppState.APP_REMOVING:
                state = appInfo.state;
                updateVal |= needUpdateState;
                break;
            case AppState.APP_REMOVED:
                state = appInfo.state;
                updateVal |= needUpdateState;
                break;
            case AppState.APP_ADSDK_CHECKING:
                state = appInfo.state;
                updateVal |= needUpdateState;
                break;
            default: break;
        }
        return updateVal;
    }
}
