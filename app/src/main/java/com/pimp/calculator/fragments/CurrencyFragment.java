package com.pimp.calculator.fragments;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.pimp.calculator.R;
import com.pimp.calculator.util.AutoResizeTextView;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Document;

import java.net.URL;
import java.net.URLConnection;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class CurrencyFragment extends Fragment {

    private final static String URL_WEB_SERVICE = "http://www.webservicex.net/CurrencyConvertor.asmx/ConversionRate?";
    ConversionRate conversionRate;
    String strJson = "{\n" +
            "  \"disclaimer\": \"Exchange rates provided for informational purposes only and do not constitute financial advice of any kind. Although every attempt is made to ensure quality, no guarantees are made of accuracy, validity, availability, or fitness for any purpose. All usage subject to acceptance of Terms: https://openexchangerates.org/terms/\",\n" +
            "  \"license\": \"Data sourced from various providers; resale prohibited; no warranties given of any kind. All usage subject to License Agreement: https://openexchangerates.org/license/\",\n" +
            "  \"timestamp\": 1457506812,\n" +
            "  \"base\": \"USD\",\n" +
            "  \"rates\": {\n" +
            "    \"AED\": 3.67308,\n" +
            "    \"AFN\": 68.550002,\n" +
            "    \"ALL\": 125.5185,\n" +
            "    \"AMD\": 490.715003,\n" +
            "    \"ANG\": 1.7888,\n" +
            "    \"AOA\": 158.762001,\n" +
            "    \"ARS\": 15.44117,\n" +
            "    \"AUD\": 1.346252,\n" +
            "    \"AWG\": 1.793333,\n" +
            "    \"AZN\": 1.633013,\n" +
            "    \"BAM\": 1.780128,\n" +
            "    \"BBD\": 2,\n" +
            "    \"BDT\": 78.47035,\n" +
            "    \"BGN\": 1.780196,\n" +
            "    \"BHD\": 0.377031,\n" +
            "    \"BIF\": 1550.342476,\n" +
            "    \"BMD\": 1,\n" +
            "    \"BND\": 1.384915,\n" +
            "    \"BOB\": 6.841428,\n" +
            "    \"BRL\": 3.759866,\n" +
            "    \"BSD\": 1,\n" +
            "    \"BTC\": 0.002437157884,\n" +
            "    \"BTN\": 67.403266,\n" +
            "    \"BWP\": 11.181213,\n" +
            "    \"BYR\": 21057.2,\n" +
            "    \"BZD\": 1.994308,\n" +
            "    \"CAD\": 1.341066,\n" +
            "    \"CDF\": 928.5,\n" +
            "    \"CHF\": 0.997325,\n" +
            "    \"CLF\": 0.024602,\n" +
            "    \"CLP\": 681.788405,\n" +
            "    \"CNY\": 6.51546,\n" +
            "    \"COP\": 3179.216631,\n" +
            "    \"CRC\": 534.884904,\n" +
            "    \"CUC\": 1,\n" +
            "    \"CUP\": 1.000313,\n" +
            "    \"CVE\": 100.254333,\n" +
            "    \"CZK\": 24.62057,\n" +
            "    \"DJF\": 177.750001,\n" +
            "    \"DKK\": 6.792586,\n" +
            "    \"DOP\": 45.82298,\n" +
            "    \"DZD\": 108.70402,\n" +
            "    \"EEK\": 14.225,\n" +
            "    \"EGP\": 7.830066,\n" +
            "    \"ERN\": 14.9985,\n" +
            "    \"ETB\": 21.3459,\n" +
            "    \"EUR\": 0.911047,\n" +
            "    \"FJD\": 2.10105,\n" +
            "    \"FKP\": 0.704937,\n" +
            "    \"GBP\": 0.704937,\n" +
            "    \"GEL\": 2.461325,\n" +
            "    \"GGP\": 0.704937,\n" +
            "    \"GHS\": 3.861264,\n" +
            "    \"GIP\": 0.704937,\n" +
            "    \"GMD\": 39.44254,\n" +
            "    \"GNF\": 7640.785049,\n" +
            "    \"GTQ\": 7.693508,\n" +
            "    \"GYD\": 206.263336,\n" +
            "    \"HKD\": 7.766368,\n" +
            "    \"HNL\": 22.61067,\n" +
            "    \"HRK\": 6.893175,\n" +
            "    \"HTG\": 61.855575,\n" +
            "    \"HUF\": 282.3218,\n" +
            "    \"IDR\": 13165.233333,\n" +
            "    \"ILS\": 3.911822,\n" +
            "    \"IMP\": 0.704937,\n" +
            "    \"INR\": 67.40129,\n" +
            "    \"IQD\": 1088.549988,\n" +
            "    \"IRR\": 30210.5,\n" +
            "    \"ISK\": 128.768,\n" +
            "    \"JEP\": 0.704937,\n" +
            "    \"JMD\": 121.2817,\n" +
            "    \"JOD\": 0.70858,\n" +
            "    \"JPY\": 112.6507,\n" +
            "    \"KES\": 101.62083,\n" +
            "    \"KGS\": 72.871649,\n" +
            "    \"KHR\": 3996.987549,\n" +
            "    \"KMF\": 444.837979,\n" +
            "    \"KPW\": 900.09,\n" +
            "    \"KRW\": 1212.96499,\n" +
            "    \"KWD\": 0.300738,\n" +
            "    \"KYD\": 0.823567,\n" +
            "    \"KZT\": 345.474492,\n" +
            "    \"LAK\": 8126.177451,\n" +
            "    \"LBP\": 1508.333317,\n" +
            "    \"LKR\": 144.925801,\n" +
            "    \"LRD\": 84.66847,\n" +
            "    \"LSL\": 15.436988,\n" +
            "    \"LTL\": 3.093658,\n" +
            "    \"LVL\": 0.632815,\n" +
            "    \"LYD\": 1.39008,\n" +
            "    \"MAD\": 9.841558,\n" +
            "    \"MDL\": 19.96134,\n" +
            "    \"MGA\": 3211.891634,\n" +
            "    \"MKD\": 56.05591,\n" +
            "    \"MMK\": 1222.222476,\n" +
            "    \"MNT\": 2043.833333,\n" +
            "    \"MOP\": 7.997486,\n" +
            "    \"MRO\": 342.834333,\n" +
            "    \"MTL\": 0.683602,\n" +
            "    \"MUR\": 35.774188,\n" +
            "    \"MVR\": 15.186667,\n" +
            "    \"MWK\": 719.877703,\n" +
            "    \"MXN\": 17.90745,\n" +
            "    \"MYR\": 4.125071,\n" +
            "    \"MZN\": 49.68,\n" +
            "    \"NAD\": 15.44019,\n" +
            "    \"NGN\": 199.022001,\n" +
            "    \"NIO\": 28.35058,\n" +
            "    \"NOK\": 8.567653,\n" +
            "    \"NPR\": 107.7816,\n" +
            "    \"NZD\": 1.486386,\n" +
            "    \"OMR\": 0.38496,\n" +
            "    \"PAB\": 1,\n" +
            "    \"PEN\": 3.459293,\n" +
            "    \"PGK\": 3.067025,\n" +
            "    \"PHP\": 46.93386,\n" +
            "    \"PKR\": 104.7022,\n" +
            "    \"PLN\": 3.93626,\n" +
            "    \"PYG\": 5697.57,\n" +
            "    \"QAR\": 3.641204,\n" +
            "    \"RON\": 4.065134,\n" +
            "    \"RSD\": 112.372599,\n" +
            "    \"RUB\": 72.0337,\n" +
            "    \"RWF\": 763.203256,\n" +
            "    \"SAR\": 3.750248,\n" +
            "    \"SBD\": 7.956749,\n" +
            "    \"SCR\": 13.426693,\n" +
            "    \"SDG\": 6.100245,\n" +
            "    \"SEK\": 8.499592,\n" +
            "    \"SGD\": 1.38599,\n" +
            "    \"SHP\": 0.704937,\n" +
            "    \"SLL\": 4074.5,\n" +
            "    \"SOS\": 613.217994,\n" +
            "    \"SRD\": 3.9925,\n" +
            "    \"STD\": 22262.5,\n" +
            "    \"SVC\": 8.741125,\n" +
            "    \"SYP\": 219.863334,\n" +
            "    \"SZL\": 15.41859,\n" +
            "    \"THB\": 35.36544,\n" +
            "    \"TJS\": 7.8696,\n" +
            "    \"TMT\": 3.501633,\n" +
            "    \"TND\": 2.038336,\n" +
            "    \"TOP\": 2.254746,\n" +
            "    \"TRY\": 2.917061,\n" +
            "    \"TTD\": 6.547645,\n" +
            "    \"TWD\": 32.88292,\n" +
            "    \"TZS\": 2186.98165,\n" +
            "    \"UAH\": 26.26046,\n" +
            "    \"UGX\": 3372.106667,\n" +
            "    \"USD\": 1,\n" +
            "    \"UYU\": 32.19072,\n" +
            "    \"UZS\": 2861.319947,\n" +
            "    \"VEF\": 6.31701,\n" +
            "    \"VND\": 22285.6,\n" +
            "    \"VUV\": 112.613749,\n" +
            "    \"WST\": 2.542555,\n" +
            "    \"XAF\": 597.466371,\n" +
            "    \"XAG\": 0.0652065,\n" +
            "    \"XAU\": 0.000798,\n" +
            "    \"XCD\": 2.70102,\n" +
            "    \"XDR\": 0.719411,\n" +
            "    \"XOF\": 599.041431,\n" +
            "    \"XPD\": 0.001763,\n" +
            "    \"XPF\": 108.544663,\n" +
            "    \"XPT\": 0.001011,\n" +
            "    \"YER\": 214.89,\n" +
            "    \"ZAR\": 15.44784,\n" +
            "    \"ZMK\": 5252.024745,\n" +
            "    \"ZMW\": 11.374763,\n" +
            "    \"ZWL\": 322.387247\n" +
            "  }\n" +
            "}";
    private AutoResizeTextView result_TV, resultant_TV;
    private Spinner fro, too;

    public static android.support.v4.app.Fragment newInstance() {
        return new CurrencyFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_currency, container, false);
        result_TV = (AutoResizeTextView) root.findViewById(R.id.curr_result_TV);
        resultant_TV = (AutoResizeTextView) root.findViewById(R.id.curr_resultant_TV);
        conversionRate = new ConversionRate();

        return root;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        fro = (Spinner) view.findViewById(R.id.fro_spinner);
        too = (Spinner) view.findViewById(R.id.too_spinner);

        ArrayAdapter<String> froAdapter = new ArrayAdapter<String>(getActivity(),
                R.layout.spinner_item, getResources().getStringArray(R.array.currency_exp));
        froAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);

        fro.setAdapter(froAdapter);
        too.setAdapter(froAdapter);
        fro.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int numFromCurrency = fro.getSelectedItemPosition();
                int numToCurrency = too.getSelectedItemPosition();
                if (numFromCurrency != numToCurrency)
                    getRate();
                else {
                    resultant_TV.setText("1");
                    SharedPreferences settings = getActivity().getSharedPreferences("MyPrefs", 0);
                    SharedPreferences.Editor editor = settings.edit();
                    editor.putFloat("rate", 1);
                    editor.apply();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        too.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int numFromCurrency = fro.getSelectedItemPosition();
                int numToCurrency = too.getSelectedItemPosition();
                if (numFromCurrency != numToCurrency)
                    getRate();
                else {
                    resultant_TV.setText("1");
                    SharedPreferences settings = getActivity().getSharedPreferences("MyPrefs", 0);
                    SharedPreferences.Editor editor = settings.edit();
                    editor.putFloat("rate", 1);
                    editor.apply();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private String connection(String str) {
        String result = null;
        try {
            URL url = new URL(str);
            URLConnection connection = url.openConnection();
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(connection.getInputStream());
            result = doc.getDocumentElement().getTextContent();
        } catch (Exception ex) {
        }
        return result;
    }

    private void getRate() {
        conversionRate = new ConversionRate();
        conversionRate.execute(getUrl());
    }

    private String checkConversionResult(String str) {
        return (str.equals("-1") || str.equals("") || str == null) ? getString(R.string.no_data) : str;
    }

    private String getUrl() {
        StringBuilder url = new StringBuilder();

        url.append(URL_WEB_SERVICE);

        int numFromCurrency = fro.getSelectedItemPosition();
        int numToCurrency = too.getSelectedItemPosition();

        url.append("FromCurrency=").append(getResources().getStringArray(R.array.currency_exp)[numFromCurrency].substring(0, 3));
        url.append("&ToCurrency=").append(getResources().getStringArray(R.array.currency_exp)[numToCurrency].substring(0, 3));
        Log.e("URL", url.toString());

        return url.toString();
    }

    private class ConversionRate extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            return connection(params[0]);
        }

        @Override
        protected void onPostExecute(String str) {
            try {
                result_TV.setText("1");
                resultant_TV.setText(String.valueOf(checkConversionResult(str)));
                SharedPreferences settings = getActivity().getSharedPreferences("MyPrefs", 0);
                SharedPreferences.Editor editor = settings.edit();
                editor.putFloat("rate", Float.parseFloat(checkConversionResult(str)));
                editor.apply();
            } catch (Exception ex) {
                String data = "";
                int numFromCurrency = fro.getSelectedItemPosition();
                int numToCurrency = too.getSelectedItemPosition();
                try {
                    JSONObject jsonRootObject = new JSONObject(strJson);
                    JSONObject jsonObject = jsonRootObject.optJSONObject("rates");

                    float From = Float.parseFloat(jsonObject.optString(getResources().getStringArray(R.array.currency_exp)[numFromCurrency].substring(0, 3)));
                    float To = Float.parseFloat(jsonObject.optString(getResources().getStringArray(R.array.currency_exp)[numToCurrency].substring(0, 3)));
                    float rate = To / From;
                    result_TV.setText("1");
                    resultant_TV.setText(String.valueOf(rate));
                    SharedPreferences settings = getActivity().getSharedPreferences("MyPrefs", 0);
                    SharedPreferences.Editor editor = settings.edit();
                    editor.putFloat("rate", rate);
                    editor.apply();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        protected void onPreExecute() {
            Toast toast = Toast.makeText(getActivity(), "Loading", Toast.LENGTH_SHORT);
            toast.show();
            super.onPreExecute();
        }
    }
}