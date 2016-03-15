package com.pimp.calculator.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pimp.calculator.R;

public class ExtrasFragment extends Fragment {

    public ExtrasFragment() {

    }

    public static Fragment newInstance() {
        return new ExtrasFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_extras, container, false);
    }
}
