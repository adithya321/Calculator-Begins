/*
 * Calculator Begins
 * Copyright (C) 2017  Adithya J
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

package com.pimp.calculator.FloatingView;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.pimp.calculator.CalculatorPageAdapter;
import com.pimp.calculator.CalculatorSettings;
import com.pimp.calculator.Page;
import com.pimp.calculator.Page.NormalPanel;

import java.util.List;

public class CalculatorViewPager extends ViewPager {
    // Usually we use a huge constant, but ViewPager crashes when the size is too big.
    public static int MAX_SIZE_CONSTANT = 100;
    private boolean mIsEnabled;

    public CalculatorViewPager(Context context) {
        this(context, null);
    }

    public CalculatorViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        mIsEnabled = true;
    }

    /**
     * ViewPager inherits ViewGroup's default behavior of delayed clicks on its children, but in order to make the calc buttons more responsive we disable that here.
     */
    @Override
    public boolean shouldDelayChildPressedState() {
        return false;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        if (mIsEnabled) {
            return super.onInterceptTouchEvent(event);
        }

        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mIsEnabled) {
            return super.onTouchEvent(event);
        }

        return false;
    }

    public boolean getPagingEnabled() {
        return mIsEnabled;
    }

    public void setPagingEnabled(boolean enabled) {
        mIsEnabled = enabled;
    }

    public void scrollToMiddle() {
        if (CalculatorSettings.useInfiniteScrolling(getContext())) {
            List<Page> pages = ((CalculatorPageAdapter) getAdapter()).getPages();
            if (pages.size() != 0) {
                int halfwayDownTheInfiniteList = (MAX_SIZE_CONSTANT / pages.size()) / 2 * pages.size() + Page.getOrder(pages, new Page(getContext(), NormalPanel.BASIC));
                setCurrentItem(halfwayDownTheInfiniteList);
            }
        }
    }
}
