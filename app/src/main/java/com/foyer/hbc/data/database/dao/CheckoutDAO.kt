package com.foyer.hbc.data.database.dao

import androidx.room.*
import com.foyer.hbc.domain.model.CheckoutEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CheckoutDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveCheckout(checkout: CheckoutEntity)

    @Query("SELECT * from CheckoutEntity")
    fun getCheckout(): Flow<CheckoutEntity>

    @Query("SELECT * from CheckoutEntity where name = :name")
    suspend fun getCheckout(name: String): CheckoutEntity

    @Update
    suspend fun updateCheckout(checkout: CheckoutEntity)
}
