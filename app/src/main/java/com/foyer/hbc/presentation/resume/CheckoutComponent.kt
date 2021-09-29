package com.foyer.hbc.presentation.resume

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.foyer.hbc.common.withCurrency
import com.foyer.hbc.databinding.CheckoutComponentBinding
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter


class CheckoutComponent @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    ///////////////////////////////////////////////////////////////////////////
    // DATA
    ///////////////////////////////////////////////////////////////////////////

    private var binding: CheckoutComponentBinding = CheckoutComponentBinding.inflate(
        LayoutInflater.from(context),
        this,
        true
    )

    ///////////////////////////////////////////////////////////////////////////
    // PUBLIC API
    ///////////////////////////////////////////////////////////////////////////

    fun init(actualAmount: Double, unpayed: Int, estimated: Double) {
        binding.checkoutAmount.text = actualAmount.withCurrency()
        binding.checkoutEstimatedAmount.text = estimated.withCurrency()
        binding.checkoutUnunpayedAmount.text = unpayed.toDouble().withCurrency()
        setGraph(
            mapOf(
                "Impayés" to unpayed.toDouble(),
                "Actuel" to actualAmount
            )
        )
    }

    ///////////////////////////////////////////////////////////////////////////
    // HELPER
    ///////////////////////////////////////////////////////////////////////////

    private fun setGraph(entries: Map<String, Double>) {
        val pieEntries = mutableListOf<PieEntry>()
        val label = mutableListOf<String>()
        val colors = arrayListOf(
            Color.parseColor("#304567"),
            Color.parseColor("#309967"),
            Color.parseColor("#476567")
        )

        entries.forEach {
            label.add(it.key)
            pieEntries.add(PieEntry(it.value.toFloat()))
        }

        val dataSet =
            PieDataSet(pieEntries, "")
                .apply {
                    valueTextSize = 12f
                    this.colors = colors
                }.run {
                    PieData(this).apply {
                        valueFormatter = PercentFormatter()
                    }
                }

        binding.resumeCheckoutChart.apply {
            data = dataSet
            visibility = View.VISIBLE
            invalidate()
        }
    }
}