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

package com.afollestad.appthemeengine;

import android.app.Activity;
import android.support.annotation.AttrRes;
import android.support.annotation.CheckResult;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.DimenRes;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.view.View;

/**
 * @author Aidan Follestad (afollestad)
 */
interface ConfigInterface {

    @CheckResult
    boolean isConfigured();

    @CheckResult
    boolean isConfigured(@IntRange(from = 0, to = Integer.MAX_VALUE) int version);

    // Activity theme

    Config activityTheme(@StyleRes int theme);

    // Primary colors

    Config primaryColor(@ColorInt int color);

    Config primaryColorRes(@ColorRes int colorRes);

    Config primaryColorAttr(@AttrRes int colorAttr);

    Config autoGeneratePrimaryDark(boolean autoGenerate);

    Config primaryColorDark(@ColorInt int color);

    Config primaryColorDarkRes(@ColorRes int colorRes);

    Config primaryColorDarkAttr(@AttrRes int colorAttr);

    // Accent colors

    Config accentColor(@ColorInt int color);

    Config accentColorRes(@ColorRes int colorRes);

    Config accentColorAttr(@AttrRes int colorAttr);

    // Status/nav bar color

    Config statusBarColor(@ColorInt int color);

    Config statusBarColorRes(@ColorRes int colorRes);

    Config statusBarColorAttr(@AttrRes int colorAttr);

    Config navigationBarColor(@ColorInt int color);

    Config navigationBarColorRes(@ColorRes int colorRes);

    Config navigationBarColorAttr(@AttrRes int colorAttr);

    //Toolbar color

    Config toolbarColor(@ColorInt int color);

    Config toolbarColorRes(@ColorRes int colorRes);

    Config toolbarColorAttr(@AttrRes int colorAttr);

    // Light UI

    Config lightStatusBarMode(@Config.LightStatusBarMode int mode);

    Config lightToolbarMode(@Config.LightToolbarMode int mode);

    // Primary text color

    Config textColorPrimary(@ColorInt int color);

    Config textColorPrimaryRes(@ColorRes int colorRes);

    Config textColorPrimaryAttr(@AttrRes int colorAttr);

    // Secondary text color

    Config textColorSecondary(@ColorInt int color);

    Config textColorSecondaryRes(@ColorRes int colorRes);

    Config textColorSecondaryAttr(@AttrRes int colorAttr);

    // Toggle configurations

    Config coloredStatusBar(boolean colored);

    Config coloredActionBar(boolean applyToActionBar);

    Config coloredNavigationBar(boolean applyToNavBar);

    Config navigationViewThemed(boolean themed);

    // NavigationView colors

    Config navigationViewSelectedIcon(@ColorInt int color);

    Config navigationViewSelectedIconRes(@ColorRes int colorRes);

    Config navigationViewSelectedIconAttr(@AttrRes int colorAttr);

    Config navigationViewNormalIcon(@ColorInt int color);

    Config navigationViewNormalIconRes(@ColorRes int colorRes);

    Config navigationViewNormalIconAttr(@AttrRes int colorAttr);

    Config navigationViewSelectedText(@ColorInt int color);

    Config navigationViewSelectedTextRes(@ColorRes int colorRes);

    Config navigationViewSelectedTextAttr(@AttrRes int colorAttr);

    Config navigationViewNormalText(@ColorInt int color);

    Config navigationViewNormalTextRes(@ColorRes int colorRes);

    Config navigationViewNormalTextAttr(@AttrRes int colorAttr);

    Config navigationViewSelectedBg(@ColorInt int color);

    Config navigationViewSelectedBgRes(@ColorRes int colorRes);

    Config navigationViewSelectedBgAttr(@AttrRes int colorAttr);

    // Text size

    Config textSizePxForMode(@IntRange(from = 1, to = Integer.MAX_VALUE) int pxValue, @Config.TextSizeMode String mode);

    Config textSizeSpForMode(@IntRange(from = 1, to = Integer.MAX_VALUE) int dpValue, @Config.TextSizeMode String mode);

    Config textSizeResForMode(@DimenRes int resId, @Config.TextSizeMode String mode);

    // Commit/themeView

    void commit();

    void apply(@NonNull Activity activity);

    //    void themeView(@NonNull android.support.v4.app.Fragment fragment);
//
//    void themeView(@NonNull android.app.Fragment fragment);
//
    void apply(@NonNull View view);
}
