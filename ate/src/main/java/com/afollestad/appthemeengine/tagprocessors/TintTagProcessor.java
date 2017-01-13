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

package com.afollestad.appthemeengine.tagprocessors;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.SwitchCompat;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Switch;

import com.afollestad.appthemeengine.util.TextInputLayoutUtil;
import com.afollestad.appthemeengine.util.TintHelper;

/**
 * @author Aidan Follestad (afollestad)
 */
public class TintTagProcessor extends TagProcessor {

    public static final String PREFIX = "tint";
    public static final String BACKGROUND_PREFIX = "tint_background";
    public static final String SELECTOR_PREFIX_LIGHT = "tint_selector_lighter";
    public static final String SELECTOR_PREFIX = "tint_selector";

    private final boolean mBackgroundMode;
    private final boolean mSelectorMode;
    private final boolean mLightSelector;

    public TintTagProcessor(boolean backgroundMode, boolean selectorMode, boolean lighter) {
        mBackgroundMode = backgroundMode;
        mSelectorMode = selectorMode;
        mLightSelector = lighter;
    }

    @Override
    public boolean isTypeSupported(@NonNull View view) {
        return mBackgroundMode ||
                mSelectorMode ||
                view instanceof CheckBox ||
                view instanceof RadioButton ||
                view instanceof ProgressBar || // includes SeekBar
                view instanceof EditText ||
                view instanceof ImageView ||
                view instanceof Switch ||
                view instanceof SwitchCompat ||
                view instanceof CheckedTextView ||
                view instanceof Spinner;
    }

    @Override
    public void process(@NonNull Context context, @Nullable String key, @NonNull View view, @NonNull String suffix) {
        final ColorResult result = getColorFromSuffix(context, key, view, suffix);
        if (result == null) return;

        if (mSelectorMode) {
            TintHelper.setTintSelector(view, result.getColor(), !mLightSelector, result.isDark(context));
        } else {
            TintHelper.setTintAuto(view, result.getColor(), mBackgroundMode, result.isDark(context));
        }

        if (view instanceof EditText) {
            // Sets accent (expanded hint) color of parent TextInputLayouts
            if (view.getParent() != null && view.getParent() instanceof TextInputLayout) {
                final TextInputLayout til = (TextInputLayout) view.getParent();
                TextInputLayoutUtil.setAccent(til, result.getColor());
            }
        }
    }
}