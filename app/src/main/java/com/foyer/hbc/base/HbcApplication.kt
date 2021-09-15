package com.foyer.hbc.base

import androidx.multidex.MultiDexApplication
import com.foyer.hbc.data.api.ApiModule
import com.foyer.hbc.data.api.firebase.firebaseRepoModule
import com.foyer.hbc.data.database.databaseModule
import com.foyer.hbc.domain.usecase.UseCaseModule
import com.foyer.hbc.presentation.ViewModelModule
import com.google.firebase.firestore.FirebaseFirestore
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class HbcApplication : MultiDexApplication() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@HbcApplication)
            modules(
                databaseModule,
                ApiModule,
                firebaseRepoModule,
                UseCaseModule,
                * ViewModelModule.toTypedArray()
            )
        }
        FirebaseFirestore.setLoggingEnabled(false)
    }
}
