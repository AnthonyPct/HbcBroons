package com.foyer.hbc.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.foyer.hbc.domain.data.dashboard.ProductType
import java.util.*

@Entity
data class ConsumptionEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Int,
    var comment: String? = null,
    var date: Date,
    var price: Double,
    var amount: Int,
    var type: Type,
    var user: String,
    var productType: ProductType
) {
    constructor(
        comment: String? = null,
        date: Date,
        price: Double,
        amount: Int,
        type: Type,
        user: String,
        productType: ProductType
    ) : this(0, comment, date, price, amount, type, user, productType)

    constructor(
        comment: String? = null,
        price: Double,
        amount: Int,
        type: Type,
        productType: ProductType
    ) : this(0, comment, Date(), price, amount, type, "", productType)

    constructor() : this(0, null, Date(), 0.0, 0, Type.FOYER, "", ProductType.BEER)
}

enum class Type {
    FOYER,
    MATCH,
    JOUEUR,
    MANIFESTATION
}
