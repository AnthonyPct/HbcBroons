package com.foyer.hbc.domain.usecase.dashboard

import com.foyer.hbc.data.database.DatabaseRepository
import com.foyer.hbc.domain.data.dashboard.CarouselCellInfo
import com.foyer.hbc.domain.data.dashboard.CarouselCellType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map

class GetCarouseCellsUseCase(private val databaseRepository: DatabaseRepository) {

    ///////////////////////////////////////////////////////////////////////////
    // PUBLIC API
    ///////////////////////////////////////////////////////////////////////////

    fun execute(): Flow<List<CarouselCellType>> {
        return combine(
            getCheckoutCellUseCase(),
            getUnPayedCellUseCase(),
            getPayedCellUseCase(),
            getTotalConsumptionsCellUseCase()
        ) { checkoutCell, unPayedCell, payedCell, totalConsumptionCell ->
            listOf(checkoutCell, unPayedCell, payedCell, totalConsumptionCell)
        }
            .flowOn(Dispatchers.IO)
    }

    ///////////////////////////////////////////////////////////////////////////
    // HELPER
    ///////////////////////////////////////////////////////////////////////////

    private fun getCheckoutCellUseCase(): Flow<CarouselCellType> {
        return databaseRepository.getCheckout().map { checkoutEntity ->
            val currentBill = databaseRepository.getCurrentBillsAmount()
            CarouselCellType.Checkout(
                info = CarouselCellInfo.CHECKOUT,
                actualAmount = checkoutEntity.actualAmount,
                currentBill = currentBill
            )
        }
    }

    private fun getUnPayedCellUseCase(): Flow<CarouselCellType> {
        return databaseRepository.getUser().map {
            CarouselCellType.UnPayed(
                info = CarouselCellInfo.UNPAYED,
                mostUnPayedUser = it.minByOrNull { user -> user.balance }
            )
        }
    }

    private fun getPayedCellUseCase(): Flow<CarouselCellType> {
        return databaseRepository.getUser().map {
            CarouselCellType.Payed(
                info = CarouselCellInfo.PAYED,
                mostPayedUser = it.maxByOrNull { user -> user.balance }
            )
        }
    }

    private fun getTotalConsumptionsCellUseCase(): Flow<CarouselCellType> {
        return databaseRepository.getTotalConsumptionsNumber().map {
            CarouselCellType.TotalConsumptions(
                info = CarouselCellInfo.SUM,
                totalConsumptions = it
            )
        }
    }
}
