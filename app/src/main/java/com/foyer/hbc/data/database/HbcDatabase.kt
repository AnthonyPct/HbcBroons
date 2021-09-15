package com.foyer.hbc.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.foyer.hbc.data.database.converter.*
import com.foyer.hbc.data.database.dao.*
import com.foyer.hbc.domain.model.CheckoutEntity
import com.foyer.hbc.domain.model.ConsumptionEntity
import com.foyer.hbc.domain.model.PaymentEntity
import com.foyer.hbc.domain.model.UserEntity

@Database(
    entities = [
        UserEntity::class,
        ConsumptionEntity::class,
        CheckoutEntity::class,
        PaymentEntity::class
    ], version = 15
)
@TypeConverters(
    DateTypeConverter::class,
    ConsumptionConverter::class,
    TypeConverter::class,
    PaymentTypeConverter::class,
    ProductTypeConverter::class
)
abstract class HbcDatabase : RoomDatabase() {
    abstract val userDAO: UserDAO
    abstract val messageDAO: ConsumptionDAO
    abstract val paymentDAO: PaymentDAO
    abstract val matchDAO: MatchDAO
    abstract val checkoutDAO: CheckoutDAO
}
