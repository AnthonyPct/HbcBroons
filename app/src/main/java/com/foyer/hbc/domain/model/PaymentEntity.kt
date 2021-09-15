package com.foyer.hbc.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
data class PaymentEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    var user: String,
    var price: Double,
    var date: Date,
    var factureUrl: String?,
    var isPayed: Boolean,
    var nbFut: Int?,ge
    var mode: PaymentType,
    var charge: Boolean
) {
    constructor() : this(0, "", 0.0, Date(), null, false, null, PaymentType.CB, false)
}

enum class PaymentType {
    CASH,
    CB
}
