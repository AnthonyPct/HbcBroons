package com.foyer.hbc.data.database.converter

import androidx.room.TypeConverter
import com.foyer.hbc.domain.model.PaymentType

class PaymentTypeConverter {

    @TypeConverter
    fun fromType(type: PaymentType): String {
        return type.toString()
    }

    @TypeConverter
    fun toType(msg: String): PaymentType {
        return PaymentType.valueOf(msg)
    }
}