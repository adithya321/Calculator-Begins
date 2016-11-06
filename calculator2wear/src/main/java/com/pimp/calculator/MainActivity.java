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

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ViewSwitcher;

import com.pimp.calculator.view.BackspaceImageButton;
import com.pimp.calculator.view.FormattedNumberEditText;
import com.xlythe.math.Constants;
import com.xlythe.math.EquationFormatter;
import com.xlythe.math.History;
import com.xlythe.math.HistoryEntry;
import com.xlythe.math.Persist;
import com.xlythe.math.Solver;

public class MainActivity extends Activity {

    /**
     * Constant for an invalid resource id.
     */
    public static final int INVALID_RES_ID = -1;

    // Calc logic
    private View.OnClickListener mListener;
    private ViewSwitcher mDisplay;
    private BackspaceImageButton mDelete;
    private ViewPager mPager;
    private Persist mPersist;
    private History mHistory;
    private CalculatorExpressionTokenizer mTokenizer;
    private CalculatorExpressionEvaluator mEvaluator;
    private State mState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Rebuild constants. If the user changed their locale, it won't kill the app
        // but it might change a decimal point from . to ,
        Constants.rebuildConstants();

        mTokenizer = new CalculatorExpressionTokenizer(this);
        mEvaluator = new CalculatorExpressionEvaluator(mTokenizer);

        mPager = (ViewPager) findViewById(R.id.panelswitch);

        mPersist = new Persist(this);
        mPersist.load();

        mHistory = mPersist.getHistory();

        mDisplay = (ViewSwitcher) findViewById(R.id.display);
        for (int i = 0; i < mDisplay.getChildCount(); i++) {
            final FormattedNumberEditText displayChild = (FormattedNumberEditText) mDisplay.getChildAt(i);
            displayChild.setSolver(mEvaluator.getSolver());
        }

        mDelete = (BackspaceImageButton) findViewById(R.id.delete);
        mListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.delete:
                        if (mDelete.getState() == BackspaceImageButton.State.CLEAR) {
                            mDisplay.showNext();
                            onClear();
                        } else {
                            onDelete();
                        }
                        break;
                    case R.id.eq:
                        mEvaluator.evaluate(getActiveEditText().getCleanText(), new CalculatorExpressionEvaluator.EvaluateCallback() {
                            @Override
                            public void onEvaluate(String expr, String result, int errorResourceId) {
                                mDisplay.showNext();
                                if (errorResourceId != MainActivity.INVALID_RES_ID) {
                                    onError(errorResourceId);
                                } else {
                                    setText(result);
                                }
                                if (saveHistory(expr, result)) {
                                    RecyclerView history = (RecyclerView) findViewById(R.id.history);
                                    history.getLayoutManager().scrollToPosition(history.getAdapter().getItemCount() - 1);
                                }
                            }
                        });
                        break;
                    case R.id.parentheses:
                        setText("(" + getActiveEditText().getText() + ")");
                        break;
                    default:
                        if (((Button) v).getText().toString().length() >= 2) {
                            onInsert(((Button) v).getText().toString() + "(");
                        } else {
                            onInsert(((Button) v).getText().toString());
                        }
                        break;
                }
            }
        };
        mDelete.setOnClickListener(mListener);
        mDelete.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (mDelete.getState() == BackspaceImageButton.State.DELETE) {
                    mDisplay.showNext();
                    onClear();
                    return true;
                }

                return false;
            }
        });


        HistoryAdapter.HistoryItemCallback historyItemCallback = new HistoryAdapter.HistoryItemCallback() {
            @Override
            public void onHistoryItemSelected(HistoryEntry entry) {
                setState(State.DELETE);
                getActiveEditText().insert(entry.getResult());
            }
        };
        final CalculatorPageAdapter adapter = new CalculatorPageAdapter(
                getBaseContext(), mListener, historyItemCallback, mEvaluator.getSolver(), mHistory);
        mPager.setAdapter(adapter);
        mPager.setCurrentItem(1);
        mPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            private int mActivePage = -1;

            @Override
            public void onPageScrolled(int i, float v, int i1) {
                // We're scrolling, so enable everything
                if (mActivePage != -1) {
                    mActivePage = -1;
                    setActivePage(mActivePage);
                }
            }

            @Override
            public void onPageSelected(int i) {
                // We've landed on a page, so disable all pages but this one
                mActivePage = i;
                setActivePage(mActivePage);
            }

            @Override
            public void onPageScrollStateChanged(int i) {
                // We've landed on a page (possibly the current page) so disable all pages but this one
                if (mActivePage == -1) {
                    mActivePage = mPager.getCurrentItem();
                    setActivePage(mActivePage);
                }
            }

            private void setActivePage(int page) {
                for (int i = 0; i < adapter.getCount(); i++) {
                    adapter.setEnabled(adapter.getViewAt(i), page == -1 || i == page);
                }
            }
        });

        setState(State.DELETE);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mPersist.save();
    }

    private void onDelete() {
        setState(State.DELETE);
        getActiveEditText().backspace();
    }

    private void onClear() {
        setState(State.DELETE);
        getActiveEditText().clear();
    }

    private void setText(String text) {
        setState(State.CLEAR);
        getActiveEditText().setText(text);
    }

    private void onInsert(String text) {
        if (mState == State.ERROR || (mState == State.CLEAR && !Solver.isOperator(text))) {
            setText(text);
        } else {
            getActiveEditText().insert(text);
        }

        setState(State.DELETE);
    }

    private void onError(int resId) {
        setState(State.ERROR);
        getActiveEditText().setText(resId);
    }

    private void setState(State state) {
        mDelete.setState(state == State.DELETE ? BackspaceImageButton.State.DELETE : BackspaceImageButton.State.CLEAR);
        if (mState != state) {
            switch (state) {
                case CLEAR:
                    getActiveEditText().setTextColor(getResources().getColor(R.color.display_formula_text_color));
                    break;
                case DELETE:
                    getActiveEditText().setTextColor(getResources().getColor(R.color.display_formula_text_color));
                    break;
                case ERROR:
                    getActiveEditText().setTextColor(getResources().getColor(R.color.calculator_error_color));
                    break;
            }
            mState = state;
        }
    }

    private FormattedNumberEditText getActiveEditText() {
        return (FormattedNumberEditText) mDisplay.getCurrentView();
    }

    protected boolean saveHistory(String expr, String result) {
        if (mHistory == null) {
            return false;
        }

        if (!TextUtils.isEmpty(expr)
                && !TextUtils.isEmpty(result)
                && !Solver.equal(expr, result)
                && (mHistory.current() == null || !mHistory.current().getFormula().equals(expr))) {
            expr = EquationFormatter.appendParenthesis(expr);
            expr = Solver.clean(expr);
            expr = mTokenizer.getLocalizedExpression(expr);
            mHistory.enter(expr, result);
            return true;
        }
        return false;
    }

    private enum State {
        DELETE, CLEAR, ERROR
    }
}
