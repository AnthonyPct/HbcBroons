package com.foyer.hbc.domain.data.dashboard

import com.foyer.hbc.domain.model.ConsumptionEntity
import java.util.*

sealed class HistoricItem {
    //  When the adapter uses DiffUtil to determine whether and how an item has changed,
    //  the DiffItemCallback needs to know the id of each item.
    abstract val id: Long

    object Header : HistoricItem() {
        override val id: Long
            get() = Long.MIN_VALUE
    }

    data class DateItem(
        val date: Date,
        override val id: Long = date.time
    ) : HistoricItem()

    data class ConsumptionItem(
        val consumption: ConsumptionEntity,
        override val id: Long = consumption.id.toLong()
    ) : HistoricItem()

    object Footer : HistoricItem() {
        override val id: Long
            get() = Long.MAX_VALUE
    }
}
