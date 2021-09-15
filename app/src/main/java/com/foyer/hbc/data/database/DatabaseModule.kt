package com.foyer.hbc.data.database

import android.app.Application
import androidx.room.Room
import com.foyer.hbc.data.database.dao.*
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

val databaseModule = module {
    fun provideDatabase(application: Application): HbcDatabase {
        return Room.databaseBuilder(application, HbcDatabase::class.java, "hbc.database")
            .fallbackToDestructiveMigration()
            .allowMainThreadQueries()
            .build()
    }

    fun provideDao(database: HbcDatabase): UserDAO {
        return database.userDAO
    }

    fun provideConsumptionDAO(database: HbcDatabase): ConsumptionDAO {
        return database.messageDAO
    }

    fun providePaymentDAO(database: HbcDatabase): PaymentDAO {
        return database.paymentDAO
    }

    fun provideMatchDAO(database: HbcDatabase): MatchDAO {
        return database.matchDAO
    }

    fun provideCheckoutDAO(database: HbcDatabase): CheckoutDAO {
        return database.checkoutDAO
    }

    single { provideDatabase(androidApplication()) }
    single { provideDao(get()) }
    single { provideConsumptionDAO(get()) }
    single { providePaymentDAO(get()) }
    single { provideMatchDAO(get()) }
    single { provideCheckoutDAO(get()) }
    single<DatabaseRepository> { DatabaseRepositoryImpl(get(), get(), get(), get(), get()) }
}
