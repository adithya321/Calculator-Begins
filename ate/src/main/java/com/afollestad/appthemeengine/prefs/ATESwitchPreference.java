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

package com.afollestad.appthemeengine.prefs;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.preference.Preference;
import android.preference.SwitchPreference;
import android.util.AttributeSet;
import android.view.View;

import com.afollestad.appthemeengine.ATE;
import com.afollestad.appthemeengine.R;
import com.afollestad.appthemeengine.inflation.ATESwitch;

import java.lang.reflect.Field;

/**
 * @author Aidan Follestad (afollestad)
 */
public class ATESwitchPreference extends SwitchPreference {

    private String mKey;
    private ATESwitch mSwitch;

    public ATESwitchPreference(Context context) {
        super(context);
        init(context, null);
    }

    public ATESwitchPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public ATESwitchPreference(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ATESwitchPreference(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        setLayoutResource(R.layout.ate_preference_custom);
        setWidgetLayoutResource(R.layout.ate_preference_switch);

        if (attrs != null) {
            TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.ATESwitchPreference, 0, 0);
            try {
                mKey = a.getString(R.styleable.ATESwitchPreference_ateKey_pref_switch);
            } finally {
                a.recycle();
            }
        }

        try {
            Field canRecycleLayoutField = Preference.class.getDeclaredField("mCanRecycleLayout");
            canRecycleLayoutField.setAccessible(true);
            canRecycleLayoutField.setBoolean(this, true);
        } catch (Exception ignored) {
        }

        try {
            Field hasSpecifiedLayout = Preference.class.getDeclaredField("mHasSpecifiedLayout");
            hasSpecifiedLayout.setAccessible(true);
            hasSpecifiedLayout.setBoolean(this, true);
        } catch (Exception ignored) {
        }
    }

    @Override
    protected void onBindView(View view) {
        super.onBindView(view);
        mSwitch = (ATESwitch) view.findViewById(R.id.switchWidget);
        mSwitch.setChecked(isChecked());
        mSwitch.setKey(mKey);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            mSwitch.setBackground(null);

        ATE.themeView(view, mKey);
    }

    @Override
    public void setChecked(boolean checked) {
        super.setChecked(checked);

        if (mSwitch != null) {
            mSwitch.setChecked(checked);
        }
    }
}