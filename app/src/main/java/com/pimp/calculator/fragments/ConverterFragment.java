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

package com.pimp.calculator.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.pimp.calculator.R;

public class ConverterFragment extends Fragment {

    public ConverterFragment() {
    }

    public static android.support.v4.app.Fragment newInstance() {
        return new ConverterFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_converter, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final Spinner unitSpinner = (Spinner) view.findViewById(R.id.units_spinner);
        String[] unitsArray = {"Area", "Cooking", "Digital Storage", "Energy", "Fuel Consumption",
                "Length / Distance", "Mass / Weight", "Power", "Pressure", "Speed",
                "Time", "Torque", "Volume"};
        final Spinner from = (Spinner) view.findViewById(R.id.from_spinner);
        final Spinner to = (Spinner) view.findViewById(R.id.to_spinner);

        final String[] areaHeaders = {getResources().getString(R.string.sq_kilometre),
                getResources().getString(R.string.sq_metre),
                getResources().getString(R.string.sq_centimetre),
                getResources().getString(R.string.hectare),
                getResources().getString(R.string.sq_mile),
                getResources().getString(R.string.sq_yard),
                getResources().getString(R.string.sq_foot),
                getResources().getString(R.string.sq_inch),
                getResources().getString(R.string.acre)};

        final String[] cookingHeaders = {getResources().getString(R.string.teaspoon),
                getResources().getString(R.string.tablespoon),
                getResources().getString(R.string.cup),
                getResources().getString(R.string.fluid_ounce),
                getResources().getString(R.string.fluid_ounce_uk),
                getResources().getString(R.string.pint),
                getResources().getString(R.string.pint_uk),
                getResources().getString(R.string.quart),
                getResources().getString(R.string.quart_uk),
                getResources().getString(R.string.gallon),
                getResources().getString(R.string.gallon_uk),
                getResources().getString(R.string.millilitre),
                getResources().getString(R.string.litre)};

        final String[] digitalHeaders = {getResources().getString(R.string.bit),
                getResources().getString(R.string.Byte),
                getResources().getString(R.string.kilobit),
                getResources().getString(R.string.kilobyte),
                getResources().getString(R.string.megabit),
                getResources().getString(R.string.megabyte),
                getResources().getString(R.string.gigabit),
                getResources().getString(R.string.gigabyte),
                getResources().getString(R.string.terabit),
                getResources().getString(R.string.terabyte)};

        final String[] energyHeaders = {getResources().getString(R.string.joule),
                getResources().getString(R.string.kilojoule),
                getResources().getString(R.string.calorie),
                getResources().getString(R.string.kilocalorie),
                getResources().getString(R.string.btu),
                getResources().getString(R.string.ft_lbF),
                getResources().getString(R.string.in_lbF),
                getResources().getString(R.string.kilowatt_hour),
                getResources().getString(R.string.electron_volt)};

        final String[] fuelHeaders = {getResources().getString(R.string.mpg_us),
                getResources().getString(R.string.mpg_uk),
                getResources().getString(R.string.l_100k),
                getResources().getString(R.string.km_l),
                getResources().getString(R.string.miles_l)};

        final String[] lengthHeaders = {getResources().getString(R.string.kilometre),
                getResources().getString(R.string.mile),
                getResources().getString(R.string.metre),
                getResources().getString(R.string.centimetre),
                getResources().getString(R.string.millimetre),
                getResources().getString(R.string.micrometre),
                getResources().getString(R.string.nanometre),
                getResources().getString(R.string.yard),
                getResources().getString(R.string.feet),
                getResources().getString(R.string.feet_inch),
                getResources().getString(R.string.inch),
                getResources().getString(R.string.nautical_mile),
                getResources().getString(R.string.furlong),
                getResources().getString(R.string.light_year)};

        final String[] massHeaders = {getResources().getString(R.string.kilogram),
                getResources().getString(R.string.pound),
                getResources().getString(R.string.gram),
                getResources().getString(R.string.milligram),
                getResources().getString(R.string.ounce),
                getResources().getString(R.string.grain),
                getResources().getString(R.string.stone),
                getResources().getString(R.string.metric_ton),
                getResources().getString(R.string.short_ton),
                getResources().getString(R.string.long_ton)};

        final String[] powerHeaders = {getResources().getString(R.string.watt),
                getResources().getString(R.string.kilowatt),
                getResources().getString(R.string.megawatt),
                getResources().getString(R.string.hp),
                getResources().getString(R.string.hp_uk),
                getResources().getString(R.string.ft_lbf_s),
                getResources().getString(R.string.calorie_s),
                getResources().getString(R.string.btu_s),
                getResources().getString(R.string.kva),
                getResources().getString(R.string.electron_volt)};

        final String[] pressureHeaders = {getResources().getString(R.string.megapascal),
                getResources().getString(R.string.kilopascal),
                getResources().getString(R.string.pascal),
                getResources().getString(R.string.bar),
                getResources().getString(R.string.psi),
                getResources().getString(R.string.psf),
                getResources().getString(R.string.atmosphere),
                getResources().getString(R.string.technical_atmosphere),
                getResources().getString(R.string.mmhg),
                getResources().getString(R.string.torr)};

        final String[] speedHeaders = {getResources().getString(R.string.km_h),
                getResources().getString(R.string.mph),
                getResources().getString(R.string.m_s),
                getResources().getString(R.string.fps),
                getResources().getString(R.string.knot)};

        final String[] timeHeaders = {getResources().getString(R.string.year),
                getResources().getString(R.string.month),
                getResources().getString(R.string.week),
                getResources().getString(R.string.day),
                getResources().getString(R.string.hour),
                getResources().getString(R.string.minute),
                getResources().getString(R.string.second),
                getResources().getString(R.string.millisecond),
                getResources().getString(R.string.nanosecond)};

        final String[] torqueHeaders = {getResources().getString(R.string.n_m),
                getResources().getString(R.string.ft_lbF),
                getResources().getString(R.string.in_lbF)};

        final String[] volumeHeaders = {getResources().getString(R.string.teaspoon),
                getResources().getString(R.string.tablespoon),
                getResources().getString(R.string.cup),
                getResources().getString(R.string.fluid_ounce),
                getResources().getString(R.string.fluid_ounce_uk),
                getResources().getString(R.string.pint),
                getResources().getString(R.string.pint_uk),
                getResources().getString(R.string.quart),
                getResources().getString(R.string.quart_uk),
                getResources().getString(R.string.gallon),
                getResources().getString(R.string.gallon_uk),
                getResources().getString(R.string.barrel),
                getResources().getString(R.string.barrel_uk),
                getResources().getString(R.string.millilitre),
                getResources().getString(R.string.litre),
                getResources().getString(R.string.cubic_cm),
                getResources().getString(R.string.cubic_m),
                getResources().getString(R.string.cubic_inch),
                getResources().getString(R.string.cubic_foot),
                getResources().getString(R.string.cubic_yard)};

        ArrayAdapter<String> adapterUnits = new ArrayAdapter<String>(getActivity(),
                R.layout.spinner_item, unitsArray);
        adapterUnits.setDropDownViewResource(R.layout.spinner_dropdown_item);
        unitSpinner.setAdapter(adapterUnits);
        unitSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        setUnitsAdapter(areaHeaders, from, to);
                        break;
                    case 1:
                        setUnitsAdapter(cookingHeaders, from, to);
                        break;
                    case 2:
                        setUnitsAdapter(digitalHeaders, from, to);
                        break;
                    case 3:
                        setUnitsAdapter(energyHeaders, from, to);
                        break;
                    case 4:
                        setUnitsAdapter(fuelHeaders, from, to);
                        break;
                    case 5:
                        setUnitsAdapter(lengthHeaders, from, to);
                        break;
                    case 6:
                        setUnitsAdapter(massHeaders, from, to);
                        break;
                    case 7:
                        setUnitsAdapter(powerHeaders, from, to);
                        break;
                    case 8:
                        setUnitsAdapter(pressureHeaders, from, to);
                        break;
                    case 9:
                        setUnitsAdapter(speedHeaders, from, to);
                        break;
                    case 10:
                        setUnitsAdapter(timeHeaders, from, to);
                        break;
                    case 11:
                        setUnitsAdapter(torqueHeaders, from, to);
                        break;
                    case 12:
                        setUnitsAdapter(volumeHeaders, from, to);
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    public void setUnitsAdapter(String[] headers, final Spinner from, final Spinner to) {
        ArrayAdapter<String> adapterUnits = new ArrayAdapter<String>(getActivity(),
                R.layout.spinner_item, headers);
        adapterUnits.setDropDownViewResource(R.layout.spinner_dropdown_item);
        from.setAdapter(adapterUnits);
        to.setAdapter(adapterUnits);
    }
}