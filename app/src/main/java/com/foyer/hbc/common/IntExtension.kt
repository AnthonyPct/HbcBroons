package com.foyer.hbc.common

fun Double.withCurrency(): String {
    return this.toString().withCurrency()
}
