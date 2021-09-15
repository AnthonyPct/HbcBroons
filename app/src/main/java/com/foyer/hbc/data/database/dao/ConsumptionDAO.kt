package com.foyer.hbc.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.foyer.hbc.domain.data.dashboard.ProductType
import com.foyer.hbc.domain.model.ConsumptionEntity
import com.foyer.hbc.domain.model.Type
import kotlinx.coroutines.flow.Flow

@Dao
interface ConsumptionDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveConsumptions(consumptions: List<ConsumptionEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveConsumption(consumptions: ConsumptionEntity)

    @Query("Select amount from ConsumptionEntity where type = :checkoutType and productType = :productType")
    fun getTotalConsumptions(
        checkoutType: Type,
        productType: ProductType = ProductType.BEER
    ): Flow<List<Int>>

    @Query("Select * from ConsumptionEntity ORDER BY date DESC")
    fun getConsumptions(): Flow<List<ConsumptionEntity>>

    @Query("Select * from ConsumptionEntity where user = :username")
    fun getUserConsumptions(username: String): Flow<List<ConsumptionEntity>>
}
