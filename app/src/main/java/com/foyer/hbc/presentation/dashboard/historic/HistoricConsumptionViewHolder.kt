package com.foyer.hbc.presentation.dashboard.historic

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.foyer.hbc.common.getHourMinutesFormat
import com.foyer.hbc.common.withCurrency
import com.foyer.hbc.databinding.HistoricConsumptionItemBinding
import com.foyer.hbc.domain.model.ConsumptionEntity
import com.foyer.hbc.presentation.dashboard.IDashboardContract

class HistoricConsumptionViewHolder(private val binding: HistoricConsumptionItemBinding) :
    RecyclerView.ViewHolder(binding.root) {

    ///////////////////////////////////////////////////////////////////////////
    // FACTORY
    ///////////////////////////////////////////////////////////////////////////

    companion object {
        fun from(parent: ViewGroup): HistoricConsumptionViewHolder {
            val binding =
                HistoricConsumptionItemBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            return HistoricConsumptionViewHolder(binding)
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // PUBLIC API
    ///////////////////////////////////////////////////////////////////////////

    fun bind(consumption: ConsumptionEntity, listener: IDashboardContract.ViewEvent.Historic) {
        with(binding) {
            historicDate.text = consumption.date.getHourMinutesFormat()
            historicUser.text = consumption.user
            historicAmount.text = consumption.price.withCurrency()
            historicDate.setOnClickListener {
                listener.onClickConsumption(consumption)
            }
        }
    }
}
