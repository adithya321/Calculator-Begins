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

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.MenuItem;

import com.xlythe.engine.theme.Theme;

public class BaseActivity extends FragmentActivity {
    private boolean mIsSwitchingActivities = false;

    @Override
    protected void onCreate(Bundle state) {
        super.onCreate(state);

        // Update com.xlythe.engine.xlythe.engine (as needed)
        Theme.buildResourceMap(R.class);
        Theme.setPackageName(CalculatorSettings.getTheme(this));
    }

    @Override
    public void onPause() {
        super.onPause();
        Intent serviceIntent = new Intent(this, FloatingCalculator.class);
        if (CalculatorSettings.floatingCalculator(this)) {
            // Start Floating Calc service if not up yet
            if (!mIsSwitchingActivities) {
                startService(serviceIntent);
            }
        }
        mIsSwitchingActivities = false;
    }

    @Override
    public void onResume() {
        super.onResume();
        // Kill floating calc (if exists)
        Intent serviceIntent = new Intent(this, FloatingCalculator.class);
        stopService(serviceIntent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Intent i = getPackageManager().getLaunchIntentForPackage(getPackageName());
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
        mIsSwitchingActivities = intent.getComponent() != null && getPackageName().equals(intent.getComponent().getPackageName());
    }
}
