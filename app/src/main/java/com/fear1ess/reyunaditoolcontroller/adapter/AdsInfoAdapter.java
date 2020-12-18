package com.fear1ess.reyunaditoolcontroller.adapter;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fear1ess.reyunaditoolcontroller.AdiToolControllerApp;
import com.fear1ess.reyunaditoolcontroller.R;
import com.fear1ess.reyunaditoolcontroller.model.AdsInfo;
import com.fear1ess.reyunaditoolcontroller.model.AdsInfo.AdsFindingState;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.zip.Inflater;

public class AdsInfoAdapter extends RecyclerView.Adapter<AdsInfoAdapter.MyViewHolder> {
    private List<AdsInfo> mAdsInfoList;
    private static Bitmap positiveIcon;
    private static Bitmap negativeIcon;

    static{
        AssetManager am = AdiToolControllerApp.getAppContext().getAssets();
        try {
            InputStream is1 = am.open("true.jpg");
            InputStream is2 = am.open("false.jpg");
            positiveIcon = BitmapFactory.decodeStream(is1);
            negativeIcon = BitmapFactory.decodeStream(is2);
            is1.close();
            is2.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public AdsInfoAdapter(List<AdsInfo> list){
        mAdsInfoList = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = View.inflate(parent.getContext(), R.layout.ads_rv_item, null);
        return new MyViewHolder(v);
    }

    public void setStateImageView(ImageView v, int state){
        switch(state){
            case AdsFindingState.FOUND:
                v.setImageBitmap(positiveIcon);
                break;
            case AdsFindingState.NOT_FOUND:
                v.setImageBitmap(negativeIcon);
                break;
            default: break;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        AdsInfo adsInfo = mAdsInfoList.get(position);
        holder.mAdsNameTextView.setText(adsInfo.getmAdsName());
        setStateImageView(holder.mAdsSdkStateImageView, adsInfo.getmAdsSdkFindingState());
        setStateImageView(holder.mAdsDataStateImageView, adsInfo.getmAdsDataFindingState());
    }

    @Override
    public int getItemCount() {
        return mAdsInfoList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        protected TextView mAdsNameTextView;
        protected ImageView mAdsSdkStateImageView;
        protected ImageView mAdsDataStateImageView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            mAdsNameTextView = itemView.findViewById(R.id.ads_name);
            mAdsSdkStateImageView = itemView.findViewById(R.id.ads_sdk_state);
            mAdsDataStateImageView = itemView.findViewById(R.id.ads_data_state);
        }
    }
}
