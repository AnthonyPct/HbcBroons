package com.example.hbc.presentation.stats

import com.example.hbc.base.BaseFragment
import com.example.hbc.databinding.FragmentStatsBinding

class StatisticFragment: BaseFragment<FragmentStatsBinding>() {

    override fun getViewBinding(): FragmentStatsBinding {
        return FragmentStatsBinding.inflate(layoutInflater)
    }
}
