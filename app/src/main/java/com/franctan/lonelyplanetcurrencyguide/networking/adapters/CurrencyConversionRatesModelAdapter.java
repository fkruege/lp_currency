package com.franctan.lonelyplanetcurrencyguide.networking.adapters;


import com.franctan.lonelyplanetcurrencyguide.models.CurrencyConversionRatesModel;
import com.franctan.lonelyplanetcurrencyguide.networking.generated_models.CurrencyRates;
import com.squareup.moshi.FromJson;
import com.squareup.moshi.ToJson;

import android.util.Log;

import java.sql.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class CurrencyConversionRatesModelAdapter {

    private static final String TAG = CurrencyConversionRatesModelAdapter.class.getSimpleName();

    @FromJson
    CurrencyConversionRatesModel ratesFromJson(CurrencyRates currencyRatesJson) {
        Log.d(TAG, currencyRatesJson.toString());

        String baseCurrency = currencyRatesJson.getBase();
        Date currencyAsOfDate = Date.valueOf(currencyRatesJson.getDate());
        HashMap<String, Double> ratesMap = new HashMap<>();

        Map<String, Double> jsonMap = (Map<String, Double>) currencyRatesJson.getRates();

        Set<Map.Entry<String, Double>> entries = jsonMap.entrySet();

        for(Map.Entry<String, Double> entry : entries){
            ratesMap.put(entry.getKey(), entry.getValue());
        }

        return new CurrencyConversionRatesModel(baseCurrency, currencyAsOfDate, ratesMap);

    }

    @ToJson
    String jsonToRatesModel(CurrencyConversionRatesModel model) {
        return "";
    }


}
