package com.foyer.hbc.presentation.dashboard.historic

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.foyer.hbc.databinding.HistoricFooterItemBinding
import com.foyer.hbc.presentation.dashboard.IDashboardContract

class HistoricFooterViewHolder(val binding: HistoricFooterItemBinding) :
    RecyclerView.ViewHolder(binding.root) {

    ///////////////////////////////////////////////////////////////////////////
    // FACTORY
    ///////////////////////////////////////////////////////////////////////////

    companion object {
        fun from(parent: ViewGroup): HistoricFooterViewHolder {
            val binding =
                HistoricFooterItemBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            return HistoricFooterViewHolder(binding)
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // PUBLIC API
    ///////////////////////////////////////////////////////////////////////////

    fun bind(listener: IDashboardContract.ViewEvent.Historic) {
        binding.footerButton.setOnClickListener {
            listener.onClickFooter()
        }
    }
}
