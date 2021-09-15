package com.foyer.hbc.data.database.converter

import androidx.room.TypeConverter
import com.foyer.hbc.domain.model.Type

class TypeConverter {

    @TypeConverter
    fun fromType(type: Type): String {
        return type.toString()
    }

    @TypeConverter
    fun toType(msg: String): Type {
        return Type.valueOf(msg)
    }
}
