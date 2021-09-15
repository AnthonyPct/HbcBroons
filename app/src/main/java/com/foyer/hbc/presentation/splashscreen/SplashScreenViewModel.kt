package com.foyer.hbc.presentation.splashscreen

import android.app.Application
import androidx.lifecycle.viewModelScope
import com.foyer.hbc.base.BaseViewModel
import com.foyer.hbc.data.database.HbcDatabase
import com.foyer.hbc.domain.usecase.common.IsNetworkConnectionAvailableUseCase
import com.foyer.hbc.domain.usecase.splashscreen.FetchCheckoutUseCase
import com.foyer.hbc.domain.usecase.splashscreen.FetchConsumptionsUseCase
import com.foyer.hbc.domain.usecase.splashscreen.FetchPaymentsUseCase
import com.foyer.hbc.domain.usecase.splashscreen.FetchUserUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import org.koin.java.KoinJavaComponent.inject

class SplashScreenViewModel(
    application: Application,
    val database: HbcDatabase
) :
    BaseViewModel(application),
    ISplashScreenContract.ViewModel {

    ///////////////////////////////////////////////////////////////////////////
    // CONFIGURATION
    ///////////////////////////////////////////////////////////////////////////

    companion object {
        val SplashScreenModule = module {
            viewModel { SplashScreenViewModel(androidApplication(), get()) }
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // DEPENDENCY
    ///////////////////////////////////////////////////////////////////////////

    private val fetchCheckoutUseCase: FetchCheckoutUseCase by inject(FetchCheckoutUseCase::class.java)

    private val fetchUserUseCase: FetchUserUseCase by inject(FetchUserUseCase::class.java)

    private val fetchConsumptionsUseCase: FetchConsumptionsUseCase by inject(
        FetchConsumptionsUseCase::class.java
    )

    private val fetchPaymentsUseCase: FetchPaymentsUseCase by inject(
        FetchPaymentsUseCase::class.java
    )

    private val isNetworkAvailableUseCase: IsNetworkConnectionAvailableUseCase by inject(
        IsNetworkConnectionAvailableUseCase::class.java
    )

    ///////////////////////////////////////////////////////////////////////////
    // DATA
    ///////////////////////////////////////////////////////////////////////////

    // The UI collects from this StateFlow to get its state updates
    override val data: StateFlow<SplashScreenState>
        get() = mutableFLow

    // Backing property to avoid state updates from other classes
    private val mutableFLow = MutableStateFlow<SplashScreenState>(SplashScreenState.Loading)

    ///////////////////////////////////////////////////////////////////////////
    // SPECIALIZATION
    ///////////////////////////////////////////////////////////////////////////

    override fun fetchAndSaveData() {
        if (isNetworkAvailableUseCase.execute()) {
            database.clearAllTables()
            viewModelScope.launch {
                try {
                    joinAll(
                        this.launch { fetchCheckoutUseCase.execute() },
                        this.launch { fetchUserUseCase.execute() },
                        this.launch { fetchConsumptionsUseCase.execute() },
                        this.launch { fetchPaymentsUseCase.execute() }
                    )
                    mutableFLow.value = SplashScreenState.Success
                } catch (e: Exception) {
                    mutableFLow.value = SplashScreenState.Error(isNetworkAvailableUseCase.execute())
                }
            }
        } else {
            mutableFLow.value = SplashScreenState.Error(false)
        }
    }
}

sealed class SplashScreenState {
    object Loading : SplashScreenState()
    object Success : SplashScreenState()
    class Error(val isInternetAvailable: Boolean) : SplashScreenState()
}
