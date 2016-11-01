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
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pimp.calculator.math.Constants;
import com.pimp.calculator.math.EquationFormatter;
import com.pimp.calculator.math.History;
import com.pimp.calculator.math.HistoryEntry;
import com.pimp.calculator.math.Solver;

import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {
    private final Context mContext;
    private final Solver mSolver;
    private final List<HistoryEntry> mEntries;
    private final EquationFormatter mEquationFormatter;
    protected HistoryItemCallback mCallback;

    public HistoryAdapter(Context context, Solver solver, History history, HistoryItemCallback callback) {
        mContext = context;
        mSolver = solver;
        mEntries = history.getEntries();
        mEquationFormatter = new EquationFormatter();
        mCallback = callback;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(getContext()).inflate(getLayoutResourceId(), parent, false);
        return new ViewHolder(view);
    }

    protected int getLayoutResourceId() {
        return R.layout.history_entry;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final HistoryEntry entry = getEntry(position);
        holder.historyExpr.setText(formatText(entry.getFormula()));
        holder.historyResult.setText(formatText(entry.getResult()));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallback.onHistoryItemSelected(entry);
            }
        });
    }

    private HistoryEntry getEntry(int position) {
        if (position < 0 || position >= mEntries.size()) {
            return null;
        }

        return mEntries.get(position);
    }

    @Override
    public int getItemCount() {
        return mEntries.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    protected Spanned formatText(String text) {
        if (text == null) {
            return null;
        }

        if (text.matches(".*\\de[-" + Constants.MINUS + "]?\\d.*")) {
            text = text.replace("e", Constants.MUL + "10^");
        }
        return Html.fromHtml(
                mEquationFormatter.insertSupScripts(
                        mEquationFormatter.addComas(mSolver, text)));
    }

    public Context getContext() {
        return mContext;
    }

    public interface HistoryItemCallback {
        void onHistoryItemSelected(HistoryEntry entry);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView historyExpr;
        public TextView historyResult;

        public ViewHolder(View v) {
            super(v);
            historyExpr = (TextView) v.findViewById(R.id.historyExpr);
            historyResult = (TextView) v.findViewById(R.id.historyResult);
        }
    }
}
