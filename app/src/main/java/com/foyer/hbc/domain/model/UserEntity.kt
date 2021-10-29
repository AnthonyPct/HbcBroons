package com.foyer.hbc.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.text.Normalizer

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
        "$nom Equipe : $equipe /balance $balance} conso $consumptionsPayed"
}

///////////////////////////////////////////////////////////////////////////
// EXTENSION
///////////////////////////////////////////////////////////////////////////

fun UserEntity.getUrlImages(): String {
    return Normalizer
        .normalize(nom.capitalize(), Normalizer.Form.NFD)
        .replace("[^\\\\p{ASCII}]", "")
        .replace(" ", "")
        .plus(".jpeg")
}
