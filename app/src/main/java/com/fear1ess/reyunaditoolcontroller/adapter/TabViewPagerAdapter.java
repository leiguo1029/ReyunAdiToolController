package com.fear1ess.reyunaditoolcontroller.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.fear1ess.reyunaditoolcontroller.fragment.TabFragment;

import java.util.ArrayList;
import java.util.List;

public class TabViewPagerAdapter extends FragmentStateAdapter {
    private List<Fragment> mFragmentList = new ArrayList<>();
    private List<String> mTabNameList = new ArrayList<>();

    public TabViewPagerAdapter(FragmentActivity fa, List<Fragment> fragmentList) {
      //  super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        super(fa);
        mFragmentList = fragmentList;
    }


    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return mFragmentList.get(position);
    }

    @Override
    public int getItemCount() {
        return mFragmentList.size();
    }
}
