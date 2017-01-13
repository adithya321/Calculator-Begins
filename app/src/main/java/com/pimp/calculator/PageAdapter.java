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

package com.pimp.calculator;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.pimp.calculator.FloatingView.CalculatorViewPager;

import java.util.Iterator;
import java.util.List;

public class PageAdapter extends CalculatorPageAdapter {
    private final Context mContext;
    private final Logic mLogic;
    private final EventListener mListener;
    private final Graph mGraph;
    private final List<Page> mPages;

    public PageAdapter(Context context, EventListener listener, Graph graph, Logic logic) {
        mContext = context;
        mGraph = graph;
        mLogic = logic;
        mListener = listener;
        mPages = Page.getPages(mContext);
    }

    protected Context getContext() {
        return mContext;
    }

    @Override
    public int getCount() {
        return CalculatorSettings.useInfiniteScrolling(mContext) ? CalculatorViewPager.MAX_SIZE_CONSTANT * mPages.size() : mPages.size();
    }

    @Override
    public View getViewAt(int position) {
        position = position % mPages.size();
        View v = mPages.get(position).getView(mContext, mListener, mGraph, mLogic);
        if (v.getParent() != null) {
            ((ViewGroup) v.getParent()).removeView(v);
        }
        applyBannedResourcesByPage(v, mLogic.getBaseModule().getBase());

        return v;
    }

    @Override
    public Iterable<View> getViewIterator() {
        return new CalculatorIterator(this);
    }

    @Override
    public List<Page> getPages() {
        return mPages;
    }

    private static class CalculatorIterator implements Iterator<View>, Iterable<View> {
        int mCurrentPosition = 0;
        List<Page> mPages;
        Context mContext;

        CalculatorIterator(PageAdapter adapter) {
            super();
            mPages = adapter.mPages;
            mContext = adapter.getContext();
        }

        @Override
        public boolean hasNext() {
            return mCurrentPosition < mPages.size();
        }

        @Override
        public View next() {
            View v = mPages.get(mCurrentPosition).getView(mContext);
            mCurrentPosition++;
            return v;
        }

        @Override
        public void remove() {
        }

        @Override
        public Iterator<View> iterator() {
            return this;
        }
    }
}
