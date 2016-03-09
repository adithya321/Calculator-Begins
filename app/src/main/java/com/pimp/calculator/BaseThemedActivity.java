package com.pimp.calculator;

import android.preference.PreferenceManager;
import android.support.annotation.Nullable;

import com.afollestad.appthemeengine.ATEActivity;

public class BaseThemedActivity extends ATEActivity {

    @Nullable
    @Override
    public final String getATEKey() {
        return PreferenceManager.getDefaultSharedPreferences(this).getBoolean("dark_theme", true) ?
                "dark_theme" : "light_theme";
    }
}
