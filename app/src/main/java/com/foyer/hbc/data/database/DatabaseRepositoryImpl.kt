package com.foyer.hbc.data.database

import com.foyer.hbc.data.database.dao.*
import com.foyer.hbc.domain.model.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.*

class DatabaseRepositoryImpl(
    private val userDAO: UserDAO,
    private val consumptionsDAO: ConsumptionDAO,
    private val paymentDAO: PaymentDAO,
    val matchDAO: MatchDAO,
    private val checkoutDAO: CheckoutDAO
) :
    AbstractRepository(),
    DatabaseRepository {

    ///////////////////////////////////////////////////////////////////////////
    // USER
    ///////////////////////////////////////////////////////////////////////////

    override fun getUser(): Flow<List<UserEntity>> =
        userDAO.getUsers()

    override suspend fun getBestFiveUser(): List<UserEntity> {
        return userDAO.getBestUsers(5)
    }

    override suspend fun saveUser(users: List<UserEntity>) {
        userDAO.saveUser(users)
    }

    override suspend fun addUser(userEntity: UserEntity) {
        userDAO.addUser(userEntity)
    }

    override suspend fun updateUserBalance(userEntity: UserEntity, price: Double, quantity: Int) {
        userDAO.updateUser(
            userEntity.copy(
                balance = userEntity.balance - price,
                consumptionsPayed = userEntity.consumptionsPayed + quantity
            )
        )
    }

    override suspend fun updateUserAfterCredit(userEntity: UserEntity, credit: Double) {
        userDAO.updateUser(
            userEntity.copy(
                balance = userEntity.balance + credit.toInt()
            )
        )
    }

    ///////////////////////////////////////////////////////////////////////////
    // CONSUMPTIONS
    ///////////////////////////////////////////////////////////////////////////

    override suspend fun saveConsumptions(consumptions: List<ConsumptionEntity>) {
        consumptionsDAO.saveConsumptions(consumptions)
    }

    override fun getTotalConsumptionsNumber(): Flow<Int> {
        return consumptionsDAO.getTotalConsumptions(Type.FOYER).map {
            it.sum()
        }
    }

    override fun getConsumptions(): Flow<List<ConsumptionEntity>> {
        return consumptionsDAO.getConsumptions()
    }

    override fun getUserConsumptions(user: UserEntity): Flow<List<ConsumptionEntity>> {
        return consumptionsDAO.getUserConsumptions(user.nom)
    }

    ///////////////////////////////////////////////////////////////////////////
    // CHECKOUT
    ///////////////////////////////////////////////////////////////////////////

    override suspend fun saveCheckout(checkout: CheckoutEntity) {
        checkoutDAO.saveCheckout(checkout)
    }

    override fun getCheckout(): Flow<CheckoutEntity> =
        checkoutDAO.getCheckout()

    override suspend fun updateCheckoutAfterConsumptions(
        date: Date,
        price: Double,
        userEntity: UserEntity
    ) {
        val checkout: CheckoutEntity = checkoutDAO.getCheckout("Foyer")

        val newUnPayedValue: Double = getNewCheckoutUnPayedAfterUserConsumptions(
            userEntity.balance,
            price,
            checkout.unPayed.toDouble()
        )

        checkoutDAO.updateCheckout(
            checkout.copy(
                unPayed = newUnPayedValue.toInt(),
                lastModify = date,
            )
        )
    }

    override suspend fun updateCheckoutAfterPayment(date: Date, amount: Double, user: UserEntity) {
        val checkout: CheckoutEntity = checkoutDAO.getCheckout("Foyer")

        checkoutDAO.updateCheckout(
            checkout.copy(
                actualAmount = checkout.actualAmount + amount.toInt(),
                lastModify = date,
                unPayed = getNewCheckoutUnPayedAfterPayment(
                    user.balance,
                    amount,
                    checkout.unPayed.toDouble()
                ).toInt()
            )
        )
    }

    override suspend fun updateCheckoutAfterBillPayment(amount: Double) {
        val checkout: CheckoutEntity = checkoutDAO.getCheckout("Foyer")
        checkoutDAO.updateCheckout(
            checkout.copy(
                actualAmount = checkout.actualAmount - amount
            )
        )
    }

    ///////////////////////////////////////////////////////////////////////////
    // PAYMENT
    ///////////////////////////////////////////////////////////////////////////

    override suspend fun savePayment(payments: List<PaymentEntity>) {
        paymentDAO.savePayments(payments)
    }

    override suspend fun getPayment(username: String): List<PaymentEntity> {
        return paymentDAO.getUserPayments(username)
    }

    override suspend fun addPayment(paymentEntity: PaymentEntity) {
        paymentDAO.addPayment(paymentEntity)
    }

    ///////////////////////////////////////////////////////////////////////////
    // BILLS
    ///////////////////////////////////////////////////////////////////////////

    override fun getBills(): Flow<List<PaymentEntity>> {
        return paymentDAO.getBillsFlow()
    }

    override suspend fun getCurrentBillsAmount(): Double? {
        val bills = paymentDAO.getBills()
        return bills
            .filter { it.isPayed.not() && it.charge }
            .sumOf { it.price }
            .takeIf { it != 0.0 }
    }

    override suspend fun payBill(bill: PaymentEntity) {
        bill.isPayed = true
        paymentDAO.payBill(bill)
    }
}
