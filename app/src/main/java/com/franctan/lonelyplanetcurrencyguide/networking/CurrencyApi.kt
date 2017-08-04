package com.franctan.lonelyplanetcurrencyguide.networking

import com.franctan.lonelyplanetcurrencyguide.models.CurrencyConversionRatesModel
import com.franctan.lonelyplanetcurrencyguide.networking.generated_models.CurrencyRates
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface CurrencyApi{
    // http://api.fixer.io/latest?base=USD
    @GET("latest")
    fun getConversionRatesFor(@Query("base") baseCurrency: String): Single<CurrencyConversionRatesModel>
}


