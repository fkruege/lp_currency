package com.franctan.lonelyplanetcurrencyguide.main_activity;


import com.franctan.lonelyplanetcurrencyguide.models.CurrencyValueModel;

import javax.inject.Inject;

class CurrencyCalculator {

    @Inject
    CurrencyCalculator() {

    }

    double calculateFromBase(CurrencyValueModel baseCurrency, CurrencyValueModel otherCurrency, double value) {
        double conversion = baseCurrency.getValue() * otherCurrency.getValue();
        return conversion * value;
    }

    double calculateToBase(CurrencyValueModel baseCurrency, CurrencyValueModel otherCurrency, double value) {
        double conversion = baseCurrency.getValue() / otherCurrency.getValue();
        return conversion * value;
    }

    boolean areEqual(double currency1, double currency2) {
        return Math.abs(currency1 - currency2) <= 0.001;
    }



}
