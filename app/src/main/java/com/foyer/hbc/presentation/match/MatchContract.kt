package com.foyer.hbc.presentation.match

import com.foyer.hbc.domain.data.matchs.MatchState
import com.foyer.hbc.domain.data.matchs.TEAM
import kotlinx.coroutines.flow.StateFlow

interface MatchContract {

    interface ViewModel {
        val state: StateFlow<MatchState>
        fun getMatchs(team: TEAM = TEAM.SG)
        fun applyFilter()
        fun resetFilter()
    }

    interface ViewEvent {
        fun onClickMatch(pdfPath: String?)
    }
}