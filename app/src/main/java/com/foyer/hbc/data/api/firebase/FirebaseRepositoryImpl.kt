package com.foyer.hbc.data.api.firebase

import com.foyer.hbc.data.database.AbstractRepository
import com.foyer.hbc.domain.model.CheckoutEntity
import com.foyer.hbc.domain.model.ConsumptionEntity
import com.foyer.hbc.domain.model.PaymentEntity
import com.foyer.hbc.domain.model.UserEntity
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import java.util.*

class FirebaseRepositoryImpl :
    AbstractRepository(),
    FirebaseRepository {

    ///////////////////////////////////////////////////////////////////////////
    // DATA
    ///////////////////////////////////////////////////////////////////////////

    private var firestoreDB = FirebaseFirestore.getInstance()

    ///////////////////////////////////////////////////////////////////////////
    // SPECIALIZATION - USER
    ///////////////////////////////////////////////////////////////////////////

    override suspend fun getUsers(): List<UserEntity> {
        val snapshot = getUsersCollection().get().await()
        return snapshot.toObjects(UserEntity::class.java)
    }

    override suspend fun updateUser(
        userEntity: UserEntity,
        consumptionPrice: Double,
        quantity: Int
    ) {
        getUsersCollection()
            .whereEqualTo("nom", userEntity.nom)
            .get()
            .addOnSuccessListener { documents ->
                documents.forEach {
                    it.reference
                        .update(
                            mapOf(
                                "balance" to userEntity.balance - consumptionPrice,
                                "consumptionsPayed" to userEntity.consumptionsPayed + quantity
                            )
                        )
                }
            }
            .await()
    }

    override suspend fun addUser(userEntity: UserEntity) {
        getUsersCollection()
            .add(userEntity)
            .await()
    }

    override suspend fun updateUserAfterCredit(userEntity: UserEntity, credit: Double) {
        getUsersCollection()
            .whereEqualTo("nom", userEntity.nom)
            .get()
            .addOnSuccessListener { documents ->
                documents.forEach {
                    it.reference
                        .update(
                            mapOf(
                                "balance" to userEntity.balance + credit
                            )
                        )
                }
            }
            .await()
    }

    ///////////////////////////////////////////////////////////////////////////
    // CONSUMPTIONS
    ///////////////////////////////////////////////////////////////////////////

    override suspend fun getConsumptions(): List<ConsumptionEntity> {
        val snapshot = getConsumptionsCollection().get().await()
        return snapshot.toObjects(ConsumptionEntity::class.java)
    }

    override suspend fun addConsumption(consumptionEntity: List<ConsumptionEntity>) {
        consumptionEntity.forEach {
            getConsumptionsCollection()
                .add(it)
                .await()
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // PAYMENT
    ///////////////////////////////////////////////////////////////////////////

    override suspend fun addPayment(paymentEntity: PaymentEntity) {
        getPaymentCollection()
            .add(paymentEntity)
            .await()
    }

    override suspend fun getCheckout(): List<CheckoutEntity> {
        val snapshot = getCheckoutCollection().get().await()
        return snapshot.toObjects(CheckoutEntity::class.java)
    }

    override suspend fun getPayments(): List<PaymentEntity> {
        val snapshot = getPaymentCollection().get().await()
        return snapshot.toObjects(PaymentEntity::class.java)
    }

    override suspend fun payBill(paymentEntity: PaymentEntity) {
        getPaymentCollection()
            .whereEqualTo("id", paymentEntity.id)
            .get()
            .addOnSuccessListener { documents ->
                documents.forEach {
                    it.reference
                        .update(
                            mapOf("isPayed" to true)
                        )
                }
            }
            .await()
    }

    ///////////////////////////////////////////////////////////////////////////
    // CHECKOUT
    ///////////////////////////////////////////////////////////////////////////

    override suspend fun updateCheckoutAfterConsumption(
        date: Date,
        price: Double,
        userEntity: UserEntity
    ) {
        val checkout: CheckoutEntity? = getCheckout().firstOrNull {
            it.name == "Foyer"
        }

        val newUnPayedValue: Double = getNewCheckoutUnPayedAfterUserConsumptions(
            userEntity.balance,
            price,
            checkout?.unPayed?.toDouble() ?: 0.0
        )

        checkout?.let {
            getCheckoutCollection()
                .document("foyer")
                .update(
                    mapOf(
                        "unPayed" to newUnPayedValue,
                        "lastModify" to date,
                    )
                )
                .await()
        }
    }

    override suspend fun updateCheckoutAfterPayment(date: Date, amount: Double, user: UserEntity) {
        val checkout = getCheckout().firstOrNull {
            it.name == "Foyer"
        }

        val newUnPayedValue = getNewCheckoutUnPayedAfterPayment(
            userBalance = user.balance,
            paymentAmount = amount,
            actualUnPayed = checkout?.unPayed?.toDouble() ?: 0.0
        )

        checkout?.let {
            getCheckoutCollection()
                .document("foyer")
                .update(
                    mapOf(
                        "actualAmount" to checkout.actualAmount + amount,
                        "lastModify" to date,
                        "unPayed" to newUnPayedValue
                    )
                )
                .await()
        }
    }

    override suspend fun updateCheckoutAfterBillPayment(amount: Double) {
        val checkout = getCheckout().firstOrNull {
            it.name == "Foyer"
        }

        checkout?.let {
            getCheckoutCollection()
                .document("foyer")
                .update(
                    mapOf(
                        "actualAmount" to checkout.actualAmount - amount
                    )
                )
                .await()
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // HELPER
    ///////////////////////////////////////////////////////////////////////////

    private fun getUsersCollection(): CollectionReference {
        return firestoreDB.collection(USERS_COLLECTION)
    }

    private fun getConsumptionsCollection(): CollectionReference {
        return firestoreDB.collection(CONSUMPTIONS_COLLECTION)
    }

    private fun getPaymentCollection(): CollectionReference {
        return firestoreDB.collection(PAYMENT_COLLECTION)
    }

    private fun getCheckoutCollection(): CollectionReference {
        return firestoreDB.collection(CHECKOUT_COLLECTION)
    }
}
