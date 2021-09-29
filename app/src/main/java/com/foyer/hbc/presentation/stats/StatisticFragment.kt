package com.foyer.hbc.presentation.stats

import android.view.View
import androidx.lifecycle.lifecycleScope
import com.foyer.hbc.base.BaseFragment
import com.foyer.hbc.databinding.FragmentStatsBinding
import com.foyer.hbc.presentation.stats.graph.XAxisDateFormatter
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
//                        setConsoDataChart(it.data)
                    }
                    is StatisticState.Loading -> {
                        // TODO
                    }
                    is StatisticState.UserSuccess -> {
                        binding?.podiumComponent?.initPodium(it.bestUser)
                    }
                }
            }
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // HELPER
    ///////////////////////////////////////////////////////////////////////////

    private fun setConsoDataChart(entry: List<Entry>) {
        val dataSet = LineDataSet(entry, "Nombre de conso")
        binding?.consoGraph?.data = LineData(dataSet)
        binding?.consoGraph?.invalidate()
    }

    private fun initConsoGraphUI() {
        binding?.consoGraph?.run {
            visibility = View.GONE // TODO
            setTouchEnabled(true)
            xAxis.valueFormatter = XAxisDateFormatter()
            xAxis.position = XAxis.XAxisPosition.BOTTOM
        }
    }

    // TODO : juste un podium ? et je pense possible de faire l'evolution de la
//  cagnotte en prenant les consos et les paiements groupé par date
}
