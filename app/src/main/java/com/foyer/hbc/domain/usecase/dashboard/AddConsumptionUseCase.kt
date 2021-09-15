package com.foyer.hbc.domain.usecase.dashboard

import com.foyer.hbc.data.api.firebase.FirebaseRepository
import com.foyer.hbc.data.database.DatabaseRepository
import com.foyer.hbc.domain.data.dashboard.ProductType
import com.foyer.hbc.domain.model.ConsumptionEntity
import com.foyer.hbc.domain.model.UserEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.withContext

class AddConsumptionUseCase(
    val databaseRepository: DatabaseRepository,
    val firebaseRepository: FirebaseRepository
) {

    ///////////////////////////////////////////////////////////////////////////
    // PUBLIC API
    ///////////////////////////////////////////////////////////////////////////

    suspend fun execute(
        user: UserEntity,
        totalPrice: Double,
        consumptions: List<ConsumptionEntity>
    ) {
        val drinkQuantity =
            consumptions.filter { it.productType != ProductType.EAT }.sumOf { it.amount }

        joinAll(
            withContext(Dispatchers.IO) {
                async { databaseRepository.saveConsumptions(consumptions) }
            },
            withContext(Dispatchers.IO) {
                async { databaseRepository.updateUserBalance(user, totalPrice, drinkQuantity) }
            },
            withContext(Dispatchers.IO) {
                async {
                    databaseRepository.updateCheckoutAfterConsumptions(
                        consumptions.first().date,
                        totalPrice,
                        user
                    )
                }
            }
        )

        joinAll(
            withContext(Dispatchers.IO) {
                async { firebaseRepository.addConsumption(consumptions) }
            },
            withContext(Dispatchers.IO) {
                async { firebaseRepository.updateUser(user, totalPrice, drinkQuantity) }
            },
            withContext(Dispatchers.IO) {
                async {
                    firebaseRepository.updateCheckoutAfterConsumption(
                        consumptions.first().date,
                        totalPrice,
                        user
                    )
                }
            })
    }
}
