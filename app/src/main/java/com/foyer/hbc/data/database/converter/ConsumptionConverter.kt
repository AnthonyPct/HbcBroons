package com.foyer.hbc.data.database.converter

import androidx.room.TypeConverter
import com.foyer.hbc.domain.model.ConsumptionEntity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class ConsumptionConverter {

    @TypeConverter
    fun fromMessageList(messages: List<ConsumptionEntity>): String {
        return Gson().toJson(messages)
    }

    @TypeConverter
    fun toMessageList(messages: String): List<ConsumptionEntity> {
        val type = object : TypeToken<List<ConsumptionEntity>>() {}.type
        return Gson().fromJson(messages, type)
    }
}
