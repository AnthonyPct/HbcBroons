package com.foyer.hbc.presentation.splashscreen

import kotlinx.coroutines.flow.StateFlow

interface ISplashScreenContract {

    interface ViewModel {
        fun fetchAndSaveData()
        val data: StateFlow<SplashScreenState>
    }
}
