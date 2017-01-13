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
import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.FrameLayout;

import com.pimp.calculator.R;

/**
 * A ViewGroup that can be set to disallow touch events on its parents or children.
 */
public class SolidLayout extends FrameLayout {
    private boolean mPreventParentTouchEvents;
    private boolean mPreventChildTouchEvents;

    public SolidLayout(Context context) {
        this(context, null);
    }

    public SolidLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SolidLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        if (attrs != null) {
            final TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SolidLayout, 0, 0);
            mPreventParentTouchEvents = a.getBoolean(R.styleable.SolidLayout_preventParentTouchEvents, mPreventParentTouchEvents);
            mPreventChildTouchEvents = a.getBoolean(R.styleable.SolidLayout_preventChildTouchEvents, mPreventChildTouchEvents);
            a.recycle();
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (mPreventChildTouchEvents) {
            return true;
        }
        if (mPreventParentTouchEvents) {
            if (ev.getAction() == MotionEvent.ACTION_DOWN) {
                getParent().requestDisallowInterceptTouchEvent(true);
            }
        }
        return super.onInterceptTouchEvent(ev);
    }

    public void setPreventParentTouchEvents(boolean prevent) {
        mPreventParentTouchEvents = prevent;
    }

    public void setPreventChildTouchEvents(boolean prevent) {
        mPreventChildTouchEvents = prevent;
    }

    @Override
    public boolean onTouchEvent(@NonNull MotionEvent event) {
        super.onTouchEvent(event);
        return true;
    }
}
