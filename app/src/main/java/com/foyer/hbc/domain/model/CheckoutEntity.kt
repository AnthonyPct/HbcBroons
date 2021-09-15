package com.foyer.hbc.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
data class CheckoutEntity(
    @PrimaryKey(autoGenerate = false)
    val name: String,
    val actualAmount: Double,
    val unPayed: Int,
    val expectedAmount: Double,
    val lastModify: Date,
    val nbFut: Int,
) {
    constructor() : this(
        name = "Foyer",
        actualAmount = 0.0,
        unPayed = 0,
        expectedAmount = 0.0,
        lastModify = Date(),
        nbFut = 0
    )
}
