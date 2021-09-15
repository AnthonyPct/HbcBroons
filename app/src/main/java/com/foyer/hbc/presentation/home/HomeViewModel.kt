package com.foyer.hbc.presentation.home

import android.app.Application
import com.foyer.hbc.base.BaseViewModel
import com.foyer.hbc.domain.model.UserEntity
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

class HomeViewModel(application: Application) : BaseViewModel(application) {

    // This VM is used to shared data between Fragment. its scope is NavGraphScope

    ///////////////////////////////////////////////////////////////////////////
    // CONFIGURATION
    ///////////////////////////////////////////////////////////////////////////

    companion object {
        val HomeModule = module {
            viewModel { HomeViewModel(androidApplication()) }
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // DATA
    ///////////////////////////////////////////////////////////////////////////

    var userSelected: UserEntity? = null
}
