package com.franctan.lonelyplanetcurrencyguide.networking.generated_models;


import com.squareup.moshi.Json;

public class CurrencyRates {

    @Json(name = "base")
    private String base;
    @Json(name = "date")
    private String date;
    @Json(name = "rates")
    private Object rates;

    public String getBase() {
        return base;
    }

    public void setBase(String base) {
        this.base = base;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Object getRates() {
        return rates;
    }

    public void setRates(Object rates) {
        this.rates = rates;
    }

}
