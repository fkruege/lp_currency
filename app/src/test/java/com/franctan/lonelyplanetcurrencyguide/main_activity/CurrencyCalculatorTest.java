package com.franctan.lonelyplanetcurrencyguide.main_activity;

import com.franctan.lonelyplanetcurrencyguide.models.CurrencyValueModel;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class CurrencyCalculatorTest {

    CurrencyCalculator calculator;

    @Before
    public void before() {
        calculator = new CurrencyCalculator();
    }


    @Test
    public void calculateFromBase() throws Exception {
        CurrencyValueModel baseCurrency = new CurrencyValueModel("USD", 1.00);
        CurrencyValueModel otherCurrency = new CurrencyValueModel("PHP", 50.00);
        assertEquals(500.00, calculator.calculateFromBase(baseCurrency, otherCurrency, 10.00), 0);
    }

    @Test
    public void calculateToBase() throws Exception {
        CurrencyValueModel baseCurrency = new CurrencyValueModel("USD", 1.00);
        CurrencyValueModel otherCurrency = new CurrencyValueModel("PHP", 50.00);
        assertEquals(10.00, calculator.calculateToBase(baseCurrency, otherCurrency, 500.00), 0);
    }


    @Test
    public void areEqual_True() throws Exception {
        assertTrue(calculator.areEqual(1.00, 1.00));
        assertTrue(calculator.areEqual(1.0001, 1.0001));
        assertTrue(calculator.areEqual(1.000001, 1.000001));
        assertTrue(calculator.areEqual(1.12345, 1.123456));
    }


    @Test
    public void areEqual_False() throws Exception {
        assertFalse(calculator.areEqual(1.02, 1.01));
        assertFalse(calculator.areEqual(1.021, 1.022));
    }

}