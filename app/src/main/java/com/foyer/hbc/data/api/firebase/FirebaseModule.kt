package com.foyer.hbc.data.api.firebase

import org.koin.dsl.module

val firebaseRepoModule = module {
    single<FirebaseRepository> { FirebaseRepositoryImpl() }
}
