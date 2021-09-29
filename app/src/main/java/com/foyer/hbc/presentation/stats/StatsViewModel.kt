package com.foyer.hbc.presentation.stats

import android.app.Application
import androidx.lifecycle.viewModelScope
import com.foyer.hbc.base.BaseViewModel
import com.foyer.hbc.data.database.DatabaseRepository
import com.foyer.hbc.domain.usecase.stats.GetConsumptionsEntryUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import org.koin.java.KoinJavaComponent.inject

class StatsViewModel(
    application: Application,
    val databaseRepository: DatabaseRepository,
) : BaseViewModel(application),
    StatsContract.ViewModel {

    ///////////////////////////////////////////////////////////////////////////
    // CONFIGURATION
    ///////////////////////////////////////////////////////////////////////////

    companion object {
        val StatsModule = module {
            viewModel { StatsViewModel(androidApplication(), get()) }
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // DEPENDENCY
    ///////////////////////////////////////////////////////////////////////////

    private val getConsumptionsEntryUseCase: GetConsumptionsEntryUseCase by inject(
        GetConsumptionsEntryUseCase::class.java
    )

    ///////////////////////////////////////////////////////////////////////////
    // DATA
    ///////////////////////////////////////////////////////////////////////////

    private val mutableStateFlow = MutableStateFlow<StatisticState>(StatisticState.Loading)


    ///////////////////////////////////////////////////////////////////////////
    // CONTRACT IMPLEMENTATION
    ///////////////////////////////////////////////////////////////////////////
    override val state: StateFlow<StatisticState>
        get() = mutableStateFlow

    override fun getData() {
        viewModelScope.launch {
            val bestUser = databaseRepository.getBestFiveUser()

            getConsumptionsEntryUseCase.execute().collect {
                mutableStateFlow.value = StatisticState.Success(it, bestUser)
            }
        }
    }
}