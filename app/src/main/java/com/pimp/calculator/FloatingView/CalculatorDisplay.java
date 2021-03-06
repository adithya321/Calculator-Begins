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

package com.pimp.calculator.FloatingView;

import android.content.Context;
import android.graphics.Rect;
import android.text.Editable;
import android.text.InputType;
import android.text.Spanned;
import android.text.method.NumberKeyListener;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ViewSwitcher;

import com.pimp.calculator.CalculatorEditable;
import com.pimp.calculator.Logic;
import com.pimp.calculator.R;

import java.util.Arrays;
import java.util.List;

/**
 * Provides vertical scrolling for the input/result EditText.
 */
public class CalculatorDisplay extends ViewSwitcher implements OnLongClickListener {
    private static final String ATTR_MAX_DIGITS = "maxDigits";
    private static final int DEFAULT_MAX_DIGITS = 10;
    // only these chars are accepted from keyboard
    private static final char[] ACCEPTED_CHARS = "0123456789.+-*/\u2212\u00d7\u00f7()!%^".toCharArray();
    private static final int ANIM_DURATION = 400;
    private final List<String> mKeywords;
    TranslateAnimation inAnimUp;
    TranslateAnimation outAnimUp;
    TranslateAnimation inAnimDown;
    TranslateAnimation outAnimDown;
    private int mMaxDigits = DEFAULT_MAX_DIGITS;

    public CalculatorDisplay(Context context, AttributeSet attrs) {
        super(context, attrs);
        mMaxDigits = attrs.getAttributeIntValue(null, ATTR_MAX_DIGITS, DEFAULT_MAX_DIGITS);
        String sinString = context.getString(R.string.sin);
        String cosString = context.getString(R.string.cos);
        String tanString = context.getString(R.string.tan);
        String arcsinString = context.getString(R.string.arcsin);
        String arccosString = context.getString(R.string.arccos);
        String arctanString = context.getString(R.string.arctan);
        String logString = context.getString(R.string.lg);
        String lnString = context.getString(R.string.ln);
        String modString = context.getString(R.string.mod);
        String detString = context.getString(R.string.det);
        String dx = context.getString(R.string.dx);
        String dy = context.getString(R.string.dy);
        String cbrtString = context.getString(R.string.cbrt);

        mKeywords = Arrays.asList(arcsinString + "(", arccosString + "(", arctanString + "(", sinString + "(", cosString + "(", tanString + "(", logString + "(", modString + "(", lnString + "(", detString + "(", dx, dy, cbrtString + "(");
        setOnLongClickListener(this);
    }

    public int getMaxDigits() {
        return mMaxDigits;
    }

    public void setEditTextLayout(int resId) {
        for (int i = 0; i < getChildCount(); i++) {
            ((ScrollableDisplay) getChildAt(i)).getView().setEditTextLayout(resId);
        }
    }

    public void setLogic(Logic logic) {
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
                    int selectionHandle = getSelectionStart();
                    if (selectionHandle == 0) {
                        // Remove the view in front
                        AdvancedDisplay editor = getAdvancedDisplay();
                        int index = editor.getChildIndex(getActiveEditText());
                        if (index > 0) {
                            editor.removeView(editor.getChildAt(index - 1));
                        }
                    } else {
                        // Check and remove keywords
                        String textBeforeInsertionHandle = getActiveEditText().getText().toString().substring(0, selectionHandle);
                        String textAfterInsertionHandle = getActiveEditText().getText().toString().substring(selectionHandle, getActiveEditText().getText().toString().length());

                        for (String s : mKeywords) {
                            if (textBeforeInsertionHandle.endsWith(s)) {
                                int deletionLength = s.length();
                                String text = textBeforeInsertionHandle.substring(0, textBeforeInsertionHandle.length() - deletionLength) + textAfterInsertionHandle;
                                getActiveEditText().setText(text);
                                setSelection(selectionHandle - deletionLength);
                                return true;
                            }
                        }
                    }
                }
                return super.onKeyDown(view, content, keyCode, event);
            }
        };

        Editable.Factory factory = new CalculatorEditable.Factory(logic);
        for (int i = 0; i < 2; ++i) {
            AdvancedDisplay text = ((ScrollableDisplay) getChildAt(i)).getView();
            text.setLogic(logic);
            text.setEditableFactory(factory);
            text.setKeyListener(calculatorKeyListener);
            text.setLayoutParams(new ScrollableDisplay.LayoutParams(ScrollableDisplay.LayoutParams.WRAP_CONTENT, ScrollableDisplay.LayoutParams.WRAP_CONTENT, Gravity.RIGHT | Gravity.CENTER_VERTICAL));
        }
    }

    @Override
    public void setOnKeyListener(OnKeyListener l) {
        getChildAt(0).setOnKeyListener(l);
        getChildAt(1).setOnKeyListener(l);
    }

    @Override
    protected void onFocusChanged(boolean gain, int direction, Rect prev) {
        super.onFocusChanged(gain, direction, prev);
        if (!gain) {
            requestFocus();
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldW, int oldH) {
        inAnimUp = new TranslateAnimation(0, 0, h, 0);
        inAnimUp.setDuration(ANIM_DURATION);
        outAnimUp = new TranslateAnimation(0, 0, 0, -h);
        outAnimUp.setDuration(ANIM_DURATION);

        inAnimDown = new TranslateAnimation(0, 0, -h, 0);
        inAnimDown.setDuration(ANIM_DURATION);
        outAnimDown = new TranslateAnimation(0, 0, 0, h);
        outAnimDown.setDuration(ANIM_DURATION);
    }

    public EditText getActiveEditText() {
        AdvancedDisplay editor = getAdvancedDisplay();
        return editor.getActiveEditText();
    }

    public void insert(String delta) {
        AdvancedDisplay editor = getAdvancedDisplay();
        editor.insert(delta);
    }

    public AdvancedDisplay getAdvancedDisplay() {
        return ((ScrollableDisplay) getCurrentView()).getView();
    }

    public void setText(CharSequence text, Scroll dir) {
        if (getText().length() == 0) {
            dir = Scroll.NONE;
        }

        if (dir == Scroll.UP) {
            setInAnimation(inAnimUp);
            setOutAnimation(outAnimUp);
        } else if (dir == Scroll.DOWN) {
            setInAnimation(inAnimDown);
            setOutAnimation(outAnimDown);
        } else { // Scroll.NONE
            setInAnimation(null);
            setOutAnimation(null);
        }

        AdvancedDisplay editor = ((ScrollableDisplay) getNextView()).getView();
        editor.setText(text.toString());
        showNext();
        getAdvancedDisplay().getLastView().requestFocus();
    }

    public String getText() {
        AdvancedDisplay text = getAdvancedDisplay();
        return text.getText();
    }

    public int getSelectionStart() {
        if (getActiveEditText() == null) return 0;
        return getActiveEditText().getSelectionStart();
    }

    private void setSelection(int position) {
        getActiveEditText().setSelection(position);
    }

    @Override
    public boolean onLongClick(View v) {
        return getAdvancedDisplay().performLongClick();
    }

    public enum Scroll {
        UP,
        DOWN,
        NONE
    }
}
