package com.foyer.hbc.presentation.stats

import com.foyer.hbc.domain.model.UserEntity
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.Entry

sealed class StatisticState {
    object Loading : StatisticState()
    data class Success(val data: List<Entry>) : StatisticState()
    data class UserSuccess(val bestUser: List<UserEntity>): StatisticState()
}
