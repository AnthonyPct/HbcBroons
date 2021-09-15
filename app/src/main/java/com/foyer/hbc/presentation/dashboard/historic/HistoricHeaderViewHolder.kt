package com.foyer.hbc.presentation.dashboard.historic

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.foyer.hbc.databinding.HistoricHeaderItemBinding

class HistoricHeaderViewHolder(val binding: HistoricHeaderItemBinding) :
    RecyclerView.ViewHolder(binding.root) {

    ///////////////////////////////////////////////////////////////////////////
    // FACTORY
    ///////////////////////////////////////////////////////////////////////////

    companion object {
        fun from(parent: ViewGroup): HistoricHeaderViewHolder {
            val binding =
                HistoricHeaderItemBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            return HistoricHeaderViewHolder(binding)
        }
    }
}
