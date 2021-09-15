package com.foyer.hbc.domain.usecase.splashscreen

import com.foyer.hbc.data.api.firebase.FirebaseRepository
import com.foyer.hbc.data.database.DatabaseRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class FetchCheckoutUseCase(
    private val remoteRepository: FirebaseRepository,
    private val localRepository: DatabaseRepository
) {

    ///////////////////////////////////////////////////////////////////////////
    // PUBLIC API
    ///////////////////////////////////////////////////////////////////////////

    //TODO : pour l'instant je save juste la cagnotte foyer mais voir si on fait autrement
    suspend fun execute() {
        withContext(Dispatchers.IO) {
            val checkout = remoteRepository.getCheckout().firstOrNull {
                it.name == "Foyer"
            }
            checkout?.let { localRepository.saveCheckout(it) }
        }
    }
}
