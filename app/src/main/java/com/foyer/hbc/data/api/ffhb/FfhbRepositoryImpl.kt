package com.foyer.hbc.data.api.ffhb

import com.foyer.hbc.domain.data.matchs.TEAM
import com.foyer.hbc.domain.model.MatchEntity

class FfhbRepositoryImpl(val api: IffhbApi) : FfhbRepository {

    ///////////////////////////////////////////////////////////////////////////
    // CONTRACT IMPLEMENTATION
    ///////////////////////////////////////////////////////////////////////////

    override suspend fun getMatchs(team: TEAM): List<MatchEntity> {
        val model = api.getMatchs(team.idFFhb)
        val result = mutableListOf<MatchEntity>()
        val days = mutableListOf(
            model.matchs.day1,
            model.matchs.day2,
            model.matchs.day3,
            model.matchs.day4,
            model.matchs.day5,
            model.matchs.day6,
            model.matchs.day7,
            model.matchs.day8,
            model.matchs.day9,
            model.matchs.day10,
            model.matchs.day11,
            model.matchs.day12,
            model.matchs.day13,
            model.matchs.day14,
            model.matchs.day15,
            model.matchs.day16,
            model.matchs.day17,
            model.matchs.day18,
            model.matchs.day19,
            model.matchs.day20,
            model.matchs.day21,
            model.matchs.day22,
        ).filterNotNull()

        days.forEach { day ->
            day.events
                .forEach { match ->
                    result.add(
                        MatchEntity(
                            match.matchDate,
                            firstTeam = match.teams.first().name,
                            firstScore = match.teams.first().score,
                            secondTeam = match.teams[1].name,
                            secondScore = match.teams[1].score,
                            pdfPath = "https://www.ffhandball.fr/api/s3/fdm/R/A/D/O/${match.pdfCode}.pdf"
                        )
                    )
                }
        }
        return result
    }
}