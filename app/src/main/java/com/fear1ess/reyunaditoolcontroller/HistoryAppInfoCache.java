package com.fear1ess.reyunaditoolcontroller;

import com.fear1ess.reyunaditoolcontroller.model.AppInfo;

import java.util.List;

public class HistoryAppInfoCache {
    private List<AppInfo> mCacheAppInfoList;
    private static HistoryAppInfoCache mCache = new HistoryAppInfoCache();

    public static HistoryAppInfoCache getInstance(){
        return mCache;
    }

    public void add(AppInfo appInfo) {
        mCacheAppInfoList.add(appInfo);
    }

}
