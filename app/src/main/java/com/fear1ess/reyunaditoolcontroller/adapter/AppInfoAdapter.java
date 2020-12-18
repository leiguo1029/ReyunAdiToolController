package com.fear1ess.reyunaditoolcontroller.adapter;

import android.app.Application;
import android.util.Log;
import android.view.ContextMenu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fear1ess.reyunaditoolcontroller.R;
import com.fear1ess.reyunaditoolcontroller.model.AppInfo;
import com.fear1ess.reyunaditoolcontroller.state.AppState;

import java.util.ArrayList;
import java.util.List;

public class AppInfoAdapter extends RecyclerView.Adapter<AppInfoAdapter.MyViewHolder> {
    private List<AppInfo> mAppInfoList;
    private RecyclerView mRecyclerview;
    private OnRecyclerviewItemClickListener mListener;

    public AppInfoAdapter(RecyclerView rv) {
        mAppInfoList = new ArrayList<>();
        mAppInfoList.add(new AppInfo("apple",null,null,2));
        mAppInfoList.add(new AppInfo("banana",null,null,2));
        mAppInfoList.add(new AppInfo("cat",null,null,2));
        mRecyclerview = rv;
    }

    public void setOnRecyclerViewItemClickListener(OnRecyclerviewItemClickListener listener){
        mListener = listener;
    }

    public AppInfo getItem(int pos){
        return mAppInfoList.get(pos);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = View.inflate(parent.getContext(),R.layout.appinfo_item,null);
        v.setClickable(true);
        return new MyViewHolder(v, mListener);
    }

    public String getStateStr(int state){
        switch (state){
            case AppState.APP_DOWNLOADING:
                return "下载中";
            case AppState.APP_DOWNLOADED_AND_PARPARE_TO_INSTALL:
                return "等待安装";
            case AppState.APP_INSTALLING:
                return "安装中";
            case AppState.APP_INSTALLED_AND_OPEN:
                return "正在打开";
            case AppState.APP_REMOVING:
                return "正在移除";
            case AppState.APP_REMOVED:
                return "已移除";
            case AppState.APP_ADSDK_CHECKING:
                return "正在检测";
            case AppState.APP_INSTALL_FAILED:
                return "安装失败";
            default: return "???";
        }
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position, @NonNull List<Object> payloads) {
        Log.d("reyunaditoolcontroller", "onBindViewHolder: ");
        if(payloads.isEmpty()){
            onBindViewHolder(holder, position);
            return;
        }
        int updateVal = (int) payloads.get(0);
        int needUpdatePkgName = 1;
        int needUpdateAppName = 2;
        int needUpdateIcon = 4;
        int needUpdateState = 8;
        AppInfo appInfo = mAppInfoList.get(position);
        if((updateVal & needUpdatePkgName) != 0){
            holder.pkgName.setText(appInfo.getPackageName());
        }
        if((updateVal & needUpdateAppName) != 0){
            holder.appName.setText(appInfo.getAppName());
        }
        if((updateVal & needUpdateIcon) != 0){
            holder.iconImage.setImageDrawable(appInfo.getIcon());
        }
        if((updateVal & needUpdateState) != 0){
            holder.state.setText(getStateStr(appInfo.getState()));
        }

    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        AppInfo appInfo = mAppInfoList.get(position);
        holder.iconImage.setImageDrawable(appInfo.getIcon());
        holder.appName.setText(appInfo.getAppName());
        holder.pkgName.setText(appInfo.getPackageName());
        holder.state.setText(getStateStr(appInfo.getState()));
      //  Log.d("recyclerview_test", "onBindViewHolder: "+appInfo.getAppName());
    }

    @Override
    public int getItemCount()
    {
        return mAppInfoList.size();
    }

    public void update(AppInfo appInfo){
        for(AppInfo item : mAppInfoList){
            if(appInfo.getPackageName().equals(item.getPackageName())){
                int pos = mAppInfoList.indexOf(item);
                int state = appInfo.getState();
                if(state == AppState.APP_INSTALL_FAILED || state == AppState.APP_REMOVED){
                    remove(pos);
                }else{
                    notifyItemChanged(pos, item.updateAppInfo(appInfo));
                }
                return;
            }
        }
        mAppInfoList.add(appInfo);
        notifyItemInserted(mAppInfoList.size());
    }

    public void add(int pos, AppInfo appInfo){
        mAppInfoList.add(pos, appInfo);
        notifyItemInserted(pos);
        notifyItemRangeChanged(pos, mAppInfoList.size() - pos);
    }

    public void remove(int pos){
        mAppInfoList.remove(pos);
        notifyItemRemoved(pos);
        notifyItemRangeChanged(pos, mAppInfoList.size() - pos);
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        protected ImageView iconImage;
        protected TextView pkgName;
        protected TextView appName;
        protected TextView state;
        protected OnRecyclerviewItemClickListener mListener;

        public MyViewHolder(@NonNull final View itemView, OnRecyclerviewItemClickListener listener) {
            super(itemView);
            iconImage = itemView.findViewById(R.id.icon1);
            pkgName = itemView.findViewById(R.id.text1);
            appName = itemView.findViewById(R.id.text2);
            state = itemView.findViewById(R.id.appstate);
            itemView.setOnClickListener(this);
            mListener = listener;
        }

        @Override
        public void onClick(View view) {
            int pos = getAdapterPosition();
            mListener.onItemClick(view, pos);
        }
    }

    public static interface OnRecyclerviewItemClickListener{
        public void onItemClick(View view, int pos);
    }
}

