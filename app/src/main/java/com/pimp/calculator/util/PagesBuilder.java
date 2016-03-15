package com.pimp.calculator.util;

import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

import java.util.ArrayList;
import java.util.Iterator;

public class PagesBuilder implements Iterable<PagesBuilder.Page> {

    private final ArrayList<Page> mPages;

    public PagesBuilder(int expectedSize) {
        mPages = new ArrayList<>(expectedSize);
    }

    public void add(@NonNull Page page) {
        mPages.add(page);
    }

    public Page get(int index) {
        return mPages.get(index);
    }

    public int size() {
        return mPages.size();
    }

    @Override
    public Iterator<Page> iterator() {
        return mPages.iterator();
    }

    public static class Page {

        public final Drawable iconRes;
        @NonNull
        public final Fragment fragment;

        public Page(Drawable iconRes, @NonNull Fragment fragment) {
            this.iconRes = iconRes;
            this.fragment = fragment;
        }
    }
}