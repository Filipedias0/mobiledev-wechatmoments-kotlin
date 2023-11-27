package com.tws.moments.di

import com.tws.moments.data.remote.api.MomentService
import okhttp3.mockwebserver.MockWebServer
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Retrofit Koin DI component for Instrumentation Testing
 */
fun configureNetworkForInstrumentationTest(mockWebServer: MockWebServer) = module (override = true) {

    single {
        Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    single { get<Retrofit>().create(MomentService::class.java) }
}

