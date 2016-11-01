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

package com.pimp.calculator.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.pimp.calculator.R;

/**
 * A ViewGroup that can be set to disallow touch events on its parents or children.
 */
public class SolidPadLayout extends CalculatorPadLayout {
    private static final String STATE_SUPER = "super";
    private static final String STATE_PREVENT_PARENT_TOUCH_EVENTS = "prevent_parent_touch_events";
    private static final String STATE_PREVENT_CHILD_TOUCH_EVENTS = "prevent_child_touch_events";

    private boolean mPreventParentTouchEvents;
    private boolean mPreventChildTouchEvents;

    public SolidPadLayout(Context context) {
        this(context, null);
    }

    public SolidPadLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SolidPadLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        if (attrs != null) {
            final TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SolidLayout, 0, 0);
            mPreventParentTouchEvents = a.getBoolean(R.styleable.SolidLayout_preventParentTouchEvents, mPreventParentTouchEvents);
            mPreventChildTouchEvents = a.getBoolean(R.styleable.SolidLayout_preventChildTouchEvents, mPreventChildTouchEvents);
            a.recycle();
        }
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();
        bundle.putParcelable(STATE_SUPER, super.onSaveInstanceState());
        bundle.putBoolean(STATE_PREVENT_PARENT_TOUCH_EVENTS, mPreventParentTouchEvents);
        bundle.putBoolean(STATE_PREVENT_CHILD_TOUCH_EVENTS, mPreventChildTouchEvents);
        return bundle;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        Bundle bundle = (Bundle) state;
        mPreventParentTouchEvents = bundle.getBoolean(STATE_PREVENT_PARENT_TOUCH_EVENTS);
        mPreventChildTouchEvents = bundle.getBoolean(STATE_PREVENT_CHILD_TOUCH_EVENTS);
        super.onRestoreInstanceState(bundle.getParcelable(STATE_SUPER));
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
