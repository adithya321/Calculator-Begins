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

import android.widget.BaseAdapter;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Vector;

public class History {
    private static final int VERSION_1 = 1;
    private static final int MAX_ENTRIES = 100;
    Vector<HistoryEntry> mEntries = new Vector<HistoryEntry>();
    int mPos;
    BaseAdapter mObserver;

    History() {
        clear();
    }

    History(int version, DataInput in) throws IOException {
        if (version >= VERSION_1) {
            int size = in.readInt();
            for (int i = 0; i < size; ++i) {
                mEntries.add(new HistoryEntry(version, in));
            }
            mPos = in.readInt();
        } else {
            throw new IOException("invalid version " + version);
        }
    }

    void clear() {
        mEntries.clear();
        mEntries.add(new HistoryEntry("", ""));
        mPos = 0;
        notifyChanged();
    }

    private void notifyChanged() {
        if (mObserver != null) {
            mObserver.notifyDataSetChanged();
        }
    }

    void setObserver(BaseAdapter observer) {
        mObserver = observer;
    }

    void write(DataOutput out) throws IOException {
        out.writeInt(mEntries.size());
        for (HistoryEntry entry : mEntries) {
            entry.write(out);
        }
        out.writeInt(mPos);
    }

    void update(String text) {
        current().setEdited(text);
    }

    HistoryEntry current() {
        return mEntries.elementAt(mPos);
    }

    boolean moveToPrevious() {
        if (mPos > 0) {
            --mPos;
            return true;
        }
        return false;
    }

    boolean moveToNext() {
        if (mPos < mEntries.size() - 1) {
            ++mPos;
            return true;
        }
        return false;
    }

    void enter(String base, String edited) {
        current().clearEdited();
        if (mEntries.size() >= MAX_ENTRIES) {
            mEntries.remove(0);
        }
        if ((mEntries.size() < 2 || !base.equals(mEntries.elementAt(mEntries.size() - 2).getBase())) && !base.isEmpty() && !edited.isEmpty()) {
            mEntries.insertElementAt(new HistoryEntry(base, edited), mEntries.size() - 1);
        }
        mPos = mEntries.size() - 1;
        notifyChanged();
    }

    String getText() {
        return current().getEdited();
    }

    String getBase() {
        return current().getBase();
    }

    public void remove(HistoryEntry he) {
        mEntries.remove(he);
        mPos--;
    }
}
