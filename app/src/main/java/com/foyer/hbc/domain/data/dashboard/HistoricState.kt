package com.foyer.hbc.domain.data.dashboard

sealed class HistoricState {
    object Loading : HistoricState()
    data class Success(val consumptions: List<HistoricItem>) : HistoricState()
    object Error : HistoricState()
}
