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
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.afollestad.appthemeengine.util.ATEUtil;
import com.afollestad.appthemeengine.util.TextInputLayoutUtil;

/**
 * @author Aidan Follestad (afollestad)
 */
public class TextColorTagProcessor extends TagProcessor {

    public static final String PREFIX = "text_color";
    public static final String LINK_PREFIX = "text_color_link";
    public static final String HINT_PREFIX = "text_color_hint";

    private final boolean mLinkMode;
    private final boolean mHintMode;

    public TextColorTagProcessor(boolean links, boolean hints) {
        mLinkMode = links;
        mHintMode = hints;
    }

    // TODO is dependent parameter needed?
    private static ColorStateList getTextSelector(@ColorInt int color, View view, boolean dependent) {
        if (dependent)
            color = ATEUtil.isColorLight(color) ? Color.BLACK : Color.WHITE;
        return new ColorStateList(new int[][]{
                new int[]{-android.R.attr.state_enabled},
                new int[]{android.R.attr.state_enabled}
        }, new int[]{
                // Buttons are gray when disabled, so the text needs to be black
                view instanceof Button ? Color.BLACK : ATEUtil.adjustAlpha(color, 0.3f),
                color
        });
    }

    @Override
    public boolean isTypeSupported(@NonNull View view) {
        return view instanceof TextView;
    }

    @Override
    public void process(@NonNull Context context, @Nullable String key, @NonNull View view, @NonNull String suffix) {
        final TextView tv = (TextView) view;
        final ColorResult result = getColorFromSuffix(context, key, view, suffix);
        if (result == null) return;

        if (mHintMode)
            result.adjustAlpha(0.5f);

        final ColorStateList sl = getTextSelector(result.getColor(), view, false);
        if (mLinkMode) {
            tv.setLinkTextColor(sl);
        } else if (mHintMode) {
            tv.setHintTextColor(sl);
            // Sets parent TextInputLayout hint color
            if (view.getParent() != null && view.getParent() instanceof TextInputLayout) {
                final TextInputLayout til = (TextInputLayout) view.getParent();
                TextInputLayoutUtil.setHint(til, result.getColor());
            }
        } else {
            tv.setTextColor(sl);
        }
    }
}