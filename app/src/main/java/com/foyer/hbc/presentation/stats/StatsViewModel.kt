package com.foyer.hbc.presentation.stats

import android.app.Application
import androidx.lifecycle.viewModelScope
import com.foyer.hbc.base.BaseViewModel
import com.foyer.hbc.common.witNoHour
import com.foyer.hbc.data.database.DatabaseRepository
import com.github.mikephil.charting.data.Entry
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

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
    // DATA
    ///////////////////////////////////////////////////////////////////////////

    override val state: StateFlow<StatisticState>
        get() = MutableStateFlow(StatisticState.Loading)

    ///////////////////////////////////////////////////////////////////////////
    // CONTRACT IMPLEMENTATION
    ///////////////////////////////////////////////////////////////////////////

    override fun getData() {
        viewModelScope.launch {
            databaseRepository.getConsumptions().collect {
                // TODO
            }
        }
    }
}