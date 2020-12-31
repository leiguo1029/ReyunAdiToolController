package com.fear1ess.reyunaditoolcontroller.adapter;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fear1ess.reyunaditoolcontroller.AdiToolControllerApp;
import com.fear1ess.reyunaditoolcontroller.AdsSdkExistsFlag;
import com.fear1ess.reyunaditoolcontroller.R;
import com.fear1ess.reyunaditoolcontroller.activity.AppDetailActivity;
import com.fear1ess.reyunaditoolcontroller.model.AdsInfo;
import com.fear1ess.reyunaditoolcontroller.model.AdsInfo.AdsFindingState;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

public class AdsInfoAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<AdsInfo> mAdsInfoList;
    private static Bitmap positiveIcon;
    private static Bitmap negativeIcon;
    private static Bitmap doingIcon;
    private int mAdsSdkState = 0;
    private int mAdsDataState = 0;

    public static final int TYPE_HEADER = 0;
    public static final int TYPE_FOOTER = 1;
    public static final int TYPE_NORMAL = 2;

    private static List<AdsRvItemInfo> mRvItemInfo = new ArrayList<>();

    static{
        mRvItemInfo.add(new AdsRvItemInfo("admob", 1, AdsSdkExistsFlag.ADMOB));
        mRvItemInfo.add(new AdsRvItemInfo("unity", 2, AdsSdkExistsFlag.UNITY));
        mRvItemInfo.add(new AdsRvItemInfo("vungle", 3, AdsSdkExistsFlag.VUNGLE));
        mRvItemInfo.add(new AdsRvItemInfo("facebook", 4, AdsSdkExistsFlag.FACEBOOK));

        AssetManager am = AdiToolControllerApp.getAppContext().getAssets();
        try {
            InputStream is1 = am.open("true.jpg");
            InputStream is2 = am.open("false.jpg");
            InputStream is3 = am.open("doing.png");
            positiveIcon = BitmapFactory.decodeStream(is1);
            negativeIcon = BitmapFactory.decodeStream(is2);
            doingIcon = BitmapFactory.decodeStream(is3);
            is1.close();
            is2.close();
            is3.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public AdsInfoAdapter(List<AdsInfo> list){
        mAdsInfoList = list;
    }

    @Override
    public int getItemViewType(int position) {
        if(position == 0) return TYPE_HEADER;
        return TYPE_NORMAL;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View v = null;
        if(viewType == TYPE_HEADER){
            v = layoutInflater.inflate(R.layout.ads_rv_header, parent, false);
            return new HeaderViewHolder(v);
        }else if(viewType == TYPE_NORMAL){
            v = layoutInflater.inflate(R.layout.ads_rv_item, parent, false);
            return new ItemViewHolder(v);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position, @NonNull List<Object> payloads) {
        if(payloads.isEmpty()){
            onBindViewHolder(holder, position);
            return;
        }
        if(!(holder instanceof ItemViewHolder)) return;
        int updateState = (int) payloads.get(0);
        AdsInfo adsInfo = mAdsInfoList.get(position);
        ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
        if((updateState & 1) != 0)
            setStateImageView(itemViewHolder.mAdsSdkStateImageView, adsInfo.getAdsSdkFindingState());
        if((updateState & 2) != 0)
            setStateImageView(itemViewHolder.mAdsDataStateImageView, adsInfo.getAdsDataFindingState());
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof HeaderViewHolder){
            HeaderViewHolder headerViewHolder = (HeaderViewHolder) holder;
            headerViewHolder.mAdsNameTextHeaderView.setText("Ads");
            headerViewHolder.mAdsSdkStateTextHeaderView.setText("sdk");
            headerViewHolder.mAdsDataStateTextHeaderView.setText("data");
        }else if(holder instanceof ItemViewHolder){
            AdsInfo adsInfo = mAdsInfoList.get(position);
            ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
            itemViewHolder.mAdsNameTextView.setText(adsInfo.getAdsName());
            setStateImageView(itemViewHolder.mAdsSdkStateImageView, adsInfo.getAdsSdkFindingState());
            setStateImageView(itemViewHolder.mAdsDataStateImageView, adsInfo.getAdsDataFindingState());
        }
    }

    public void setStateImageView(ImageView v, int state){
        switch(state){
            case AdsFindingState.FOUND:
                v.clearAnimation();
                v.setImageBitmap(positiveIcon);
                break;
            case AdsFindingState.NOT_FOUND:
                v.clearAnimation();
                v.setImageBitmap(negativeIcon);
                break;
            case AdsFindingState.FINDING:
                v.setImageBitmap(doingIcon);
                Animation anim = AnimationUtils.loadAnimation(AdiToolControllerApp.getAppContext(), R.anim.doing_rotate);
                //变速器必须用代码实现，xml里设置无效
                LinearInterpolator lir = new LinearInterpolator();
                anim.setInterpolator(lir);
                v.startAnimation(anim);
                break;
            default: break;
        }
    }

    public void updateRvItemState(int newSdkState, int newDataState){
        if(newSdkState == mAdsSdkState && newDataState == mAdsDataState) return;
        int val1 = newSdkState ^ mAdsSdkState;
        int val2 = newDataState ^ mAdsDataState;

        for(AdsRvItemInfo item : mRvItemInfo) {
            int state1 = ((val1 & item.flag) != 0) ? 1 : 0;
            int state2 = ((val2 & item.flag) != 0) ? 2 : 0;
            int state = state1 | state2;
            if(state == 0) continue;
            AdsInfo ai = mAdsInfoList.get(item.pos);
            if(state1 != 0) {
                ai.setAdsSdkFindingState(AdsFindingState.FOUND);
            }
            if(state2 != 0) {
                ai.setAdsDataFindingState(AdsFindingState.FOUND);
            }
            mAdsInfoList.set(item.pos, ai);
            notifyItemChanged(item.pos, state);
        }
    }


    @Override
    public int getItemCount() {
        return mAdsInfoList.size();
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder{
        protected TextView mAdsNameTextView;
        protected ImageView mAdsSdkStateImageView;
        protected ImageView mAdsDataStateImageView;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            mAdsNameTextView = itemView.findViewById(R.id.ads_name);
            mAdsSdkStateImageView = itemView.findViewById(R.id.ads_sdk_state);
            mAdsDataStateImageView = itemView.findViewById(R.id.ads_data_state);
        }
    }

    public static class HeaderViewHolder extends RecyclerView.ViewHolder{
        protected TextView mAdsNameTextHeaderView;
        protected TextView mAdsSdkStateTextHeaderView;
        protected TextView mAdsDataStateTextHeaderView;
        public HeaderViewHolder(@NonNull View itemView) {
            super(itemView);
            mAdsNameTextHeaderView = itemView.findViewById(R.id.header_ads_name);
            mAdsSdkStateTextHeaderView = itemView.findViewById(R.id.header_ads_sdk_state);
            mAdsDataStateTextHeaderView = itemView.findViewById(R.id.header_ads_data_state);
        }
    }

    public static class AdsRvItemInfo {
        String adsName;
        int pos;
        int flag;

        public AdsRvItemInfo(String name, int p, int f) {
            adsName = name;
            pos = p;
            flag = f;
        }
    }
}
