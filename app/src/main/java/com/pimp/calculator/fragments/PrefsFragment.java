package com.pimp.calculator.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceActivity;

import com.pimp.calculator.MainActivity;
import com.pimp.calculator.R;

public class PrefsFragment extends PreferenceActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.prefs);
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
        super.onBackPressed();
    }
}