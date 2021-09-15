package com.foyer.hbc.common

import java.text.SimpleDateFormat
import java.util.*

private const val dayAndMonthFormat = "dd/MM"
private const val hourAndMinutesFormat = "HH:mm"
private const val completeDayFormat = "EEEE d MMMM"

fun Date.getHistoricDateFormat(): String {
    val formatter = SimpleDateFormat(dayAndMonthFormat, Locale.FRANCE)
    return formatter.format(this)
}

fun Date.getHourMinutesFormat(): String {
    val formatter = SimpleDateFormat(hourAndMinutesFormat, Locale.FRANCE)
    return formatter.format(this)
}

fun Date.getCompleteDayFormat(): String {
    val formatter = SimpleDateFormat(completeDayFormat, Locale.FRANCE)
    return formatter.format(this)
}

fun Date.witNoHour(): Date {
    val stringDate = this.getHistoricDateFormat()
    return SimpleDateFormat(dayAndMonthFormat, Locale.FRANCE).parse(stringDate) ?: this
}
