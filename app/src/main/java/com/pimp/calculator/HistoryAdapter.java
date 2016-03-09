package com.pimp.calculator;

import android.content.Context;
import android.text.Html;
import android.text.Spanned;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.pimp.calculator.FloatingView.HistoryLine;
import com.pimp.calculator.math.EquationFormatter;

import java.util.Vector;

class HistoryAdapter extends BaseAdapter {
    private final Context mContext;
    private final Vector<HistoryEntry> mEntries;
    private final EquationFormatter mEquationFormatter;
    private final History mHistory;

    HistoryAdapter(Context context, History history) {
        mContext = context;
        mEntries = history.mEntries;
        mEquationFormatter = new EquationFormatter();
        mHistory = history;
    }

    @Override
    public int getCount() {
        return mEntries.size() - 1;
    }

    @Override
    public Object getItem(int position) {
        return mEntries.elementAt(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        HistoryLine view;
        if (convertView == null) {
            view = createView();
        } else {
            view = (HistoryLine) convertView;
        }
        HistoryEntry entry = mEntries.elementAt(position);
        view.setHistoryEntry(entry);
        view.setHistory(mHistory);
        view.setAdapter(this);
        updateView(entry, view);

        return view;
    }

    protected HistoryLine createView() {
        return (HistoryLine) View.inflate(getContext(), R.layout.history_entry, null);
    }

    protected void updateView(HistoryEntry entry, HistoryLine view) {
        TextView expr = (TextView) view.findViewById(R.id.historyExpr);
        TextView result = (TextView) view.findViewById(R.id.historyResult);

        expr.setText(formatText(entry.getBase()));
        result.setText(entry.getEdited());
    }

    public Context getContext() {
        return mContext;
    }

    protected Spanned formatText(String text) {
        return Html.fromHtml(mEquationFormatter.insertSupScripts(text));
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}
