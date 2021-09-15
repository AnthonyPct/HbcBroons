package com.foyer.hbc.presentation.dashboard

import android.app.Application
import androidx.lifecycle.viewModelScope
import com.foyer.hbc.base.BaseViewModel
import com.foyer.hbc.common.getCompleteDayFormat
import com.foyer.hbc.data.database.DatabaseRepository
import com.foyer.hbc.domain.data.dashboard.CarouselState
import com.foyer.hbc.domain.data.dashboard.HistoricState
import com.foyer.hbc.domain.data.dashboard.ProductType
import com.foyer.hbc.domain.model.ConsumptionEntity
import com.foyer.hbc.domain.model.Type
import com.foyer.hbc.domain.model.UserEntity
import com.foyer.hbc.domain.usecase.dashboard.AddConsumptionUseCase
import com.foyer.hbc.domain.usecase.dashboard.GetCarouseCellsUseCase
import com.foyer.hbc.domain.usecase.dashboard.GetHistoricItemUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import org.koin.java.KoinJavaComponent.inject
import java.util.*

class DashboardViewModel(application: Application, val databaseRepository: DatabaseRepository) :
    BaseViewModel(application),
    IDashboardContract.ViewModel {

    ///////////////////////////////////////////////////////////////////////////
    // CONFIGURATION
    ///////////////////////////////////////////////////////////////////////////

    companion object {
        val DashboardModule = module {
            viewModel { DashboardViewModel(androidApplication(), get()) }
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // DEPENDENCY
    ///////////////////////////////////////////////////////////////////////////

    private val getCarouseCellsUseCase: GetCarouseCellsUseCase by inject(GetCarouseCellsUseCase::class.java)
    private val getHistoricItemUseCase: GetHistoricItemUseCase by inject(GetHistoricItemUseCase::class.java)
    private val addConsumptionUseCase: AddConsumptionUseCase by inject(AddConsumptionUseCase::class.java)

    ///////////////////////////////////////////////////////////////////////////
    // DATA
    ///////////////////////////////////////////////////////////////////////////

    // The UI collects from this StateFlow to get its state updates
    override val carouselState: StateFlow<CarouselState>
        get() = mutableFLow

    // Backing property to avoid state updates from other classes
    private val mutableFLow = MutableStateFlow<CarouselState>(CarouselState.Loading)

    override val historicState: StateFlow<HistoricState>
        get() = mutableHistoricFlow

    private val mutableHistoricFlow = MutableStateFlow<HistoricState>(HistoricState.Loading)

    private var beerQuantity: Int = 0
    private var softQuantity: Int = 0
    private var eatQuantity: Int = 0
    private var beerConsumptions: ConsumptionEntity? = null
    private var softConsumptions: ConsumptionEntity? = null
    private var eatConsumptions: ConsumptionEntity? = null

    ///////////////////////////////////////////////////////////////////////////
    // SPECIALIZATION
    ///////////////////////////////////////////////////////////////////////////

    override fun getCarouselState() {
        viewModelScope.launch {
            getCarouseCellsUseCase.execute().collect {
                mutableFLow.value = CarouselState.Success(it)
            }
        }
    }

    override fun getHistoricState() {
        viewModelScope.launch {
            try {
                getHistoricItemUseCase.execute().collect {
                    mutableHistoricFlow.value = HistoricState.Success(it)
                }
            } catch (e: Exception) {
                mutableHistoricFlow.value = HistoricState.Error
            }
        }
    }

    override fun getCurrentDate(): String {
        return Date().getCompleteDayFormat()
    }

    ///////////////////////////////////////////////////////////////////////////
    // ORDER
    ///////////////////////////////////////////////////////////////////////////

    override fun addConsumption(user: UserEntity, comment: String?) {
        val consumptions = listOfNotNull(
            beerConsumptions,
            softConsumptions,
            eatConsumptions
        ).onEach {
            it.user = user.nom
            it.date = Date()
        }

        viewModelScope.launch {
            addConsumptionUseCase.execute(
                user,
                getTotalOrderPrice(),
                consumptions
            )
        }
    }

    override fun addBeerToOrder() {
        beerQuantity += 1
        if (beerConsumptions != null) {
            beerConsumptions?.amount = beerQuantity
            beerConsumptions?.price = getTotalBeerPrice()
        } else {
            beerConsumptions = ConsumptionEntity(
                null,
                getTotalBeerPrice(),
                beerQuantity,
                Type.FOYER,
                productType = ProductType.BEER
            )
        }
    }

    override fun addSoftToOrder() {
        softQuantity += 1
        if (softConsumptions != null) {
            softConsumptions?.amount = softQuantity
            softConsumptions?.price = getTotalSoftPrice()
        } else {
            softConsumptions = ConsumptionEntity(
                null,
                getTotalSoftPrice(),
                softQuantity,
                Type.FOYER,
                productType = ProductType.SOFT
            )
        }
    }

    override fun addEatToOrder() {
        eatQuantity += 1
        if (eatConsumptions != null) {
            eatConsumptions?.amount = eatQuantity
            eatConsumptions?.price = getTotalEatPrice()
        } else {
            eatConsumptions = ConsumptionEntity(
                null,
                getTotalEatPrice(),
                eatQuantity,
                Type.FOYER,
                productType = ProductType.EAT
            )
        }
    }

    override fun removeBeerToOrder() {
        if (beerQuantity > 0) {
            beerQuantity -= 1
            beerConsumptions?.amount = beerQuantity
            beerConsumptions?.price = getTotalBeerPrice()
        } else {
            beerConsumptions = null
        }
    }

    override fun removeEatToOrder() {
        if (eatQuantity > 0) {
            eatQuantity -= 1
            eatConsumptions?.amount = eatQuantity
            eatConsumptions?.price = getTotalEatPrice()
        } else {
            eatConsumptions = null
        }
    }

    override fun removeSoftToOrder() {
        if (softQuantity > 0) {
            softQuantity -= 1
            softConsumptions?.amount = softQuantity
            softConsumptions?.price = getTotalSoftPrice()
        } else {
            softConsumptions = null
        }
    }

    override fun getTotalEatPrice(): Double {
        return eatQuantity * ProductType.EAT.price
    }

    override fun getTotalBeerPrice(): Double {
        return beerQuantity * ProductType.BEER.price
    }

    override fun getTotalSoftPrice(): Double {
        return softQuantity * ProductType.SOFT.price
    }

    override fun getTotalOrderPrice(): Double {
        return getTotalBeerPrice() + getTotalSoftPrice() + getTotalEatPrice()
    }

    ///////////////////////////////////////////////////////////////////////////
    // HELPER
    ///////////////////////////////////////////////////////////////////////////

    fun resetData() {
        beerConsumptions = null
        softConsumptions = null
        eatConsumptions = null
        softQuantity = 0
        beerQuantity = 0
        eatQuantity = 0
    }
}
