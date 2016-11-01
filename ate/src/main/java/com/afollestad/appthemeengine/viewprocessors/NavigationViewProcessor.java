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

package com.afollestad.appthemeengine.viewprocessors;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.StateListDrawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;

import com.afollestad.appthemeengine.Config;
import com.afollestad.appthemeengine.util.ATEUtil;

/**
 * @author Aidan Follestad (afollestad)
 */
public class NavigationViewProcessor implements ViewProcessor<NavigationView, Void> {

    public static final String MAIN_CLASS = "android.support.design.widget.NavigationView";

    @Override
    public void process(@NonNull Context context, @Nullable String key, @Nullable NavigationView view, @Nullable Void extra) {
        if (view == null || !Config.navigationViewThemed(context, key))
            return;

        boolean darkTheme = false;
        if (view.getBackground() != null && view.getBackground() instanceof ColorDrawable) {
            final ColorDrawable cd = (ColorDrawable) view.getBackground();
            darkTheme = !ATEUtil.isColorLight(cd.getColor());
        }

        final ColorStateList iconSl = new ColorStateList(
                new int[][]{
                        new int[]{-android.R.attr.state_checked},
                        new int[]{android.R.attr.state_checked}
                },
                new int[]{
                        Config.navigationViewNormalIcon(context, key, darkTheme),
                        Config.navigationViewSelectedIcon(context, key, darkTheme)
                });
        final ColorStateList textSl = new ColorStateList(
                new int[][]{
                        new int[]{-android.R.attr.state_checked},
                        new int[]{android.R.attr.state_checked}
                },
                new int[]{
                        Config.navigationViewNormalText(context, key, darkTheme),
                        Config.navigationViewSelectedText(context, key, darkTheme)
                });
        view.setItemTextColor(textSl);
        view.setItemIconTintList(iconSl);

        StateListDrawable bgDrawable = new StateListDrawable();
        bgDrawable.addState(new int[]{android.R.attr.state_checked}, new ColorDrawable(
                Config.navigationViewSelectedBg(context, key, darkTheme)));
        view.setItemBackground(bgDrawable);

        // TODO not needed since the layout inflater will catch it?
//        final View headerView = view.getHeaderView(0);
//        if (headerView != null) ATE.themeView(context, headerView, key);
    }
}
