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

import android.content.Context;
import android.content.res.Resources;
import android.view.KeyEvent;
import android.widget.EditText;

import com.pimp.calculator.FloatingView.CalculatorDisplay;
import com.pimp.calculator.FloatingView.CalculatorDisplay.Scroll;
import com.pimp.calculator.FloatingView.GraphView;
import com.pimp.calculator.math.BaseModule;
import com.pimp.calculator.math.EquationFormatter;
import com.pimp.calculator.math.GraphModule.OnGraphUpdatedListener;
import com.pimp.calculator.math.Point;
import com.pimp.calculator.math.Solver;

import org.javia.arity.SyntaxException;

import java.text.DecimalFormatSymbols;
import java.util.List;

public class Logic {
    // Double.toString() for NaN
    public static final String NAN = "NaN";
    public static final char MINUS = '\u2212';
    public static final String MARKER_EVALUATE_ON_RESUME = "?";
    public static final int DELETE_MODE_BACKSPACE = 0;
    public static final int DELETE_MODE_CLEAR = 1;
    final String mErrorString;
    private final Context mContext;
    private final Solver mSolver = new Solver();
    private final CalculatorExpressionTokenizer mTokenizer;
    int mDeleteMode = DELETE_MODE_BACKSPACE;
    CalculatorDisplay mDisplay;
    GraphView mGraphView;
    String mResult = "";
    boolean mIsError = false;
    int mLineLength = 0;
    EquationFormatter mEquationFormatter;
    private History mHistory;
    private Graph mGraph;
    private Listener mListener;
    private OnGraphUpdatedListener mOnGraphUpdateListener = new OnGraphUpdatedListener() {
        @Override
        public void onGraphUpdated(List<Point> result) {
            mGraph.setData(result);
        }
    };

    public Logic(Context context) {
        this(context, null);
    }

    Logic(Context context, CalculatorDisplay display) {
        final Resources r = context.getResources();
        DecimalFormatSymbols dfs = new DecimalFormatSymbols();
        mContext = context.getApplicationContext();
        mErrorString = r.getString(R.string.error);

        mEquationFormatter = new EquationFormatter();
        mTokenizer = new CalculatorExpressionTokenizer(context);
        mDisplay = display;
        if (mDisplay != null) mDisplay.setLogic(this);
    }

    public static boolean isOperator(String text) {
        return text.length() == 1 && isOperator(text.charAt(0));
    }

    static boolean isPostFunction(String text) {
        return text.length() == 1 && isPostFunction(text.charAt(0));
    }

    static boolean isOperator(char c) {
        // plus minus times div
        return "+\u2212\u00d7\u00f7/*^".indexOf(c) != -1;
    }

    static boolean isPostFunction(char c) {
        // exponent, factorial, percent
        return "^!%".indexOf(c) != -1;
    }

    public void setHistory(History history) {
        mHistory = history;
    }

    public void setGraphDisplay(GraphView graphView) {
        mGraphView = graphView;
    }

    public void setGraph(Graph graph) {
        mGraph = graph;
    }

    public void setListener(Listener listener) {
        this.mListener = listener;
    }

    void setLineLength(int nDigits) {
        mLineLength = nDigits;
    }

    void insert(String delta) {
        if (!acceptInsert(delta)) {
            clear(true);
        }
        mDisplay.insert(delta);
        setDeleteMode(DELETE_MODE_BACKSPACE);
        graph();
    }

    boolean acceptInsert(String delta) {
        if (mIsError || getText().equals(mErrorString)) {
            return false;
        }
        if (getDeleteMode() == DELETE_MODE_BACKSPACE || isOperator(delta) || isPostFunction(delta)) {
            return true;
        }

        EditText editText = mDisplay.getActiveEditText();
        int editLength = editText == null ? 0 : editText.getText().length();

        return mDisplay.getSelectionStart() != editLength;
    }

    public int getDeleteMode() {
        return mDeleteMode;
    }

    public void setDeleteMode(int mode) {
        if (mDeleteMode != mode) {
            mDeleteMode = mode;
            if (mListener != null) mListener.onDeleteModeChange();
        }
    }

    public String getText() {
        return mDisplay.getText();
    }

