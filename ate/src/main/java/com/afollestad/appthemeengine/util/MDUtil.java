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

package com.afollestad.appthemeengine.util;

import android.content.Context;
import android.content.res.ColorStateList;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.afollestad.appthemeengine.Config;
import com.afollestad.materialdialogs.internal.ThemeSingleton;

/**
 * @author Aidan Follestad (afollestad)
 */
public final class MDUtil {

    public static final String MAIN_CLASS = "com.afollestad.materialdialogs.MaterialDialog";

    private MDUtil() {
    }

    public static void initMdSupport(@NonNull Context context, @Nullable String key) {
        final ThemeSingleton md = ThemeSingleton.get();
        md.titleColor = Config.textColorPrimary(context, key);
        md.contentColor = Config.textColorSecondary(context, key);
        md.itemColor = md.titleColor;
        md.widgetColor = Config.accentColor(context, key);
        md.linkColor = ColorStateList.valueOf(md.widgetColor);
        md.positiveColor = ColorStateList.valueOf(md.widgetColor);
        md.neutralColor = ColorStateList.valueOf(md.widgetColor);
        md.negativeColor = ColorStateList.valueOf(md.widgetColor);
    }
}