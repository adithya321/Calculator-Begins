package com.pimp.calculator.FloatingView;

import android.content.Context;
import android.text.Editable;
import android.text.InputType;
import android.text.Spanned;
import android.text.method.NumberKeyListener;
import android.view.ActionMode;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.pimp.calculator.Logic;
import com.pimp.calculator.R;
import com.xlythe.engine.theme.Theme;
import com.xlythe.engine.theme.ThemedEditText;

public class MatrixEditText extends ThemedEditText implements OnFocusChangeListener {
    private static final char[] ACCEPTED_CHARS = "0123456789,.-\u2212".toCharArray();

    private MatrixView mParent;
    private AdvancedDisplay mDisplay;

    public MatrixEditText(Context context) {
        super(context);
    }

    public MatrixEditText(final AdvancedDisplay display, final MatrixView parent) {
        super(display.getContext());
        setCustomSelectionActionModeCallback(new NoTextSelectionMode());
        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(this, InputMethodManager.SHOW_IMPLICIT);
        int padding = getContext().getResources().getDimensionPixelSize(R.dimen.matrix_edit_text_padding);
        setPadding(padding, 0, padding, 0);
        this.mParent = parent;
        this.mDisplay = display;
        setKeyListener(new MatrixKeyListener());
        setOnFocusChangeListener(this);
        setGravity(Gravity.CENTER);
        setTextColor(Theme.get(R.color.display_text_color));
        setFont("display_font");

        // Listen for the enter button on physical keyboards
        setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                mDisplay.mLogic.onEnter();
                return true;
            }
        });
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus) {
            mDisplay.mActiveEditText = MatrixEditText.this;
            if (getText().toString().equals(Logic.NAN)) {
                setText("");
            }
        }
    }

    @Override
    public String toString() {
        try {
            return getText().toString();
        } catch (ClassCastException e) {
            return "";
        }
    }

    @Override
    public View focusSearch(int direction) {
        switch (direction) {
            case View.FOCUS_FORWARD:
                return mParent.nextView(this);
            case View.FOCUS_BACKWARD:
                return mParent.previousView(this);
        }
        return super.focusSearch(direction);
    }

    public MatrixView getMatrixView() {
        return mParent;
    }

    class MatrixKeyListener extends NumberKeyListener {
        @Override
        public int getInputType() {
            return EditorInfo.TYPE_CLASS_NUMBER | InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS;
        }

        @Override
        protected char[] getAcceptedChars() {
            return ACCEPTED_CHARS;
        }

        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            return null;
        }

        @Override
        public boolean onKeyDown(View view, Editable content, int keyCode, KeyEvent event) {
            if (keyCode == KeyEvent.KEYCODE_DEL) {
                if (mParent.isEmpty()) mDisplay.removeView(mParent);
            }
            return super.onKeyDown(view, content, keyCode, event);
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
