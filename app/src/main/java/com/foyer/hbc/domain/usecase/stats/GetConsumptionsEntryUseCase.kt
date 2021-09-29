package com.foyer.hbc.domain.usecase.stats

import com.foyer.hbc.common.witNoHour
import com.foyer.hbc.data.database.DatabaseRepository
import com.github.mikephil.charting.data.Entry
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.zip

class GetConsumptionsEntryUseCase(private val databaseRepository: DatabaseRepository) {

    ///////////////////////////////////////////////////////////////////////////
    // PUBLIC API
    ///////////////////////////////////////////////////////////////////////////

    fun execute(): Flow<List<Entry>> {
        return databaseRepository.getConsumptions()
            .zip(databaseRepository.getAllPayment()) { consumptions, payments ->
                val consumptionsByDate = consumptions
                    .sortedBy { it.date }
                    .groupBy { it.date.witNoHour() }
                var initialAmount = 0F
                var result = mutableListOf<Entry>()
                consumptionsByDate.forEach {
                    val dayConsumptionsPayed = it.value.sumOf { it.price }.toFloat()
                    result.add(
                        Entry(
                            it.key.time.toFloat(),
                            dayConsumptionsPayed + initialAmount
                        )
                    )
                    initialAmount += dayConsumptionsPayed
                }
                result
            }
    }
}