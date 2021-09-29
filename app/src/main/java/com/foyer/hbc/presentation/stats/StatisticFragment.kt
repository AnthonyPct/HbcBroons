package com.foyer.hbc.presentation.stats

import androidx.lifecycle.lifecycleScope
import com.foyer.hbc.base.BaseFragment
import com.foyer.hbc.databinding.FragmentStatsBinding
import com.foyer.hbc.presentation.stats.graph.XAxisDateFormatter
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel


class StatisticFragment : BaseFragment<FragmentStatsBinding>() {

    ///////////////////////////////////////////////////////////////////////////
    // CONFIGURATION
    ///////////////////////////////////////////////////////////////////////////

    override fun getViewBinding(): FragmentStatsBinding {
        return FragmentStatsBinding.inflate(layoutInflater)
    }

    ///////////////////////////////////////////////////////////////////////////
    // DEPENDENCY
    ///////////////////////////////////////////////////////////////////////////

    private val statsViewModel: StatsViewModel by viewModel()

    ///////////////////////////////////////////////////////////////////////////
    // LIFECYCLE
    ///////////////////////////////////////////////////////////////////////////

    override fun initData() {
        statsViewModel.getData()
    }

    override fun initUI() {
        initConsoGraphUI()
    }

    override fun collectData() {
        lifecycleScope.launch {
            statsViewModel.state.collect {
                when (it) {
                    is StatisticState.Success -> {
                        binding?.podiumComponent?.initPodium(it.bestUser)
                        setConsoDataChart(it.data)
                    }
                    is StatisticState.Loading -> {
                        // TODO
                    }
                    else -> {

                    }
                }
            }
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // HELPER
    ///////////////////////////////////////////////////////////////////////////

    private fun setConsoDataChart(entry: List<Entry>) {
        val dataSet = LineDataSet(entry, "Evolution du nombre de conso totals")
        binding?.moneyGraph?.xAxis?.setLabelCount(entry.size, true)
        binding?.moneyGraph?.data = LineData(dataSet)
        binding?.moneyGraph?.notifyDataSetChanged()
        binding?.moneyGraph?.invalidate()
    }

    private fun initConsoGraphUI() {
        binding?.moneyGraph?.run {
            setTouchEnabled(true)
            setPinchZoom(false)
            isDoubleTapToZoomEnabled = false
            xAxis.valueFormatter = XAxisDateFormatter()
            xAxis.setDrawGridLines(false)
            xAxis.position = XAxis.XAxisPosition.BOTTOM
            axisLeft.axisMinimum = 0f
            axisLeft.setDrawZeroLine(false)
            axisLeft.setDrawGridLines(false)
            axisRight.isEnabled = false
            description = Description().apply {
                text = ""
            }
        }
    }
}
