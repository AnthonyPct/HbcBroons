package com.foyer.hbc.data.api.firebase

import com.foyer.hbc.domain.model.CheckoutEntity
import com.foyer.hbc.domain.model.ConsumptionEntity
import com.foyer.hbc.domain.model.PaymentEntity
import com.foyer.hbc.domain.model.UserEntity
import java.util.*

interface FirebaseRepository {
    suspend fun getConsumptions(): List<ConsumptionEntity>
    suspend fun getUsers(): List<UserEntity>
    suspend fun getCheckout(): List<CheckoutEntity>
    suspend fun getPayments(): List<PaymentEntity>

    suspend fun addUser(userEntity: UserEntity)
    suspend fun addConsumption(consumptionEntity: List<ConsumptionEntity>)
    suspend fun addPayment(paymentEntity: PaymentEntity)
    suspend fun payBill(paymentEntity: PaymentEntity)

    suspend fun updateUser(userEntity: UserEntity, consumptionPrice: Double, quantity: Int)
    suspend fun updateUserAfterCredit(userEntity: UserEntity, credit: Double)
    suspend fun updateCheckoutAfterConsumption(date: Date, price: Double, userEntity: UserEntity)
    suspend fun updateCheckoutAfterPayment(date: Date, amount: Double, user: UserEntity)
    suspend fun updateCheckoutAfterBillPayment(amount: Double)
}
