package com.franctan.lonelyplanetcurrencyguide.repositories;


import com.franctan.lonelyplanetcurrencyguide.models.CurrencyConversionRatesModel;
import com.franctan.lonelyplanetcurrencyguide.networking.CurrencyApi;

import javax.inject.Inject;

import io.reactivex.Single;

public class CurrencyRepository {

    private final CurrencyApi restCurrencyApi;

    @Inject
    public CurrencyRepository(CurrencyApi restCurrencyApi) {
        this.restCurrencyApi = restCurrencyApi;
    }

    public Single<CurrencyConversionRatesModel> getConversionRatesFor(String currency) {
        return restCurrencyApi.getConversionRatesFor(currency);
    }
}
