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

package com.afollestad.appthemeengine;

/**
 * @author Aidan Follestad (afollestad)
 */
interface ConfigKeys {

    String CONFIG_PREFS_KEY_DEFAULT = "[[afollestad_theme-engine]]";
    String CONFIG_PREFS_KEY_CUSTOM = "[[afollestad_heme-engine_%s]]";
    String IS_CONFIGURED_KEY = "is_configured";
    String IS_CONFIGURED_VERSION_KEY = "is_configured_version";
    String VALUES_CHANGED = "values_changed";

    String KEY_ACTIVITY_THEME = "activity_theme";
    String KEY_ACTIVITY_THEME_DEFTYPE = "activity_theme_deftype";

    String KEY_PRIMARY_COLOR = "primary_color";
    String KEY_PRIMARY_COLOR_DARK = "primary_color_dark";
    String KEY_ACCENT_COLOR = "accent_color";
    String KEY_STATUS_BAR_COLOR = "status_bar_color";
    String KEY_TOOLBAR_COLOR = "toolbar_color";
    String KEY_NAVIGATION_BAR_COLOR = "navigation_bar_color";

    String KEY_LIGHT_STATUS_BAR_MODE = "lightstatusbar_mode";
    String KEY_LIGHT_TOOLBAR_MODE = "lighttoolbar_mode";

    String KEY_TEXT_COLOR_PRIMARY = "text_color_primary";
    String KEY_TEXT_COLOR_PRIMARY_INVERSE = "text_color_primary_inverse";
    String KEY_TEXT_COLOR_SECONDARY = "text_color_secondary";
    String KEY_TEXT_COLOR_SECONDARY_INVERSE = "text_color_secondary_inverse";

    String KEY_APPLY_PRIMARYDARK_STATUSBAR = "apply_primarydark_statusbar";
    String KEY_APPLY_PRIMARY_SUPPORTAB = "apply_primary_supportab";
    String KEY_APPLY_PRIMARY_NAVBAR = "apply_primary_navbar";
    String KEY_AUTO_GENERATE_PRIMARYDARK = "auto_generate_primarydark";

    String KEY_THEMED_NAVIGATION_VIEW = "apply_navigation_view";
    String KEY_NAVIGATIONVIEW_SELECTED_TEXT = "navigation_view_selected_text";
    String KEY_NAVIGATIONVIEW_NORMAL_TEXT = "navigation_view_normal_text";
    String KEY_NAVIGATIONVIEW_SELECTED_ICON = "navigation_view_selected_icon";
    String KEY_NAVIGATIONVIEW_NORMAL_ICON = "navigation_view_normal_icon";
    String KEY_NAVIGATIONVIEW_SELECTED_BG = "navigation_view_selected_bg";
}