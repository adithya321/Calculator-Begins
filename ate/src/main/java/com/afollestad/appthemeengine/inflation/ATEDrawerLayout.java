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

package com.afollestad.appthemeengine.inflation;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v4.widget.DrawerLayout;
import android.util.AttributeSet;

import com.afollestad.appthemeengine.ATE;
import com.afollestad.appthemeengine.ATEActivity;
import com.afollestad.appthemeengine.Config;

/**
 * @author Aidan Follestad (afollestad)
 */
class ATEDrawerLayout extends DrawerLayout implements ViewInterface {

    public ATEDrawerLayout(Context context) {
        super(context);
        init(context, null);
    }

    public ATEDrawerLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, null);
    }

    public ATEDrawerLayout(Context context, AttributeSet attrs, @Nullable ATEActivity keyContext) {
        super(context, attrs);
        init(context, keyContext);
    }

    private void init(Context context, @Nullable ATEActivity keyContext) {
        final String key = ATEViewUtil.init(keyContext, this, context);
        if (Config.coloredStatusBar(context, key)) {
            // Sets the status bar overlayed by the DrawerLayout
            setStatusBarBackgroundColor(Config.statusBarColor(context, key));
            if (context instanceof Activity && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                final Activity activity = (Activity) context;
                // Sets Activity status bar to transparent, DrawerLayout overlays a color.
                activity.getWindow().setStatusBarColor(Color.TRANSPARENT);
                ATE.invalidateLightStatusBar(activity, key);
            }
        }
    }

    @Override
    public boolean setsStatusBarColor() {
        return true;
    }

    @Override
    public boolean setsToolbarColor() {
        return false;
    }
}
