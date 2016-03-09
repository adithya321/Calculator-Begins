package com.pimp.calculator.math;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class HistoryEntry {
    private static final int VERSION_1 = 1;
    private static final int VERSION_4 = 4;
    private String mFormula;
    private String mResult;
    private int mGroupId;

    public HistoryEntry(String formula, String result, int groupId) {
        mFormula = formula;
        mResult = result;
        mGroupId = groupId;
    }

    HistoryEntry(int version, DataInput in) throws IOException {
        if (version >= VERSION_1) {
            mFormula = in.readUTF();
            mResult = in.readUTF();
        }
        if (version >= VERSION_4) {
            mGroupId = in.readInt();
        }
    }

    void write(DataOutput out) throws IOException {
        out.writeUTF(mFormula);
        out.writeUTF(mResult);
        out.writeInt(mGroupId);
    }

    @Override
    public String toString() {
        return mFormula;
    }

    public String getResult() {
        return mResult;
    }

    void setResult(String result) {
        mResult = result;
    }

    public String getFormula() {
        return mFormula;
    }

    public int getGroupId() {
        return mGroupId;
    }

    void setGroupId(int groupId) {
        mGroupId = groupId;
    }
}
