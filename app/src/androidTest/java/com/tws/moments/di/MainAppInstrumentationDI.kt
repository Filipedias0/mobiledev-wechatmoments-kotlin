package com.tws.moments.di

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.tws.moments.TWApplication
import com.tws.moments.data.remote.api.MomentService
import com.tws.moments.data.repository.MomentRepositoryImpl
import com.tws.moments.domain.repository.MomentRepository
import com.tws.moments.presentation.viewModels.MainViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import okhttp3.Cache
import okhttp3.CacheControl
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockWebServer
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import java.io.File
import java.util.concurrent.TimeUnit

/**
 * Main Koin DI component for Instrumentation Testing
 */

val MockWebServerInstrumentationTest = module(override = true) {
    factory {
        MockWebServer()
    }

}

private val dataModule = module(override = true) {
    single<MomentRepository> { MomentRepositoryImpl(get()) }
}

private val viewModelsModule = module(override = true) {
    single { dispatchersProvider() }
    viewModel { MainViewModel(get(), get()) }
}

private val networkModule = module {
    single {
        createHttpClient<OkHttpClient>(get(), get())
    }

    single {
       gsonProvider()
    }
}

private fun dispatchersProvider(): CoroutineDispatcher = Dispatchers.Main

private fun gsonProvider(): Gson = GsonBuilder().create()

private fun createCacheInterceptor(): Interceptor {
    return Interceptor { chain ->
        val response = chain.proceed(chain.request())

        val cacheControl = CacheControl.Builder()
            .maxAge(15, TimeUnit.MINUTES) // 15 minutes cache
            .build()

        response.newBuilder()
            .removeHeader("Pragma")
            .removeHeader("Cache-Control")
            .header("Cache-Control", cacheControl.toString())
            .build()
    }
}


private fun httpCache(
    application: TWApplication
): Cache {
    val httpCacheDirectory = File(application.cacheDir, "http-cache")
    val cacheSize = 10 * 1024 * 1024 // 10 MiB

    return Cache(httpCacheDirectory, cacheSize.toLong())
}

private inline fun <reified T> createHttpClient(
    cacheInterceptor: Interceptor,
    httpCache: Cache
): T {
    return OkHttpClient.Builder()
        .addNetworkInterceptor(cacheInterceptor)
        .cache(httpCache)
        .build() as T
}

fun generateTestAppComponent(mockWebServer: MockWebServer)
        = listOf(
    configureNetworkForInstrumentationTest(mockWebServer),
    MockWebServerInstrumentationTest,
    viewModelsModule,
    dataModule
)

