package com.foyer.hbc.presentation.stats

import com.foyer.hbc.base.BaseFragment
import com.foyer.hbc.databinding.FragmentStatsBinding
import com.github.mikephil.charting.components.XAxis

class StatisticFragment : BaseFragment<FragmentStatsBinding>() {

    ///////////////////////////////////////////////////////////////////////////
    // CONFIGURATION
    ///////////////////////////////////////////////////////////////////////////

    override fun getViewBinding(): FragmentStatsBinding {
        return FragmentStatsBinding.inflate(layoutInflater)
    }

    ///////////////////////////////////////////////////////////////////////////
    // LIFECYCLE
    ///////////////////////////////////////////////////////////////////////////

    override fun initData() {
    }

    override fun initUI() {
        binding?.consoGraph?.apply {
            setTouchEnabled(true)
            xAxis.position = XAxis.XAxisPosition.BOTTOM
        }
    }

    override fun collectData() {
    }

    ///////////////////////////////////////////////////////////////////////////
    // HELPER
    ///////////////////////////////////////////////////////////////////////////

}
