/*
 * Calculator Begins
 * Copyright (C) 2016  Adithya J
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

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