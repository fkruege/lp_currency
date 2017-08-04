package com.franctan.lonelyplanetcurrencyguide.models


data class CurrencyValueModel(
        val currency: String = DEFAULT_CURRENCY,
        val value: Double = DEFAULT_VALUE
) {
    fun valueAsString(): String {
        return String.format("%.2f", value)
    }
}