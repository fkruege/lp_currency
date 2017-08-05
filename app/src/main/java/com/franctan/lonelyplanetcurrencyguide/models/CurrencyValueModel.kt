package com.franctan.lonelyplanetcurrencyguide.models

import java.math.RoundingMode
import java.text.DecimalFormat


data class CurrencyValueModel(
        val currency: String = DEFAULT_CURRENCY,
        val value: Double = DEFAULT_VALUE
) {
    fun valueAsString(): String {
        val df: DecimalFormat = DecimalFormat("0.00")
        df.roundingMode = RoundingMode.DOWN
        return df.format(value)
    }

    fun setValue(newValue: Double): CurrencyValueModel {
        return this.copy(value = newValue)
    }
}