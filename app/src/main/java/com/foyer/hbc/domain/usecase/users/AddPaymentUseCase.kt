package com.foyer.hbc.domain.usecase.users

import com.foyer.hbc.data.api.firebase.FirebaseRepository
import com.foyer.hbc.data.database.DatabaseRepository
import com.foyer.hbc.domain.model.PaymentEntity
import com.foyer.hbc.domain.model.PaymentType
import com.foyer.hbc.domain.model.UserEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.withContext
import java.util.*

class AddPaymentUseCase(
    private val databaseRepository: DatabaseRepository,
    private val firebaseRepository: FirebaseRepository
) {

    ///////////////////////////////////////////////////////////////////////////
    // PUBLIC API
    ///////////////////////////////////////////////////////////////////////////

    suspend fun execute(amount: Double, user: UserEntity, mode: PaymentType) {
        val newPayment = PaymentEntity(
            user = user.nom,
            price = amount,
            date = Date(),
            factureUrl = null,
            isPayed = true,
            nbFut = null,
            mode = mode,
            charge = false
        )

        // Database
        joinAll(
            withContext(Dispatchers.IO) {
                async { databaseRepository.updateCheckoutAfterPayment(Date(), amount, user) }
            },
            withContext(Dispatchers.IO) {
                async { databaseRepository.updateUserAfterCredit(user, amount) }
            },
            withContext(Dispatchers.IO) {
                async { databaseRepository.addPayment(newPayment) }
            }
        )

        // Repo
        joinAll(
            withContext(Dispatchers.IO) {
                async { firebaseRepository.updateUserAfterCredit(user, amount) }
            },
            withContext(Dispatchers.IO) {
                async { firebaseRepository.updateCheckoutAfterPayment(Date(), amount, user) }
            },
            withContext(Dispatchers.IO) {
                async { firebaseRepository.addPayment(newPayment) }
            }
        )
    }
}