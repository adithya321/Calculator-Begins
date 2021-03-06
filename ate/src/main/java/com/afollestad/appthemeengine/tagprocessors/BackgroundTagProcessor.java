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
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import com.afollestad.appthemeengine.util.ATEUtil;

import java.lang.reflect.Method;

/**
 * @author Aidan Follestad (afollestad)
 */
public class BackgroundTagProcessor extends TagProcessor {

    public static final String PREFIX = "background";

    @Override
    public boolean isTypeSupported(@NonNull View view) {
        return true;
    }

    @Override
    public void process(@NonNull Context context, @Nullable String key, @NonNull View view, @NonNull String suffix) {
        final ColorResult result = getColorFromSuffix(context, key, view, suffix);
        if (result == null) return;

        if (ATEUtil.isInClassPath("android.support.v7.widget.CardView") &&
                (view.getClass().getName().equalsIgnoreCase("android.support.v7.widget.CardView") ||
                        view.getClass().getSuperclass().getName().equals("android.support.v7.widget.CardView"))) {
            try {
                final Class<?> cardViewCls = Class.forName("android.support.v7.widget.CardView");
                final Method setCardBg = cardViewCls.getMethod("setCardBackgroundColor", Integer.class);
                setCardBg.invoke(view, result.getColor());
            } catch (Throwable t) {
                t.printStackTrace();
            }
        } else {
            view.setBackgroundColor(result.getColor());
        }
    }


}