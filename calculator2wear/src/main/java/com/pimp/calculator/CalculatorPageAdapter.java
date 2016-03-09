package com.pimp.calculator;

import android.content.Context;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import com.pimp.calculator.math.Constants;
import com.pimp.calculator.math.History;
import com.pimp.calculator.math.Solver;
import com.pimp.calculator.view.SolidLayout;
import com.pimp.calculator.view.SolidPadLayout;

public class CalculatorPageAdapter extends PagerAdapter {
    private final Context mContext;
    private final View.OnClickListener mListener;
    private final HistoryAdapter.HistoryItemCallback mHistoryCallback;
    private final Solver mSolver;
    private final History mHistory;
    private final View[] mViews = new View[3];

    public CalculatorPageAdapter(
            Context context,
            View.OnClickListener listener,
            HistoryAdapter.HistoryItemCallback historyCallback,
            Solver solver,
            History history) {
        mContext = context;
        mListener = listener;
        mHistoryCallback = historyCallback;
        mSolver = solver;
        mHistory = history;
    }

    protected Context getContext() {
        return mContext;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public void startUpdate(ViewGroup container) {
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View v = getViewAt(position);
        container.addView(v);

        return v;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        if (mViews[position] != null) mViews[position] = null;
        container.removeView((View) object);
    }

    @Override
    public void finishUpdate(ViewGroup container) {
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Parcelable saveState() {
        return null;
    }

    @Override
    public void restoreState(Parcelable state, ClassLoader loader) {
    }

    public View getViewAt(int position) {
        if (mViews[position] != null) return mViews[position];
        switch (position) {
            case 0:
                mViews[position] = View.inflate(mContext, R.layout.calculator_history, null);
                RecyclerView historyView =
                        (RecyclerView) mViews[position].findViewById(R.id.history);
                setUpHistory(historyView);

                // This is the first time loading the history panel -- disable it until the user moves to it
                setEnabled(mViews[position], false);
                break;
            case 1:
                mViews[position] = View.inflate(mContext, R.layout.calculator_basic, null);

                Button dot = (Button) mViews[position].findViewById(R.id.dec_point);
                dot.setText(String.valueOf(Constants.DECIMAL_POINT));

                break;
            case 2:
                mViews[position] = View.inflate(mContext, R.layout.calculator_advanced, null);

                // This is the first time loading the advanced panel -- disable it until the user moves to it
                setEnabled(mViews[position], false);
                break;
        }
        applyListener(mViews[position]);
        return mViews[position];
    }

    private void applyListener(View view) {
        if (view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                applyListener(((ViewGroup) view).getChildAt(i));
            }
        } else if (view instanceof Button) {
            view.setOnClickListener(mListener);
        } else if (view instanceof ImageButton) {
            view.setOnClickListener(mListener);
        }
    }

    protected void setEnabled(View view, boolean enabled) {
        if (view instanceof SolidLayout) {
            ((SolidLayout) view).setPreventChildTouchEvents(!enabled);
        } else if (view instanceof SolidPadLayout) {
            ((SolidPadLayout) view).setPreventChildTouchEvents(!enabled);
        } else if (view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                setEnabled(((ViewGroup) view).getChildAt(i), enabled);
            }
        }
    }

    private void setUpHistory(RecyclerView historyView) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        historyView.setLayoutManager(layoutManager);

        final HistoryAdapter historyAdapter = new HistoryAdapter(mContext, mSolver, mHistory, mHistoryCallback);
        mHistory.setObserver(new History.Observer() {
            @Override
            public void notifyDataSetChanged() {
                historyAdapter.notifyDataSetChanged();
            }
        });
        historyView.setAdapter(historyAdapter);

        layoutManager.scrollToPosition(historyAdapter.getItemCount() - 1);
    }
}
