package com.tws.moments.di

import android.content.Context
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.tws.moments.TWApplication
import com.tws.moments.data.imageloader.ImageLoader
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
import okhttp3.Response
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.module.Module
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.util.concurrent.TimeUnit

object AppInject {

    private const val BASE_URL = "http://10.0.2.2:2727/"

    fun modules(): List<Module> =
        ArrayList<Module>().apply {
            add(networkModule)
            add(dataModule)
            add(viewModelsModule)
        }

    private val viewModelsModule = module {
        single { dispatchersProvider() }
        viewModel { MainViewModel(get(), get()) }
    }

    private val dataModule = module {
        single<MomentRepository> { MomentRepositoryImpl(get()) }
    }

    private val networkModule = module {
        single { provideOkHttpClient(androidApplication()) }
        single { provideRetrofit(BASE_URL, androidApplication(), get()) }
        single { provideMomentService(get()) }
        single { provideImageLoader(get()) }
    }

    private fun dispatchersProvider(): CoroutineDispatcher = Dispatchers.Main

    private fun provideRetrofit(baseUrl: String, context: Context, okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .client(okHttpClient)
            .build()
    }

    private fun provideOkHttpClient(context: Context): OkHttpClient {
        val cacheSize = 10 * 1024 * 1024 // 10 MiB
        val httpCacheDirectory: File = File(context.cacheDir, "http-cache")
        val cache = Cache(httpCacheDirectory, cacheSize.toLong())

        return OkHttpClient.Builder()
            .addNetworkInterceptor(createCacheInterceptor())
            .cache(cache)
            .build()
    }

    private fun provideMomentService(retrofit: Retrofit): MomentService {
        return retrofit.create(MomentService::class.java)
    }

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

    private fun provideImageLoader(context: Context): ImageLoader {
        return object : ImageLoader {
            override fun displayImage(url: String?, imageView: ImageView) {
                if (!url.isNullOrEmpty()) {
                    Glide.with(context)
                        .load(url)
                        .into(imageView)
                }
            }
        }
    }
}
