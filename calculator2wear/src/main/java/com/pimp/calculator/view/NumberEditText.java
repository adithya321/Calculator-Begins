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

package com.pimp.calculator.view;

import android.content.Context;
import android.text.Editable;
import android.text.InputType;
import android.text.Spanned;
import android.text.method.NumberKeyListener;
import android.util.AttributeSet;
import android.view.ActionMode;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;

/**
 * NumberEditText disables the keyboard and most EditText touch events.
 * It also restricts the characters allowed from physical keyboards to only numbers
 * and a few operations.
 */
public class NumberEditText extends ResizingEditText {
    // Restrict keys from hardware keyboards
    private static final char[] ACCEPTED_CHARS = "0123456789.+-*/\u2212\u00d7\u00f7()!%^".toCharArray();

    private final Editable.Factory mFactory = new CalculatorEditable.Factory();

    public NumberEditText(Context context) {
        super(context);
        setUp(context, null);
    }

    public NumberEditText(Context context, AttributeSet attr) {
        super(context, attr);
        setUp(context, attr);
    }

    private void setUp(Context context, AttributeSet attrs) {
        setLongClickable(false);

        // Disable highlighting text
        setCustomSelectionActionModeCallback(new NoTextSelectionMode());
        setEditableFactory(mFactory);
        NumberKeyListener calculatorKeyListener = new NumberKeyListener() {
            @Override
            public int getInputType() {
                return EditorInfo.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS;
            }

            @Override
            protected char[] getAcceptedChars() {
                return ACCEPTED_CHARS;
            }

            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                /*
                 * the EditText should still accept letters (eg. 'sin') coming from the on-screen touch buttons, so don't filter anything.
                 */
                return null;
            }

            @Override
            public boolean onKeyDown(View view, Editable content, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_DEL) {
                    NumberEditText.this.backspace();
                }
                return super.onKeyDown(view, content, keyCode, event);
            }
        };
        setKeyListener(calculatorKeyListener);
    }

    public Editable.Factory getEditableFactory() {
        return mFactory;
    }

    public void backspace() {
        String text = getText().toString();
        int selectionHandle = getSelectionStart();
        String textBeforeInsertionHandle = text.substring(0, selectionHandle);
        String textAfterInsertionHandle = text.substring(selectionHandle, text.length());

        if (selectionHandle != 0) {
            setText(textBeforeInsertionHandle.substring(0, textBeforeInsertionHandle.length() - 1)
                    + textAfterInsertionHandle);
            setSelection(selectionHandle - 1);
        }
    }

    private class NoTextSelectionMode implements ActionMode.Callback {
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            // Prevents the selection action mode on double tap.
            return false;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            return false;
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
        }
    }
}
