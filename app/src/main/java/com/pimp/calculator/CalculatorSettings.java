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
import android.preference.PreferenceManager;

import com.pimp.calculator.Page.Panel;

public class CalculatorSettings {
    public static boolean isPageEnabled(Context context, Panel panel) {
        return isPageEnabled(context, new Page(context, panel));
    }

    public static boolean isPageEnabled(Context context, Page page) {
        return PreferenceManager.getDefaultSharedPreferences(context).getBoolean(page.getKey(), page.getDefaultValue());
    }

    public static void setPageEnabled(Context context, Page page, boolean enabled) {
        PreferenceManager.getDefaultSharedPreferences(context).edit().putBoolean(page.getKey(), enabled).commit();
    }

    public static void setPageOrder(Context context, Page page, int order) {
        PreferenceManager.getDefaultSharedPreferences(context).edit().putInt(page.getKey() + "_order", order).commit();
    }

    public static int getPageOrder(Context context, Page page) {
        return PreferenceManager.getDefaultSharedPreferences(context).getInt(page.getKey() + "_order", Integer.MAX_VALUE);
    }

    static void setRadiansEnabled(Context context, boolean enabled) {
        PreferenceManager.getDefaultSharedPreferences(context).edit().putBoolean("USE_RADIANS", enabled).commit();
    }

    static boolean useRadians(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getBoolean("USE_RADIANS", context.getResources().getBoolean(R.bool.USE_RADIANS));
    }

    static boolean returnToBasic(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getBoolean("RETURN_TO_BASIC", context.getResources().getBoolean(R.bool.RETURN_TO_BASIC));
    }

    public static boolean useInfiniteScrolling(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getBoolean("INFINITE_SCROLLING", context.getResources().getBoolean(R.bool.RETURN_TO_BASIC));
    }

    static boolean clickToOpenHistory(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getBoolean("CLICK_TO_OPEN_HISTORY", context.getResources().getBoolean(R.bool.CLICK_TO_OPEN_HISTORY));
    }

    public static boolean digitGrouping(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getBoolean("DIGIT_GROUPING", context.getResources().getBoolean(R.bool.DIGIT_GROUPING));
    }

    public static boolean showDetails(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getBoolean("SHOW_DETAILS", context.getResources().getBoolean(R.bool.SHOW_DETAILS));
    }

    public static boolean floatingCalculator(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getBoolean("FLOATING_CALCULATOR", context.getResources().getBoolean(R.bool.FLOATING_CALCULATOR));
    }

    public static boolean vibrateOnPress(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getBoolean("VIBRATE_ON_PRESS", context.getResources().getBoolean(R.bool.VIBRATE_ON_PRESS));
    }

    static boolean isDismissed(Context context, String key) {
        return PreferenceManager.getDefaultSharedPreferences(context).getBoolean(key, false);
    }

    public static String getTheme(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getString("SELECTED_THEME", context.getPackageName());
    }

    public static void setTheme(Context context, String packageName) {
        PreferenceManager.getDefaultSharedPreferences(context).edit().putString("SELECTED_THEME", packageName).commit();
    }

    static void saveKey(Context context, String key, boolean value) {
        PreferenceManager.getDefaultSharedPreferences(context).edit().putBoolean(key, value).commit();
    }

    public static int getVibrationStrength() {
        return 10;
    }

    public static boolean showWidgetBackground(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getBoolean("SHOW_WIDGET_BACKGROUND", context.getResources().getBoolean(R.bool.SHOW_WIDGET_BACKGROUND));
    }
}
