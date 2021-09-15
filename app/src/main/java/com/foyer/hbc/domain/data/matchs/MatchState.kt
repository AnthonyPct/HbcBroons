package com.foyer.hbc.domain.data.matchs

import com.foyer.hbc.domain.model.MatchEntity

sealed class MatchState {
    object Loading : MatchState()
    data class HasMatch(val matchs: List<MatchEntity>) : MatchState()
    object Error : MatchState()
}
