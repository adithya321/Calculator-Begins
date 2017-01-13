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

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Handler;
import android.text.Selection;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.widget.Toast;

import com.pimp.calculator.R;

public class FloatingCalculatorEditText extends CalculatorEditText {
    public static int LONG_PRESS_TIME = ViewConfiguration.getLongPressTimeout();
    private final Handler mHandler = new Handler();
    private long mPressedTime;
    private Runnable mOnLongPressed = new Runnable() {
        public void run() {
            copyContent(getAdvancedDisplay().getText());
        }
    };

    public FloatingCalculatorEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            mPressedTime = System.currentTimeMillis();
            mHandler.postDelayed(mOnLongPressed, LONG_PRESS_TIME);
        }
        if (event.getAction() == MotionEvent.ACTION_UP) {
            if (System.currentTimeMillis() - mPressedTime < LONG_PRESS_TIME) {
                mHandler.removeCallbacks(mOnLongPressed);
            }
            final int offset = getOffsetForPosition(event.getX(), event.getY());
            Selection.setSelection(getText(), offset);
        }
        return true;
    }

    @Override
    public void setDefaultFont() {
    }

    @Override
    public void setFont(String font) {
    }

    private void copyContent(String text) {
        ClipboardManager clipboard = (ClipboardManager) getContext().getSystemService(Context.CLIPBOARD_SERVICE);
        clipboard.setPrimaryClip(ClipData.newPlainText(null, text));
        String toastText = getContext().getResources().getString(R.string.text_copied_toast);
        Toast.makeText(getContext(), toastText, Toast.LENGTH_SHORT).show();
    }
}
