package com.foyer.hbc.data.database

import com.foyer.hbc.domain.model.CheckoutEntity
import com.foyer.hbc.domain.model.ConsumptionEntity
import com.foyer.hbc.domain.model.PaymentEntity
import com.foyer.hbc.domain.model.UserEntity
import kotlinx.coroutines.flow.Flow
import java.util.*

interface DatabaseRepository {

    ///////////////////////////////////////////////////////////////////////////
    // USERS
    ///////////////////////////////////////////////////////////////////////////

    fun getUser(): Flow<List<UserEntity>>
    suspend fun getBestFiveUser(): List<UserEntity>
    suspend fun addUser(userEntity: UserEntity)
    suspend fun saveUser(users: List<UserEntity>)
    suspend fun updateUserBalance(userEntity: UserEntity, price: Double, quantity: Int)
    suspend fun updateUserAfterCredit(userEntity: UserEntity, credit: Double)

    ///////////////////////////////////////////////////////////////////////////
    // CONSUMPTIONS
    ///////////////////////////////////////////////////////////////////////////

    suspend fun saveConsumptions(consumptions: List<ConsumptionEntity>)
    fun getTotalConsumptionsNumber(): Flow<Int>
    fun getConsumptions(): Flow<List<ConsumptionEntity>>
    fun getUserConsumptions(user: UserEntity): Flow<List<ConsumptionEntity>>

    ///////////////////////////////////////////////////////////////////////////
    // CHECKOUT
    ///////////////////////////////////////////////////////////////////////////

    suspend fun saveCheckout(checkout: CheckoutEntity)
    fun getCheckout(): Flow<CheckoutEntity>
    suspend fun updateCheckoutAfterConsumptions(date: Date, price: Double, userEntity: UserEntity)
    suspend fun updateCheckoutAfterPayment(date: Date, amount: Double, user: UserEntity)
    suspend fun updateCheckoutAfterBillPayment(amount: Double)

    ///////////////////////////////////////////////////////////////////////////
    // PAYMENTS
    ///////////////////////////////////////////////////////////////////////////

    suspend fun savePayment(payments: List<PaymentEntity>)
    suspend fun getPayment(username: String): List<PaymentEntity>
    suspend fun addPayment(paymentEntity: PaymentEntity)
    fun getBills(): Flow<List<PaymentEntity>>
    suspend fun getCurrentBillsAmount(): Double?
    suspend fun payBill(bill: PaymentEntity)
}
