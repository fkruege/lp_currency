package com.franctan.lonelyplanetcurrencyguide.models

import java.util.*
import kotlin.collections.HashMap


data class CurrencyConversionRatesModel(
        val baseCurrency: String = "",
        val asOfDate: Date = Date(),
        val ratesMap: HashMap <String, Double> = HashMap()
)