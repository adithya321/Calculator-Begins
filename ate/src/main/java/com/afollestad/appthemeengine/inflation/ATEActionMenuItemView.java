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

package com.afollestad.appthemeengine.inflation;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v7.view.menu.ActionMenuItemView;
import android.support.v7.view.menu.MenuItemImpl;
import android.util.AttributeSet;
import android.view.View;

import com.afollestad.appthemeengine.ATE;
import com.afollestad.appthemeengine.ATEActivity;
import com.afollestad.appthemeengine.Config;
import com.afollestad.appthemeengine.util.TintHelper;
import com.afollestad.appthemeengine.viewprocessors.ViewProcessor;

import java.lang.reflect.Field;

/**
 * @author Aidan Follestad (afollestad)
 */
class ATEActionMenuItemView extends ActionMenuItemView implements ViewInterface {

    private String mKey;
    private int mTintColor;
    private Drawable mIcon;
    private boolean mCheckedActionView;

    public ATEActionMenuItemView(Context context) {
        super(context);
        init(context, null);
    }

    public ATEActionMenuItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, null);
    }

    public ATEActionMenuItemView(Context context, AttributeSet attrs, @Nullable ATEActivity keyContext) {
        super(context, attrs);
        init(context, keyContext);
    }

    private void init(Context context, @Nullable ATEActivity keyContext) {
        if (keyContext == null && context instanceof ATEActivity)
            keyContext = (ATEActivity) context;
        mKey = null;
        if (keyContext != null)
            mKey = keyContext.getATEKey();

        if (mIcon != null)
            setIcon(mIcon); // invalidates initial icon tint
        else invalidateTintColor();

        ATE.themeView(context, this, mKey);
        setTextColor(mTintColor); // sets menu item text color
    }

    private void invalidateTintColor() {
        // TODO get a reference to toolbar instead of null here?
        final int mToolbarColor = Config.toolbarColor(getContext(), mKey, null);
        mTintColor = Config.getToolbarTitleColor(getContext(), null, mKey, mToolbarColor);
    }

    @Override
    public void setIcon(Drawable icon) {
        invalidateTintColor();
        mIcon = TintHelper.createTintedDrawable(icon, mTintColor);
        super.setIcon(mIcon);
        invalidateActionView();
    }

    @Override
    public void setTitle(CharSequence title) {
        super.setTitle(title);
        invalidateActionView();
    }

    @SuppressWarnings("unchecked")
    private void invalidateActionView() {
        if (mCheckedActionView) return;
        mCheckedActionView = true;
        View actionView = getActionView();
        if (actionView != null) {
            ViewProcessor processor = ATE.getViewProcessor(actionView.getClass());
            if (processor != null)
                processor.process(getContext(), mKey, actionView, null);
        }
    }

    @Nullable
    private View getActionView() {
        try {
            final Field itemData = getClass().getSuperclass().getDeclaredField("mItemData");
            itemData.setAccessible(true);
            final MenuItemImpl menuImpl = (MenuItemImpl) itemData.get(this);
            if (menuImpl == null) return null;
            return menuImpl.getActionView();
        } catch (Throwable t) {
            throw new RuntimeException("Failed to get ActionView from an ActionMenuItemView: " + t.getLocalizedMessage(), t);
        }
    }

    @Override
    public boolean setsStatusBarColor() {
        return false;
    }

    @Override
    public boolean setsToolbarColor() {
        return false;
    }
}