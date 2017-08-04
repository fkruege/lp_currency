package com.franctan.lonelyplanetcurrencyguide.main_activity;


import com.franctan.lonelyplanetcurrencyguide.models.CurrencyValueModel;

class CurrencyEqualizer {

    private final CurrencyValueModel base;

    private CurrencyValueModel primary;

    private CurrencyValueModel secondary;

    private final CurrencyCalculator calculator;

    CurrencyEqualizer(CurrencyValueModel base, CurrencyValueModel primary, CurrencyValueModel secondary, CurrencyCalculator calculator) {
        this.base = base;
        this.primary = primary;
        this.secondary = secondary;
        this.calculator = calculator;
    }

    CurrencyValueModel equalizeUsingPrimary(double newValue) {
        double baseValue = calculator.calculateToBase(base, primary, newValue);
        double newSecondaryValue = calculator.calculateFromBase(base, secondary, baseValue);
        secondary = secondary.copy(secondary.getCurrency(), newSecondaryValue);
        return secondary;
    }

    CurrencyValueModel equalizeUsingSecondary(double newValue) {
        double baseValue = calculator.calculateToBase(base, secondary, newValue);
        double newPrimaryValue = calculator.calculateFromBase(base, primary, baseValue);
        primary = primary.copy(primary.getCurrency(), newPrimaryValue);
        return primary;
    }


}
