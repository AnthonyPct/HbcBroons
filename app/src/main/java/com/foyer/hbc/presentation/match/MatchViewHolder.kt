package com.foyer.hbc.presentation.match

import android.graphics.Typeface
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.foyer.hbc.R
import com.foyer.hbc.databinding.ItemMatchBinding
import com.foyer.hbc.domain.model.MatchEntity

class MatchViewHolder(
    private val binding: ItemMatchBinding,
    val listener: MatchContract.ViewEvent
) : RecyclerView.ViewHolder(binding.root) {

    ///////////////////////////////////////////////////////////////////////////
    // PUBLIC API
    ///////////////////////////////////////////////////////////////////////////

    fun bind(matchEntity: MatchEntity) {
        with(binding) {
            matchDate.text = if (matchEntity.date?.date != null) {
                binding.root.resources.getString(
                    R.string.matchs_date,
                    matchEntity.date?.date,
                    matchEntity.date?.hour,
                    matchEntity.date?.minute
                )
            } else {
                binding.root.resources.getString(R.string.matchs_date_unkow)
            }
            matchFirstTeam.text = matchEntity.firstTeam
            matchFirstScore.text = matchEntity.firstScore?.toString()
            matchSecondTeam.text = matchEntity.secondTeam
            matchSecondScore.text = matchEntity.secondScore?.toString()
            binding.root.setOnClickListener {
                listener.onClickMatch(matchEntity.pdfPath)
            }
            highlightWinningTeam(matchEntity.firstScore ?: 0, matchEntity.secondScore ?: 0)
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // HELPER
    ///////////////////////////////////////////////////////////////////////////

    private fun highlightWinningTeam(firstScore: Int, secondScore: Int) {
        when {
            firstScore > secondScore -> {
                binding.matchFirstTeam.setTextColor(ContextCompat.getColor(binding.root.context, R.color.black))

            }
            secondScore > firstScore -> {
                binding.matchSecondTeam.setTextColor(ContextCompat.getColor(binding.root.context, R.color.black))
            }
        }
    }
}