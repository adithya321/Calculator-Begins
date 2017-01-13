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
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.view.View;

import com.afollestad.appthemeengine.util.ATEUtil;
import com.afollestad.appthemeengine.util.TintHelper;

/**
 * @author Aidan Follestad (afollestad)
 */
public class TabLayoutTagProcessor extends TagProcessor {

    public static final String MAIN_CLASS = "android.support.design.widget.TabLayout";

    public static final String TEXT_PREFIX = "tab_text";
    public static final String INDICATOR_PREFIX = "tab_indicator";

    private final static float UNFOCUSED_ALPHA = 0.5f;

    private final boolean mTextMode;
    private final boolean mIndicatorMode;

    public TabLayoutTagProcessor(boolean text, boolean indicator) {
        mTextMode = text;
        mIndicatorMode = indicator;
    }

    @Override
    public boolean isTypeSupported(@NonNull View view) {
        return view instanceof TabLayout;
    }

    @Override
    public void process(@NonNull Context context, @Nullable String key, @NonNull View view, @NonNull String suffix) {
        final TabLayout tl = (TabLayout) view;
        final ColorResult result = getColorFromSuffix(context, key, view, suffix);
        if (result == null) return;
        final int color = result.getColor();

        if (mTextMode) {
            tl.setTabTextColors(ATEUtil.adjustAlpha(color, UNFOCUSED_ALPHA), color);
        } else if (mIndicatorMode) {
            tl.setSelectedTabIndicatorColor(color);

            final ColorStateList sl = new ColorStateList(new int[][]{
                    new int[]{-android.R.attr.state_selected},
                    new int[]{android.R.attr.state_selected}
            },
                    new int[]{
                            ATEUtil.adjustAlpha(color, UNFOCUSED_ALPHA),
                            color
                    });
            for (int i = 0; i < tl.getTabCount(); i++) {
                final TabLayout.Tab tab = tl.getTabAt(i);
                if (tab != null && tab.getIcon() != null)
                    tab.setIcon(TintHelper.createTintedDrawable(tab.getIcon(), sl));
            }
        }
    }
}