package com.foyer.hbc.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class UserEntity(
    @PrimaryKey
    var nom: String,
    var equipe: String,
    var balance: Double,
    var consumptionsPayed: Int
) {
    constructor() : this("", "", 0.0, 0)

    override fun toString(): String =
        "$nom Equipe : $equipe /balance $balance}"
}

///////////////////////////////////////////////////////////////////////////
// EXTENSION
///////////////////////////////////////////////////////////////////////////

fun UserEntity.getUrlImages(): String {
    return nom.replace(" ", "").plus(".jpeg")
}
