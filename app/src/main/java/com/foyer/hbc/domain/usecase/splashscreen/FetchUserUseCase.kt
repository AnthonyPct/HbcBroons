package com.foyer.hbc.domain.usecase.splashscreen

import com.foyer.hbc.data.api.firebase.FirebaseRepository
import com.foyer.hbc.data.database.DatabaseRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class FetchUserUseCase(
    private val remoteRepository: FirebaseRepository,
    private val localRepository: DatabaseRepository
) {

    ///////////////////////////////////////////////////////////////////////////
    // PUBLIC API
    ///////////////////////////////////////////////////////////////////////////

    suspend fun execute() {
        withContext(Dispatchers.IO) {
            val users = remoteRepository.getUsers()
            localRepository.saveUser(users)
        }
    }
}
