package com.foyer.hbc.data.api

import com.foyer.hbc.data.api.ffhb.FfhbRepository
import com.foyer.hbc.data.api.ffhb.FfhbRepositoryImpl
import com.foyer.hbc.data.api.ffhb.IffhbApi
import com.squareup.moshi.Moshi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

const val baseUrl = "https://jjht57whqb.execute-api.us-west-2.amazonaws.com"

val ApiModule = module {
    val connectTimeout: Long = 20// 20s
    val readTimeout: Long = 20 // 20s

    fun provideHttpClient(): OkHttpClient {
        val client = OkHttpClient.Builder()
        client.addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        })
        client.readTimeout(readTimeout, TimeUnit.SECONDS)
        client.writeTimeout(connectTimeout, TimeUnit.SECONDS)
        return client
            .build()
    }

    fun provideRetrofit(client: OkHttpClient, baseUrl: String): Retrofit {
        val moshi = Moshi.Builder()
            .build()

        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .client(client)
            .build()
    }

    fun provideMatchApi(retrofit: Retrofit): IffhbApi {
        return retrofit.create(IffhbApi::class.java)
    }

    fun provideFfhRepository(api: IffhbApi): FfhbRepository {
        return FfhbRepositoryImpl(api)
    }

    single { provideHttpClient() }
    single { provideRetrofit(get(), baseUrl) }
    single { provideMatchApi(get()) }
    single { provideFfhRepository(get()) }
}