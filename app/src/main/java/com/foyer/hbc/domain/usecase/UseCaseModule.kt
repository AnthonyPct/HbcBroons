package com.foyer.hbc.domain.usecase

import com.foyer.hbc.domain.usecase.common.IsNetworkConnectionAvailableUseCase
import com.foyer.hbc.domain.usecase.dashboard.AddConsumptionUseCase
import com.foyer.hbc.domain.usecase.dashboard.GetCarouseCellsUseCase
import com.foyer.hbc.domain.usecase.dashboard.GetHistoricItemUseCase
import com.foyer.hbc.domain.usecase.splashscreen.FetchCheckoutUseCase
import com.foyer.hbc.domain.usecase.splashscreen.FetchConsumptionsUseCase
import com.foyer.hbc.domain.usecase.splashscreen.FetchPaymentsUseCase
import com.foyer.hbc.domain.usecase.splashscreen.FetchUserUseCase
import com.foyer.hbc.domain.usecase.users.AddPaymentUseCase
import com.foyer.hbc.domain.usecase.users.AddUserUseCase
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val UseCaseModule = module {
    single { FetchUserUseCase(get(), get()) }
    single { FetchConsumptionsUseCase(get(), get()) }
    single { IsNetworkConnectionAvailableUseCase(androidContext()) }
    single { FetchCheckoutUseCase(get(), get()) }
    single { FetchPaymentsUseCase(get(), get()) }
    single { GetCarouseCellsUseCase(get()) }
    single { GetHistoricItemUseCase(get()) }
    single { AddConsumptionUseCase(get(), get()) }
    single { AddPaymentUseCase(get(), get()) }
    single { AddUserUseCase(get(), get()) }
}
