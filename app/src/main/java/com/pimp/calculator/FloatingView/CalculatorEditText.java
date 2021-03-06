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
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Handler;
import android.os.SystemClock;
import android.text.Editable;
import android.text.Html;
import android.text.Spanned;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.ActionMode;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.pimp.calculator.CalculatorSettings;
import com.pimp.calculator.R;
import com.xlythe.engine.theme.ThemedEditText;
import com.xlythe.math.BaseModule;
import com.xlythe.math.Constants;
import com.xlythe.math.EquationFormatter;

import java.text.DecimalFormatSymbols;

public class CalculatorEditText extends ThemedEditText {
    private static final int BLINK = 500;
    private final long mShowCursor = SystemClock.uptimeMillis();
    Paint mHighlightPaint = new Paint();
    Handler mHandler = new Handler();
    Runnable mRefresher = new Runnable() {
        @Override
        public void run() {
            CalculatorEditText.this.invalidate();
        }
    };
    String mDecSeparator;
    String mBinSeparator;
    String mHexSeparator;
    private EquationFormatter mEquationFormatter;
    private AdvancedDisplay mDisplay;
    private String mInput = "";
    private int mSelectionHandle = 0;

    public CalculatorEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        setUp();
    }

    public CalculatorEditText(final AdvancedDisplay display) {
        super(display.getContext());
        setUp();
        mDisplay = display;
    }

    public static String load(final AdvancedDisplay parent) {
        return CalculatorEditText.load("", parent);
    }

    public static String load(String text, final AdvancedDisplay parent) {
        return CalculatorEditText.load(text, parent, parent.getChildCount());
    }

    public static String load(String text, final AdvancedDisplay parent, final int pos) {
        final CalculatorEditText et = (CalculatorEditText) View.inflate(parent.getContext(), parent.getEditTextLayout(), null);
        et.mDisplay = parent;
        et.setText(text);
        et.setSelection(0);
        et.setLongClickable(false);
        if (parent.mKeyListener != null) et.setKeyListener(parent.mKeyListener);
        if (parent.mFactory != null) et.setEditableFactory(parent.mFactory);
        et.setFont("display_font");
        et.setEnabled(parent.isEnabled());
        AdvancedDisplay.LayoutParams params = new AdvancedDisplay.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.CENTER_VERTICAL;
        et.setLayoutParams(params);
        parent.addView(et, pos);
        return "";
    }

    private void setUp() {
        final Resources r = getContext().getResources();
        final DecimalFormatSymbols dfs = new DecimalFormatSymbols();
        mDecSeparator = dfs.getGroupingSeparator() + "";
        mBinSeparator = r.getString(R.string.bin_separator);
        mHexSeparator = r.getString(R.string.hex_separator);

        // Hide the keyboard
        setCustomSelectionActionModeCallback(new NoTextSelectionMode());
        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(this, InputMethodManager.SHOW_IMPLICIT);

        // Display ^ , and other visual cues
        mEquationFormatter = new EquationFormatter();
        addTextChangedListener(new TextWatcher() {
            boolean updating = false;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (updating) return;

                mInput = s.toString().replace(Constants.POWER_PLACEHOLDER, Constants.POWER).replace(mDecSeparator, "").replace(mBinSeparator, "").replace(mHexSeparator, "");
                updating = true;

                // Get the selection handle, since we're setting text and
                // that'll overwrite it
                mSelectionHandle = getSelectionStart();
                // Adjust the handle by removing any comas or spacing to the
                // left
                String cs = s.subSequence(0, mSelectionHandle).toString();
                mSelectionHandle -= countOccurrences(cs, mDecSeparator.charAt(0));
                if (!mBinSeparator.equals(mDecSeparator)) {
                    mSelectionHandle -= countOccurrences(cs, mBinSeparator.charAt(0));
                }
                if (!mHexSeparator.equals(mBinSeparator) && !mHexSeparator.equals(mDecSeparator)) {
                    mSelectionHandle -= countOccurrences(cs, mHexSeparator.charAt(0));
                }

                setText(formatText(mInput));
                setSelection(Math.min(mSelectionHandle, getText().length()));
                updating = false;
            }
        });

        setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus && mDisplay != null)
                    mDisplay.mActiveEditText = CalculatorEditText.this;
            }
        });

        // Listen for the enter button on physical keyboards
        setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                mDisplay.mLogic.onEnter();
                return true;
            }
        });
    }

    private int countOccurrences(String haystack, char needle) {
        int count = 0;
        for (int i = 0; i < haystack.length(); i++) {
            if (haystack.charAt(i) == needle) {
                count++;
            }
        }
        return count;
    }

    private Spanned formatText(String input) {
        BaseModule bm = mDisplay.mLogic.getBaseModule();
        if (CalculatorSettings.digitGrouping(getContext())) {
            // Add grouping, and then split on the selection handle
            // which is saved as a unique char
            String grouped = bm.groupSentence(input, mSelectionHandle);
            if (grouped.contains(String.valueOf(BaseModule.SELECTION_HANDLE))) {
                String[] temp = grouped.split(String.valueOf(BaseModule.SELECTION_HANDLE));
                mSelectionHandle = temp[0].length();
                input = "";
                for (String s : temp) {
                    input += s;
                }
            } else {
                input = grouped;
                mSelectionHandle = input.length();
            }
        }
        return Html.fromHtml(mEquationFormatter.insertSupScripts(input));
    }

    public AdvancedDisplay getAdvancedDisplay() {
        return mDisplay;
    }

    @Override
    public String toString() {
        return mInput;
    }

    @Override
    public View focusSearch(int direction) {
        View v;
        switch (direction) {
            case View.FOCUS_FORWARD:
                v = mDisplay.nextView(this);
                while (!v.isFocusable())
                    v = mDisplay.nextView(v);
                return v;
            case View.FOCUS_BACKWARD:
                v = mDisplay.previousView(this);
                while (!v.isFocusable())
                    v = mDisplay.previousView(v);
                if (MatrixView.class.isAssignableFrom(v.getClass())) {
                    v = ((ViewGroup) v).getChildAt(((ViewGroup) v).getChildCount() - 1);
                    v = ((ViewGroup) v).getChildAt(((ViewGroup) v).getChildCount() - 1);
                }
                return v;
        }
        return super.focusSearch(direction);
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // TextViews don't draw the cursor if textLength is 0. Because we're an
        // array of TextViews, we'd prefer that it did.
        if (getText().length() == 0 && isEnabled() && (isFocused() || isPressed())) {
            if ((SystemClock.uptimeMillis() - mShowCursor) % (2 * BLINK) < BLINK) {
                mHighlightPaint.setColor(getCurrentTextColor());
                mHighlightPaint.setStyle(Paint.Style.STROKE);
                canvas.drawLine(getWidth() / 2, 0, getWidth() / 2, getHeight(), mHighlightPaint);
                mHandler.postAtTime(mRefresher, SystemClock.uptimeMillis() + BLINK);
            }
        }
    }

    class NoTextSelectionMode implements ActionMode.Callback {
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
