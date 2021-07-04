package com.example.hbc.base

import android.app.Application
import androidx.multidex.MultiDexApplication

class HbcApplication : MultiDexApplication() {

    override fun onCreate() {
        super.onCreate()

        /*if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        startKoin {
            androidLogger()
            androidContext(this@BaseApplication)
            modules(
                listOf(
                    prefModule,
                    homeViewModelModule,
                    firebaseRepoModule,
                    databaseModule,
                    useCaseModule
                )
            )
        }

        Stetho.initializeWithDefaults(this)*/
    }
}
