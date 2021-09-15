package com.foyer.hbc.presentation.stats

import kotlinx.coroutines.flow.StateFlow

interface StatsContract {

    interface ViewModel {
        val state: StateFlow<StatisticState>
        fun getData()
    }
}