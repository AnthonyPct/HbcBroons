package com.foyer.hbc.data.database.converter

import androidx.room.TypeConverter
import com.foyer.hbc.domain.data.dashboard.ProductType

class ProductTypeConverter {

    @TypeConverter
    fun fromType(type: ProductType): String {
        return type.toString()
    }

    @TypeConverter
    fun toType(msg: String): ProductType {
        return ProductType.valueOf(msg)
    }
}