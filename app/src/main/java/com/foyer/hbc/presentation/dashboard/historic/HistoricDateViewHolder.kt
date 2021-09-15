package com.foyer.hbc.presentation.dashboard.historic

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.foyer.hbc.common.getHistoricDateFormat
import com.foyer.hbc.databinding.HistoricDateItemBinding
import java.util.*

class HistoricDateViewHolder(private val binding: HistoricDateItemBinding) :
    RecyclerView.ViewHolder(binding.root) {

    ///////////////////////////////////////////////////////////////////////////
    // FACTORY
    ///////////////////////////////////////////////////////////////////////////

    companion object {
        fun from(parent: ViewGroup): HistoricDateViewHolder {
            val binding =
                HistoricDateItemBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            return HistoricDateViewHolder(binding)
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // PUBLIC API
    ///////////////////////////////////////////////////////////////////////////

    fun bind(date: Date) {
        binding.historicDate.text = date.getHistoricDateFormat()
    }
}
