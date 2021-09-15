package com.foyer.hbc.domain.model

import com.foyer.hbc.data.api.model.MatchDate

data class MatchEntity(
    var date: MatchDate?,
    var firstTeam: String?,
    var secondTeam: String?,
    var firstScore: Int?,
    var secondScore: Int?,
    var pdfPath: String?
)
