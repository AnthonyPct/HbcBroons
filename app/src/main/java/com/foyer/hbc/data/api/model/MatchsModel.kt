package com.foyer.hbc.data.api.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class MatchsModel(
    @Json(name = "dates")
    val matchs: DayMatchsModel
)

// Api ffhb est vraiment naze
@JsonClass(generateAdapter = true)
data class DayMatchsModel(
    @Json(name = "1")
    val day1: DayMatchModel?,
    @Json(name = "2")
    val day2: DayMatchModel?,
    @Json(name = "3")
    val day3: DayMatchModel?,
    @Json(name = "4")
    val day4: DayMatchModel?,
    @Json(name = "5")
    val day5: DayMatchModel?,
    @Json(name = "6")
    val day6: DayMatchModel?,
    @Json(name = "7")
    val day7: DayMatchModel?,
    @Json(name = "8")
    val day8: DayMatchModel?,
    @Json(name = "9")
    val day9: DayMatchModel?,
    @Json(name = "10")
    val day10: DayMatchModel?,
    @Json(name = "11")
    val day11: DayMatchModel?,
    @Json(name = "12")
    val day12: DayMatchModel?,
    @Json(name = "13")
    val day13: DayMatchModel?,
    @Json(name = "14")
    val day14: DayMatchModel?,
    @Json(name = "15")
    val day15: DayMatchModel?,
    @Json(name = "16")
    val day16: DayMatchModel?,
    @Json(name = "17")
    val day17: DayMatchModel?,
    @Json(name = "18")
    val day18: DayMatchModel?,
    @Json(name = "19")
    val day19: DayMatchModel?,
    @Json(name = "20")
    val day20: DayMatchModel?,
    @Json(name = "21")
    val day21: DayMatchModel?,
    @Json(name = "22")
    val day22: DayMatchModel?
)


@JsonClass(generateAdapter = true)
data class DayMatchModel(
    @Json(name = "start")
    val start: String?,
    @Json(name = "events")
    val events: List<MatchModel>
)

@JsonClass(generateAdapter = true)
data class MatchModel(
    @Json(name = "date")
    val matchDate: MatchDate,
    @Json(name = "teams")
    val teams: List<MatchTeam>,
    @Json(name = "pdfPath")
    val pdfPath: String?,
    @Json(name = "CON_CODE_RENC")
    val pdfCode: String?
)

@JsonClass(generateAdapter = true)
data class MatchDate(
    @Json(name = "date")
    val date: String?,
    @Json(name = "hour")
    val hour: String?,
    @Json(name = "minute")
    val minute: String?
)

@JsonClass(generateAdapter = true)
data class MatchTeam(
    @Json(name = "name")
    val name: String,
    @Json(name = "score")
    val score: Int?
)



