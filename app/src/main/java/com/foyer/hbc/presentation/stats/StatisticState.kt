package com.foyer.hbc.presentation.stats

sealed class StatisticState {
    object Loading : StatisticState()
    data class Success(val data: List<Entry>) : StatisticState()
}
