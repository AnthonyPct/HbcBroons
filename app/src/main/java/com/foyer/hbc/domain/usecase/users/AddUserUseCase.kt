package com.foyer.hbc.domain.usecase.users

import com.foyer.hbc.data.api.firebase.FirebaseRepository
import com.foyer.hbc.data.database.DatabaseRepository
import com.foyer.hbc.domain.model.UserEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.withContext

class AddUserUseCase(
    private val databaseRepository: DatabaseRepository,
    private val firebaseRepository: FirebaseRepository
) {

    ///////////////////////////////////////////////////////////////////////////
    // PUBLIC API
    ///////////////////////////////////////////////////////////////////////////

    suspend fun execute(nom: String, team: String) {
        val newUser = UserEntity(nom, team, 0.0, 0)

        joinAll(
            withContext(Dispatchers.IO) {
                async { databaseRepository.addUser(newUser) }
            },
            withContext(Dispatchers.IO) {
                async { firebaseRepository.addUser(newUser) }
            }
        )
    }
}