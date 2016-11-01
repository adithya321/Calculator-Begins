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

public class MutableString {
    private String mText;

    public MutableString() {
    }

    public MutableString(String text) {
        this.mText = text;
    }

    public String getText() {
        return mText;
    }

    public void setText(String text) {
        this.mText = text;
    }

    public int length() {
        return mText.length();
    }

    public boolean isEmpty() {
        return mText.isEmpty();
    }

    public String substring(int start) {
        return mText.substring(start);
    }

    public String substring(int start, int end) {
        return mText.substring(start, end);
    }

    public CharSequence subSequence(int start, int end) {
        return mText.subSequence(start, end);
    }

    public boolean startsWith(String prefix) {
        return mText.startsWith(prefix);
    }
}
