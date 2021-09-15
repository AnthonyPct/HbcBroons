package com.foyer.hbc.domain.usecase.dashboard

import com.foyer.hbc.common.witNoHour
import com.foyer.hbc.data.database.DatabaseRepository
import com.foyer.hbc.domain.data.dashboard.HistoricItem
import com.foyer.hbc.domain.model.ConsumptionEntity
import com.foyer.hbc.domain.model.UserEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import java.util.*

class GetHistoricItemUseCase(private val databaseRepository: DatabaseRepository) {

    companion object {
        private const val LIMIT_DASHBOARD_CONSUMPTIONS_DISPLAYED = 25
    }

    ///////////////////////////////////////////////////////////////////////////
    // PUBLIC API
    ///////////////////////////////////////////////////////////////////////////

    fun execute(): Flow<List<HistoricItem>> {
        return databaseRepository.getConsumptions()
            .map { getConsumptionsByDate(it) }
            .map { getHistoricItems(it) }
            .flowOn(Dispatchers.IO)
    }

    fun execute(user: UserEntity): Flow<List<HistoricItem>> {
        return databaseRepository.getUserConsumptions(user)
            .map { getConsumptionsByDate(it) }
            .map { getHistoricItems(it, false) }
            .flowOn(Dispatchers.IO)
    }

    ///////////////////////////////////////////////////////////////////////////
    // HELPER
    ///////////////////////////////////////////////////////////////////////////

    private fun getConsumptionsByDate(consumption: List<ConsumptionEntity>):
            Map<Date, List<ConsumptionEntity>> {
        return consumption
            .sortedByDescending { it.date }
            .groupBy { it.date.witNoHour() }
    }

    private fun getHistoricItems(
        consumptions: Map<Date, List<ConsumptionEntity>>,
        showFooter: Boolean = true
    ): List<HistoricItem> {
        val result = mutableListOf<HistoricItem>()
        result.add(HistoricItem.Header)
        return if (consumptions.isEmpty()) {
            result
        } else {
            consumptions.map {
                result.add(HistoricItem.DateItem(it.key))
                it.value.map { consumption -> result.add(HistoricItem.ConsumptionItem(consumption)) }
            }
            if (showFooter && consumptions.size > LIMIT_DASHBOARD_CONSUMPTIONS_DISPLAYED) {
                result.add(HistoricItem.Footer)
            }
            result
        }
    }
}
