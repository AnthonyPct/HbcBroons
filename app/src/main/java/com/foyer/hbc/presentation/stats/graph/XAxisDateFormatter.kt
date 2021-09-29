package com.foyer.hbc.presentation.stats.graph

import com.github.mikephil.charting.formatter.ValueFormatter
import java.text.SimpleDateFormat
import java.util.*

class XAxisDateFormatter : ValueFormatter() {

    ///////////////////////////////////////////////////////////////////////////
    // DATA
    ///////////////////////////////////////////////////////////////////////////
    private val mFormat: SimpleDateFormat =
        SimpleDateFormat("dd MMM", Locale.FRANCE)

    ///////////////////////////////////////////////////////////////////////////
    // SPECIALIZATION
    ///////////////////////////////////////////////////////////////////////////

    override fun getFormattedValue(value: Float): String {
        val millis = value.toLong()
        return mFormat.format(Date(millis))
    }
}