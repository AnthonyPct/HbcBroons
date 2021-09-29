package com.foyer.hbc.domain.usecase.stats

import com.foyer.hbc.data.database.DatabaseRepository
import com.github.mikephil.charting.data.Entry
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetConsumptionsEntryUseCase(private val databaseRepository: DatabaseRepository) {

    ///////////////////////////////////////////////////////////////////////////
    // PUBLIC API
    ///////////////////////////////////////////////////////////////////////////

    fun execute(): Flow<List<Entry>> {
        return databaseRepository.getConsumptions()
            .map { it.groupBy { it.date } }
            .map {
                it.map {
                    Entry(it.key.time.toFloat(), it.value.sumOf { it.amount }.toFloat())
                }
            }
    }
}