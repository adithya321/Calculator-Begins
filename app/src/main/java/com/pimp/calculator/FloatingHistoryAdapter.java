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

package com.pimp.calculator;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.pimp.calculator.FloatingView.HistoryLine;

class FloatingHistoryAdapter extends HistoryAdapter {
    private OnHistoryItemClickListener mListener;

    FloatingHistoryAdapter(Context context, History history) {
        super(context, history);
    }

    @Override
    protected HistoryLine createView() {
        HistoryLine v = (HistoryLine) View.inflate(getContext(), R.layout.floating_history_entry, null);
        return v;
    }

    @Override
    protected void updateView(final HistoryEntry entry, HistoryLine view) {
        TextView expr = (TextView) view.findViewById(R.id.historyExpr);
        TextView result = (TextView) view.findViewById(R.id.historyResult);

        expr.setText(formatText(entry.getBase()));
        result.setText(entry.getEdited());

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) mListener.onHistoryItemClick(entry);
                // copyContent(entry.getResult());
            }
        });
    }

    private void copyContent(String text) {
        ClipboardManager clipboard = (ClipboardManager) getContext().getSystemService(Context.CLIPBOARD_SERVICE);
        clipboard.setPrimaryClip(ClipData.newPlainText(null, text));
        String toastText = getContext().getResources().getString(R.string.text_copied_toast);
        Toast.makeText(getContext(), toastText, Toast.LENGTH_SHORT).show();
    }

    public void setOnHistoryItemClickListener(OnHistoryItemClickListener l) {
        mListener = l;
    }

    public static interface OnHistoryItemClickListener {
        public void onHistoryItemClick(HistoryEntry entry);
    }
}
