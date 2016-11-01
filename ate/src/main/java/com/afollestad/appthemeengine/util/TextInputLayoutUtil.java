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

package com.afollestad.appthemeengine.util;

import android.content.res.ColorStateList;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;

import java.lang.reflect.Field;

/**
 * @author Aidan Follestad (afollestad)
 */
public final class TextInputLayoutUtil {

    private TextInputLayoutUtil() {
    }

    public static void setHint(@NonNull TextInputLayout view, @ColorInt int hintColor) {
        try {
            final Field mDefaultTextColorField = TextInputLayout.class.getDeclaredField("mDefaultTextColor");
            mDefaultTextColorField.setAccessible(true);
            mDefaultTextColorField.set(view, ColorStateList.valueOf(hintColor));
        } catch (Throwable t) {
            throw new RuntimeException("Failed to set TextInputLayout hint (collapsed) color: " + t.getLocalizedMessage(), t);
        }
    }

    public static void setAccent(@NonNull TextInputLayout view, @ColorInt int accentColor) {
        try {
            final Field mFocusedTextColorField = TextInputLayout.class.getDeclaredField("mFocusedTextColor");
            mFocusedTextColorField.setAccessible(true);
            mFocusedTextColorField.set(view, ColorStateList.valueOf(accentColor));
        } catch (Throwable t) {
            throw new RuntimeException("Failed to set TextInputLayout accent (expanded) color: " + t.getLocalizedMessage(), t);
        }
    }
}