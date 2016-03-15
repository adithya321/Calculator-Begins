package com.pimp.calculator.adapters;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.pimp.calculator.util.PagesBuilder;


public class MainPagerAdapter extends FragmentStatePagerAdapter {

    private final PagesBuilder mPages;

    public MainPagerAdapter(FragmentManager fm, PagesBuilder pages) {
        super(fm);
        mPages = pages;
    }

    @Override
    public Fragment getItem(int position) {
        return mPages.get(position).fragment;
    }

    @Override
    public int getCount() {
        return mPages.size();
    }
}