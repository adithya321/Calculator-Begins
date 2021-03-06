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
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.pimp.calculator.Calculator;
import com.pimp.calculator.EventListener;
import com.pimp.calculator.R;
import com.xlythe.engine.theme.Theme;
import com.xlythe.engine.theme.ThemedButton;

/**
 * Button with click-animation effect.
 */
public class ColorButton extends ThemedButton {
    static final int CLICK_FEEDBACK_INTERVAL = 10;
    static final int CLICK_FEEDBACK_DURATION = 350;
    final Paint mHintPaint;
    int CLICK_FEEDBACK_COLOR;
    float mTextX;
    float mTextY;
    long mAnimStart;
    EventListener mListener;
    Paint mFeedbackPaint;
    float mTextSize = 0f;

    public ColorButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        Calculator calc = (Calculator) context;
        init();
        mListener = calc.mListener;
        setOnClickListener(mListener);
        setOnLongClickListener(mListener);
        mHintPaint = new Paint(getPaint());
    }

    private void init() {
        CLICK_FEEDBACK_COLOR = Theme.getColor(getContext(), R.color.magic_flame);
        mFeedbackPaint = new Paint();
        mFeedbackPaint.setStyle(Style.STROKE);
        mFeedbackPaint.setStrokeWidth(2);

        mAnimStart = -1;
    }

    @Override
    public void setTypeface(Typeface tf) {
        if (mHintPaint != null) {
            if (mHintPaint.getTypeface() != tf) {
                mHintPaint.setTypeface(tf);
            }
        }
        super.setTypeface(tf);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (mAnimStart != -1) {
            int animDuration = (int) (System.currentTimeMillis() - mAnimStart);

            if (animDuration >= CLICK_FEEDBACK_DURATION) {
                mAnimStart = -1;
            } else {
                drawMagicFlame(animDuration, canvas);
                postInvalidateDelayed(CLICK_FEEDBACK_INTERVAL);
            }
        } else if (isPressed()) {
            drawMagicFlame(0, canvas);
        }

        mHintPaint.setColor(Theme.getColor(getContext(), R.color.button_hint_text));
        CharSequence hint = getHint();
        if (hint != null) {
            mHintPaint.setTextSize(getPaint().getTextSize() * getContext().getResources().getInteger(R.integer.button_hint_text_size_percent) / 100f);
            int offsetX = getContext().getResources().getDimensionPixelSize(R.dimen.button_hint_offset_x);
            int offsetY = (int) ((mTextY + getContext().getResources().getDimensionPixelSize(R.dimen.button_hint_offset_y) - mHintPaint.getTextSize()) / 2) - getPaddingTop();

            float textWidth = mHintPaint.measureText(hint.toString());
            float width = getWidth() - getPaddingLeft() - getPaddingRight() - mTextX - offsetX;
            float textSize = mHintPaint.getTextSize();
            if (textWidth > width) {
                mHintPaint.setTextSize(textSize * width / textWidth);
            }

            canvas.drawText(getHint(), 0, getHint().length(), mTextX + offsetX, mTextY - offsetY, mHintPaint);
        }

        getPaint().setColor(getCurrentTextColor());
        CharSequence text = getText();
        canvas.drawText(text, 0, text.length(), mTextX, mTextY, getPaint());
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (changed) layoutText();
    }

    @Override
    protected void onTextChanged(CharSequence text, int start, int before, int after) {
        layoutText();
    }

    private void layoutText() {
        Paint paint = getPaint();
        if (mTextSize != 0f) paint.setTextSize(mTextSize);
        float textWidth = paint.measureText(getText().toString());
        float width = getWidth() - getPaddingLeft() - getPaddingRight();
        float textSize = getTextSize();
        if (textWidth > width) {
            paint.setTextSize(textSize * width / textWidth);
            mTextX = getPaddingLeft();
            mTextSize = textSize;
        } else {
            mTextX = (getWidth() - textWidth) / 2;
        }
        mTextY = (getHeight() - paint.ascent() - paint.descent()) / 2;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        boolean result = super.onTouchEvent(event);

        switch (event.getAction()) {
            case MotionEvent.ACTION_UP:
                if (isPressed()) {
                    animateClickFeedback();
                } else {
                    invalidate();
                }
                break;
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_CANCEL:
                mAnimStart = -1;
                invalidate();
                break;
        }

        return result;
    }

    public void animateClickFeedback() {
        mAnimStart = System.currentTimeMillis();
        invalidate();
    }

    private void drawMagicFlame(int duration, Canvas canvas) {
        if (CLICK_FEEDBACK_COLOR >= 0x00000000 && CLICK_FEEDBACK_COLOR <= 0x00FFFFFF) {
            // Feedback has been set as transparent
            return;
        }
        int alpha = 255 - 255 * duration / CLICK_FEEDBACK_DURATION;
        int color = CLICK_FEEDBACK_COLOR | (alpha << 24);

        mFeedbackPaint.setColor(color);
        canvas.drawRect(1, 1, getWidth() - 1, getHeight() - 1, mFeedbackPaint);
    }
}
