package com.foyer.hbc.presentation.match

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.foyer.hbc.databinding.ItemMatchBinding
import com.foyer.hbc.domain.model.MatchEntity

class MatchsAdapter(val listener: MatchContract.ViewEvent) :
    RecyclerView.Adapter<MatchViewHolder>() {

    ///////////////////////////////////////////////////////////////////////////
    // DATA
    ///////////////////////////////////////////////////////////////////////////

    private var matchs = emptyList<MatchEntity>()
    private var oldMatchs = matchs

    ///////////////////////////////////////////////////////////////////////////
    // PUBLIC API
    ///////////////////////////////////////////////////////////////////////////

    fun setData(matchs: List<MatchEntity>) {
        this.matchs = matchs
        notifyDataSetChanged()
    }

    ///////////////////////////////////////////////////////////////////////////
    // SPECIALIZATION
    ///////////////////////////////////////////////////////////////////////////

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MatchViewHolder {
        val binding =
            ItemMatchBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MatchViewHolder(binding, listener)
    }

    override fun onBindViewHolder(holder: MatchViewHolder, position: Int) {
        holder.bind(matchs[position])
    }

    override fun getItemCount(): Int {
        return matchs.size
    }
}