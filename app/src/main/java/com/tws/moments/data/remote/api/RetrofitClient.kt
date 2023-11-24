package com.tws.moments.data.remote.api

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory




object RetrofitInstance {

    private val BASE_URL = "http://10.0.2.2:2727/"

    private val okHttpClient = HttpClient.okHttpClient

         private val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .client(okHttpClient)
            .build()


        val momentService: MomentService by lazy {
            retrofit.create(MomentService::class.java)
    }
}
