package com.foyer.hbc.presentation.dashboard

import com.foyer.hbc.domain.data.dashboard.CarouselState
import com.foyer.hbc.domain.data.dashboard.HistoricState
import com.foyer.hbc.domain.model.ConsumptionEntity
import com.foyer.hbc.domain.model.UserEntity
import kotlinx.coroutines.flow.StateFlow

interface IDashboardContract {

    interface ViewModel {
        val carouselState: StateFlow<CarouselState>
        val historicState: StateFlow<HistoricState>

        fun getHistoricState()
        fun getCarouselState()
        fun getCurrentDate(): String

        fun addBeerToOrder()
        fun removeBeerToOrder()
        fun addSoftToOrder()
        fun removeSoftToOrder()
        fun addEatToOrder()
        fun removeEatToOrder()
        fun getTotalBeerPrice(): Double
        fun getTotalSoftPrice(): Double
        fun getTotalEatPrice(): Double
        fun getTotalOrderPrice(): Double
        fun addConsumption(user: UserEntity, comment: String?)
    }

    interface ViewCapabilities {
        fun showSelectUserPopup()
        fun updateOrderButton()
        fun showConfirmationOrderPopup(user: UserEntity?)
    }

    interface ViewEvent {
        interface Historic {
            fun onClickConsumption(consumptionEntity: ConsumptionEntity)
            fun onClickFooter()
        }

        interface Order {
            fun onClickAddBeer()
            fun onClickRemoveBeer()
            fun onClickAddSoft()
            fun onClickRemoveSoft()
            fun onClickAddEat()
            fun onClickRemoveEat()
            fun onClickValidateOrder()
        }
    }
}
