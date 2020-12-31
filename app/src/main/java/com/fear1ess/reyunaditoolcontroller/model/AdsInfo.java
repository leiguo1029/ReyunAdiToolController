package com.fear1ess.reyunaditoolcontroller.model;

import com.fear1ess.reyunaditoolcontroller.AdsSdkExistsFlag;

import org.json.JSONException;
import org.json.JSONObject;

public class AdsInfo {
    private String mAdsName;
    private int mAdsSdkFindingState;
    private int mAdsDataFindingState;

    public String getAdsName() {
        return mAdsName;
    }

    public int getAdsSdkFindingState() {
        return mAdsSdkFindingState;
    }

    public int getAdsDataFindingState() {
        return mAdsDataFindingState;
    }

    public void setAdsName(String AdsName) {
        this.mAdsName = AdsName;
    }

    public void setAdsSdkFindingState(int AdsSdkFindingState) {
        this.mAdsSdkFindingState = AdsSdkFindingState;
    }

    public void setAdsDataFindingState(int AdsDataFindingState) {
        this.mAdsDataFindingState = AdsDataFindingState;
    }

    public AdsInfo(String adsName, int sdkState, int dataState){
        mAdsName = adsName;
        mAdsSdkFindingState = sdkState;
        mAdsDataFindingState = dataState;
    }

    public class AdsFindingState{
        public final static int FINDING = 1;
        public final static int FOUND = 2;
        public final static int NOT_FOUND = 3;
    }
}
