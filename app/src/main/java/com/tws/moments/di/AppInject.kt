package com.tws.moments.di

import android.content.Context
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.tws.moments.TWApplication
import com.tws.moments.data.imageloader.ImageLoader
import com.tws.moments.data.remote.api.MomentService
import com.tws.moments.data.remote.api.RetrofitInstance
import com.tws.moments.data.repository.MomentRepositoryImpl
import com.tws.moments.domain.repository.MomentRepository
import com.tws.moments.presentation.viewModels.MainViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import okhttp3.Cache
import okhttp3.CacheControl
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.module.Module
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.util.concurrent.TimeUnit

object AppInject {
    private val BASE_URL = "http://10.0.2.2:2727/"

    fun modules(): List<Module> =
        ArrayList<Module>().apply {
            add(networkModule)
            add(dataModule)
            //add(useCasesModule)
            add(viewModelsModule)
        }

    private val viewModelsModule = module {
        single { dispatchersProvider() }
        viewModel { MainViewModel(get(), get()) }
    }

    private val dataModule = module {
        single<MomentRepository> { MomentRepositoryImpl(RetrofitInstance.momentService) }
    }

    private val networkModule = module {
        single {
            createHttpClient<OkHttpClient>(get(), get())
        }

        single {
            gsonProvider()
        }

        single {
            createService<MomentService>(get(), get())
        }

        single{
            glideImageLoader(get())
        }
    }

    private fun dispatchersProvider(): CoroutineDispatcher = Dispatchers.Main

    private fun gsonProvider(): Gson = GsonBuilder().create()
    private fun callAdapterFactory(): CoroutineCallAdapterFactory = CoroutineCallAdapterFactory()

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

    private inline fun <reified T> createService(
        okHttpClient: OkHttpClient,
        gson: Gson
    ): T {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .client(okHttpClient)
            .build()
            .create(T::class.java)
    }

    private fun glideImageLoader(context: Context): ImageLoader {
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