    void setText(String text) {
        clear(false);
        mDisplay.insert(text);
        if (text.equals(mErrorString)) setDeleteMode(DELETE_MODE_CLEAR);
    }

    public void onTextChanged() {
        setDeleteMode(DELETE_MODE_BACKSPACE);
    }

    public void resumeWithHistory() {
        clearWithHistory(false);
    }

    private void clearWithHistory(boolean scroll) {
        String text = mHistory.getText();
        if (MARKER_EVALUATE_ON_RESUME.equals(text)) {
            if (!mHistory.moveToPrevious()) {
                text = "";
            }
            text = mHistory.getBase();
            evaluateAndShowResult(text, CalculatorDisplay.Scroll.NONE);
        } else {
            mResult = "";
            mDisplay.setText(text, scroll ? CalculatorDisplay.Scroll.UP : CalculatorDisplay.Scroll.NONE);
            mIsError = false;
        }
    }

    public String convertToDecimal(String text) {
        try {
            return mSolver.convertToDecimal(text);
        } catch (SyntaxException e) {
            return mErrorString;
        }
    }

    public String evaluate(String text) {
        try {
            return mTokenizer.getLocalizedExpression(mSolver.solve(mTokenizer.getNormalizedExpression(text)));
        } catch (SyntaxException e) {
            return mErrorString;
        }
    }

    public void evaluateAndShowResult(String text, Scroll scroll) {
        try {
            String result = mTokenizer.getLocalizedExpression(mSolver.solve(mTokenizer.getNormalizedExpression(text)));
            if (!text.equals(result)) {
                mHistory.enter(EquationFormatter.appendParenthesis(text), result);
                mResult = result;
                mDisplay.setText(mResult, scroll);
                setDeleteMode(DELETE_MODE_CLEAR);
            }
        } catch (SyntaxException e) {
            mIsError = true;
            mResult = mErrorString;
            mDisplay.setText(mResult, scroll);
            setDeleteMode(DELETE_MODE_CLEAR);
        }
    }

    private void clear(boolean scroll) {
        mHistory.enter("", "");
        mDisplay.setText("", scroll ? CalculatorDisplay.Scroll.UP : CalculatorDisplay.Scroll.NONE);
        cleared();
    }

    void cleared() {
        mResult = "";
        mIsError = false;
        updateHistory();

        setDeleteMode(DELETE_MODE_BACKSPACE);
    }

    void onDelete() {
        if (getText().equals(mResult) || mIsError) {
            clear(false);
        } else {
            mDisplay.dispatchKeyEvent(new KeyEvent(0, KeyEvent.KEYCODE_DEL));
            mResult = "";
        }
        graph();
    }

    void onClear() {
        clear(mDeleteMode == DELETE_MODE_CLEAR);
        graph();
    }

    public void onEnter() {
        if (mDeleteMode == DELETE_MODE_CLEAR) {
            clearWithHistory(false); // clear after an Enter on result
        } else {
            evaluateAndShowResult(getText(), CalculatorDisplay.Scroll.UP);
        }
    }

    void onUp() {
        if (mHistory.moveToPrevious()) {
            mDisplay.setText(mHistory.getText(), CalculatorDisplay.Scroll.DOWN);
        }
    }

    void onDown() {
        if (mHistory.moveToNext()) {
            mDisplay.setText(mHistory.getText(), CalculatorDisplay.Scroll.UP);
        }
    }

    void updateHistory() {
        String text = getText();
        mHistory.update(text);
    }

    public boolean isError() {
        return getText().equals(mErrorString);
    }

    public Context getContext() {
        return mContext;
    }

    public void setDomain(float min, float max) {
        mSolver.getGraphModule().setDomain(min, max);
    }

    public void setRange(float min, float max) {
        mSolver.getGraphModule().setRange(min, max);
    }

    public void setZoomLevel(float level) {
        mSolver.getGraphModule().setZoomLevel(level);
    }

    public void graph() {
        mSolver.getGraphModule().updateGraph(getText(), mOnGraphUpdateListener);
    }

    public BaseModule getBaseModule() {
        return mSolver.getBaseModule();
    }

    public interface Listener {
        void onDeleteModeChange();
    }
}
