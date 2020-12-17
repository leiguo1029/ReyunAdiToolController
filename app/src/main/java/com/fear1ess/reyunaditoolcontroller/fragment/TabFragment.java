package com.fear1ess.reyunaditoolcontroller.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fear1ess.reyunaditoolcontroller.AdiToolControllerApp;
import com.fear1ess.reyunaditoolcontroller.R;
import com.fear1ess.reyunaditoolcontroller.adapter.AppInfoAdapter;

public class TabFragment extends Fragment {
    private AppInfoAdapter mAdapter;
    public static TabFragment newInstance(String label) {
        Bundle args = new Bundle();
       // args.putString("text", label);
       // args.putString("label", label);
        TabFragment fragment = new TabFragment();
       // fragment.setArguments(args);
        return fragment;
    }

    public AppInfoAdapter getRecycleViewAdapter(){
        return mAdapter;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Context context = AdiToolControllerApp.getAppContext();
        View v =  inflater.inflate(R.layout.tab_recyclerview, container, false);
        RecyclerView rv = v.findViewById(R.id.recycler_view);
        rv.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.VERTICAL));
        rv.setLayoutManager(new LinearLayoutManager(context));
        mAdapter = new AppInfoAdapter(rv);
        mAdapter.setOnRecyclerViewItemClickListener(new AppInfoAdapter.OnRecyclerviewItemClickListener() {
            @Override
            public void onItemClick(View view, int pos) {
                //TODO....
            }
        });
        rv.setAdapter(mAdapter);
        return v;
    }

}
