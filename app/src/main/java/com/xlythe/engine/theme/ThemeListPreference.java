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

package com.xlythe.engine.theme;

import android.content.Context;
import android.preference.ListPreference;
import android.preference.PreferenceManager;
import android.util.AttributeSet;

import com.pimp.calculator.R;

import java.util.List;

public class ThemeListPreference extends ListPreference {
    public ThemeListPreference(Context context) {
        super(context);
        setup();
    }

    public ThemeListPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        setup();
    }

    private void setup() {
        // Grab all installed themes
        final List<App> themes = Theme.getApps(getContext());
        CharSequence[] themeEntry = new CharSequence[themes.size() + 1];
        CharSequence[] themeValue = new CharSequence[themes.size() + 1];

        // Set a default
        themeEntry[0] = getContext().getString(R.string.preferences_option_default);
        themeValue[0] = getContext().getPackageName();

        // Add the rest to the preference
        for (int i = 1; i < themeEntry.length; i++) {
            themeEntry[i] = themes.get(i - 1).getName();
            themeValue[i] = themes.get(i - 1).getPackageName();
        }

        // Set the values
        setEntries(themeEntry);
        setEntryValues(themeValue);
        setDefaultValue(null);

        // Update the UI to display the selected com.xlythe.engine.xlythe.engine name
        setSummary(getThemeTitle(themes, PreferenceManager.getDefaultSharedPreferences(getContext()).getString(getKey(), getContext().getPackageName())));
    }

    private String getThemeTitle(List<App> themes, Object packageName) {
        for (App a : themes) {
            if (a.getPackageName().equals(packageName)) {
                return a.getName();
            }
        }
        return getContext().getString(R.string.preferences_option_default);
    }
}
