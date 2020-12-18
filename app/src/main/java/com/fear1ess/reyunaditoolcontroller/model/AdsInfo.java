package com.fear1ess.reyunaditoolcontroller.model;

public class AdsInfo {
    private String mAdsName;
    private int mAdsSdkFindingState;
    private int mAdsDataFindingState;

    public String getmAdsName() {
        return mAdsName;
    }

    public int getmAdsSdkFindingState() {
        return mAdsSdkFindingState;
    }

    public int getmAdsDataFindingState() {
        return mAdsDataFindingState;
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
