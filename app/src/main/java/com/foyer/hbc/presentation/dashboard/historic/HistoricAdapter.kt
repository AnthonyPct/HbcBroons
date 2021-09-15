package com.foyer.hbc.presentation.dashboard.historic

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.foyer.hbc.domain.data.dashboard.HistoricItem
import com.foyer.hbc.presentation.dashboard.IDashboardContract

class HistoricAdapter(val listener: IDashboardContract.ViewEvent.Historic) :
    ListAdapter<HistoricItem, RecyclerView.ViewHolder>(HistoricDiffUtilCallback()) {

    ///////////////////////////////////////////////////////////////////////////
    // CONST
    ///////////////////////////////////////////////////////////////////////////

    companion object {
        private const val ITEM_HEADER_TYPE = 0
        private const val ITEM_DATE_TYPE = 1
        private const val ITEM_CONSUMPTION_TYPE = 2
        private const val ITEM_FOOTER_TYPE = 3
    }

    ///////////////////////////////////////////////////////////////////////////
    // DATA
    ///////////////////////////////////////////////////////////////////////////

    private var data: List<HistoricItem> = emptyList()

    ///////////////////////////////////////////////////////////////////////////
    // SPECIALIZATION
    ///////////////////////////////////////////////////////////////////////////

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            ITEM_HEADER_TYPE -> HistoricHeaderViewHolder.from(parent)
            ITEM_DATE_TYPE -> HistoricDateViewHolder.from(parent)
            ITEM_CONSUMPTION_TYPE -> HistoricConsumptionViewHolder.from(parent)
            else -> HistoricFooterViewHolder.from(parent)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is HistoricConsumptionViewHolder -> {
                val consumptionItem = getItem(position) as HistoricItem.ConsumptionItem
                holder.bind(consumption = consumptionItem.consumption, listener)
            }
            is HistoricDateViewHolder -> {
                val dateItem = getItem(position) as HistoricItem.DateItem
                holder.bind(dateItem.date)
            }
            is HistoricFooterViewHolder -> {
                holder.bind(listener)
            }
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is HistoricItem.Header -> ITEM_HEADER_TYPE
            is HistoricItem.DateItem -> ITEM_DATE_TYPE
            is HistoricItem.ConsumptionItem -> ITEM_CONSUMPTION_TYPE
            is HistoricItem.Footer -> ITEM_FOOTER_TYPE
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // PUBLIC API
    ///////////////////////////////////////////////////////////////////////////

    fun setItems(items: List<HistoricItem>) {
        this.data = items
        submitList(items)
    }

    ///////////////////////////////////////////////////////////////////////////
    // DiffUtil
    ///////////////////////////////////////////////////////////////////////////

    class HistoricDiffUtilCallback : DiffUtil.ItemCallback<HistoricItem>() {
        override fun areItemsTheSame(oldItem: HistoricItem, newItem: HistoricItem): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: HistoricItem, newItem: HistoricItem): Boolean {
            return oldItem == newItem
        }
    }
}
