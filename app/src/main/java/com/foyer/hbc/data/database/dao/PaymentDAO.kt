package com.foyer.hbc.data.database.dao

import androidx.room.*
import com.foyer.hbc.domain.model.PaymentEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PaymentDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun savePayments(payments: List<PaymentEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addPayment(payments: PaymentEntity)

    @Query("Select * from PaymentEntity where user = :username ORDER BY date DESC")
    suspend fun getUserPayments(username: String): List<PaymentEntity>

    @Query("Select * from PaymentEntity where charge = :isCharge")
    fun getBillsFlow(isCharge: Boolean = true): Flow<List<PaymentEntity>>

    @Query("Select * from PaymentEntity where charge = :isCharge")
    suspend fun getBills(isCharge: Boolean = true): List<PaymentEntity>

    @Update
    suspend fun payBill(bill: PaymentEntity)

    @Delete
    suspend fun deletePayment(payments: PaymentEntity)
}
