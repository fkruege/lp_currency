package com.franctan.lonelyplanetcurrencyguide.main_activity;


import com.franctan.lonelyplanetcurrencyguide.models.CurrencyValueModel;

import java.math.BigDecimal;
import java.math.RoundingMode;

import javax.inject.Inject;

public class CurrencyCalculator {

    @Inject
    public CurrencyCalculator() {

    }

    public double calculateFromBase(CurrencyValueModel baseCurrency, CurrencyValueModel otherCurrency, double value) {
        double conversion = baseCurrency.getValue() * otherCurrency.getValue();
        return roundIt(conversion * value);
    }

    double calculateToBase(CurrencyValueModel baseCurrency, CurrencyValueModel otherCurrency, double value) {
        double conversion = baseCurrency.getValue() / otherCurrency.getValue();
        return roundIt(conversion * value);
    }

    boolean areEqual(double currency1, double currency2) {
        return Math.abs(currency1 - currency2) <= 0.001;
    }

    private double roundIt(double value) {
        return new BigDecimal(value).setScale(2, RoundingMode.HALF_UP).doubleValue();
    }

    public boolean dummyMock(){
       return true;
    }


}
