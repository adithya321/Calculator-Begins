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

package com.pimp.calculator;

import android.content.Context;

import com.xlythe.math.Constants;

import java.util.LinkedList;
import java.util.List;

public class CalculatorExpressionTokenizer {
    private final Context mContext;
    private final List<Localizer> mReplacements;

    public CalculatorExpressionTokenizer(Context context) {
        mContext = context;
        mReplacements = new LinkedList<Localizer>();
    }

    private void generateReplacements(Context context) {
        Constants.rebuildConstants();
        mReplacements.clear();
        mReplacements.add(new Localizer(",", String.valueOf(Constants.MATRIX_SEPARATOR)));
        mReplacements.add(new Localizer(".", String.valueOf(Constants.DECIMAL_POINT)));
        mReplacements.add(new Localizer("0", context.getString(R.string.digit0)));
        mReplacements.add(new Localizer("1", context.getString(R.string.digit1)));
        mReplacements.add(new Localizer("2", context.getString(R.string.digit2)));
        mReplacements.add(new Localizer("3", context.getString(R.string.digit3)));
        mReplacements.add(new Localizer("4", context.getString(R.string.digit4)));
        mReplacements.add(new Localizer("5", context.getString(R.string.digit5)));
        mReplacements.add(new Localizer("6", context.getString(R.string.digit6)));
        mReplacements.add(new Localizer("7", context.getString(R.string.digit7)));
        mReplacements.add(new Localizer("8", context.getString(R.string.digit8)));
        mReplacements.add(new Localizer("9", context.getString(R.string.digit9)));
        mReplacements.add(new Localizer("/", context.getString(R.string.div)));
        mReplacements.add(new Localizer("*", context.getString(R.string.mul)));
        mReplacements.add(new Localizer("-", context.getString(R.string.minus)));
        mReplacements.add(new Localizer("cbrt", context.getString(R.string.cbrt)));
        mReplacements.add(new Localizer("asin", context.getString(R.string.arcsin)));
        mReplacements.add(new Localizer("acos", context.getString(R.string.arccos)));
        mReplacements.add(new Localizer("atan", context.getString(R.string.arctan)));
        mReplacements.add(new Localizer("sin", context.getString(R.string.sin)));
        mReplacements.add(new Localizer("cos", context.getString(R.string.cos)));
        mReplacements.add(new Localizer("tan", context.getString(R.string.tan)));
        if (!CalculatorSettings.useRadians(context)) {
            mReplacements.add(new Localizer("sind", "sin"));
            mReplacements.add(new Localizer("cosd", "cos"));
            mReplacements.add(new Localizer("tand", "tan"));
        }
        mReplacements.add(new Localizer("ln", context.getString(R.string.ln)));
        mReplacements.add(new Localizer("log", context.getString(R.string.lg)));
        mReplacements.add(new Localizer("det", context.getString(R.string.det)));
        mReplacements.add(new Localizer("Infinity", "\u221e"));
    }

    public String getNormalizedExpression(String expr) {
        generateReplacements(mContext);
        for (Localizer replacement : mReplacements) {
            expr = expr.replace(replacement.local, replacement.english);
        }
        return expr;
    }

    public String getLocalizedExpression(String expr) {
        generateReplacements(mContext);
        for (Localizer replacement : mReplacements) {
            expr = expr.replace(replacement.english, replacement.local);
        }
        return expr;
    }

    private class Localizer {
        String english;
        String local;

        Localizer(String english, String local) {
            this.english = english;
            this.local = local;
        }
    }
}