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

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class HistoryEntry {
    private static final int VERSION_1 = 1;
    private String mBase;
    private String mEdited;

    HistoryEntry(String base, String edited) {
        mBase = base;
        mEdited = edited;
    }

    HistoryEntry(int version, DataInput in) throws IOException {
        if (version >= VERSION_1) {
            mBase = in.readUTF();
            mEdited = in.readUTF();
        } else {
            throw new IOException("invalid version " + version);
        }
    }

    void write(DataOutput out) throws IOException {
        out.writeUTF(mBase);
        out.writeUTF(mEdited);
    }

    @Override
    public String toString() {
        return mBase;
    }

    void clearEdited() {
        mEdited = mBase;
    }

    public String getEdited() {
        return mEdited;
    }

    void setEdited(String edited) {
        mEdited = edited;
    }

    public String getBase() {
        return mBase;
    }
}
