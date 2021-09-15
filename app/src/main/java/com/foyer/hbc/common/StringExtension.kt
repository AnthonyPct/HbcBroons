package com.foyer.hbc.common

import java.util.*


fun String.withCurrency(): String {
    val currency = Currency.getInstance(Locale.FRANCE)
    return this.plus(currency.symbol)
}
