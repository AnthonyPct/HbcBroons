package com.foyer.hbc.data.api.ffhb

import com.foyer.hbc.data.api.model.MatchsModel
import com.foyer.hbc.domain.model.MatchEntity
import retrofit2.http.GET
import retrofit2.http.Path

interface IffhbApi {

    @GET("prod/pool/{groupNumber}")
    suspend fun getMatchs(@Path("groupNumber") groupNumber: String = "90242"): MatchsModel

    @GET("prod/player/90242")
    suspend fun getPlayer(groupNumber: String = "90242"): List<MatchEntity>
}