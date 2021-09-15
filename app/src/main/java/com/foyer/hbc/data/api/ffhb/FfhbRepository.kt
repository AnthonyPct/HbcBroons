package com.foyer.hbc.data.api.ffhb

import com.foyer.hbc.domain.data.matchs.TEAM
import com.foyer.hbc.domain.model.MatchEntity

interface FfhbRepository {
    suspend fun getMatchs(team: TEAM): List<MatchEntity>
}