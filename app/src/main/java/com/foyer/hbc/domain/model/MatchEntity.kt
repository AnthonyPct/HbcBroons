package com.foyer.hbc.domain.model

import com.foyer.hbc.data.api.model.MatchDate

///////////////////////////////////////////////////////////////////////////
// DATA
///////////////////////////////////////////////////////////////////////////

private const val BROONS_TEAMS_NAME = "BROONS"

data class MatchEntity(
    var date: MatchDate?,
    var firstTeam: String?,
    var secondTeam: String?,
    var firstScore: Int?,
    var secondScore: Int?,
    var pdfPath: String?
)

///////////////////////////////////////////////////////////////////////////
// EXTENSION
///////////////////////////////////////////////////////////////////////////

fun MatchEntity.isBroonsMatch(): Boolean {
    return firstTeam?.contains(BROONS_TEAMS_NAME, true) == true ||
            secondTeam?.contains(BROONS_TEAMS_NAME, true) == true
}
