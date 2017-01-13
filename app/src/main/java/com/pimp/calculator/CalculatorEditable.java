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

import android.text.Editable;
import android.text.SpannableStringBuilder;

public class CalculatorEditable extends SpannableStringBuilder {
    private static final char[] ORIGINALS = {'-', '*', '/'};
    private static final char[] REPLACEMENTS = {'\u2212', '*', '/'};
    private boolean isInsideReplace = false;
    private Logic mLogic;

    private CalculatorEditable(CharSequence source, Logic logic) {
        super(source);
        mLogic = logic;
    }

    @Override
    public SpannableStringBuilder replace(int start, int end, CharSequence tb, int tbstart, int tbend) {
        if (isInsideReplace) {
            return super.replace(start, end, tb, tbstart, tbend);
        } else {
            isInsideReplace = true;
            try {
                String delta = tb.subSequence(tbstart, tbend).toString();
                return internalReplace(start, end, delta);
            } finally {
                isInsideReplace = false;
            }
        }
    }

    private SpannableStringBuilder internalReplace(int start, int end, String delta) {
        if (!mLogic.acceptInsert(delta)) {
            mLogic.cleared();
            start = 0;
            end = length();
        }

        for (int i = ORIGINALS.length - 1; i >= 0; --i) {
            delta = delta.replace(ORIGINALS[i], REPLACEMENTS[i]);
        }

        int length = delta.length();
        if (length == 1) {
            char text = delta.charAt(0);

            // don't allow two dots in the same number
            if (text == '.') {
                int p = start - 1;
                while (p >= 0 && Character.isDigit(charAt(p))) {
                    --p;
                }
                if (p >= 0 && charAt(p) == '.') {
                    return super.replace(start, end, "");
                }
            }

            char prevChar = start > 0 ? charAt(start - 1) : '\0';

            // don't allow 2 successive minuses
            if (text == Logic.MINUS && prevChar == Logic.MINUS) {
                return super.replace(start, end, "");
            }

            // TODO move to advanced display
            // don't allow multiple successive operators
            if (Logic.isOperator(text)) {
                while (Logic.isOperator(prevChar) && (text != Logic.MINUS || prevChar == '+')) {
                    --start;
                    prevChar = start > 0 ? charAt(start - 1) : '\0';
                }
            }
        }
        return super.replace(start, end, delta);
    }

    public static class Factory extends Editable.Factory {
        private Logic mLogic;

        public Factory(Logic logic) {
            mLogic = logic;
        }

        public Editable newEditable(CharSequence source) {
            return new CalculatorEditable(source, mLogic);
        }
    }
}
