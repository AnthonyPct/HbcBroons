package com.foyer.hbc.domain.usecase.splashscreen

import com.foyer.hbc.data.api.firebase.FirebaseRepository
import com.foyer.hbc.data.database.DatabaseRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class FetchPaymentsUseCase(
    private val remoteRepository: FirebaseRepository,
    private val localRepository: DatabaseRepository
) {

    ///////////////////////////////////////////////////////////////////////////
    // PUBLIC API
    ///////////////////////////////////////////////////////////////////////////

    suspend fun execute() {
        withContext(Dispatchers.IO) {
            val payments = remoteRepository.getPayments()
            localRepository.savePayment(payments)
        }
    }
}
