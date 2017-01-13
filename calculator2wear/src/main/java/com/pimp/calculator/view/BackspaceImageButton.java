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
import android.util.AttributeSet;
import android.widget.ImageButton;

import com.pimp.calculator.R;

public class BackspaceImageButton extends ImageButton {
    private static final int[] STATE_DELETE = {R.attr.state_delete};
    private static final int[] STATE_CLEAR = {R.attr.state_clear};
    private State mState = State.DELETE;

    public BackspaceImageButton(Context context) {
        super(context);
        setup();
    }

    public BackspaceImageButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        setup();
    }

    public BackspaceImageButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setup();
    }

    public BackspaceImageButton(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        setup();
    }

    private void setup() {
        setState(State.DELETE);
    }

    public State getState() {
        return mState;
    }

    public void setState(State state) {
        mState = state;
        refreshDrawableState();
    }

    @Override
    public int[] onCreateDrawableState(int extraSpace) {
        int[] state = super.onCreateDrawableState(extraSpace + 1);
        if (mState == null) mState = State.DELETE;

        switch (mState) {
            case DELETE:
                mergeDrawableStates(state, STATE_DELETE);
                break;
            case CLEAR:
                mergeDrawableStates(state, STATE_CLEAR);
                break;
        }
        return state;
    }

    public enum State {
        DELETE, CLEAR
    }
}
