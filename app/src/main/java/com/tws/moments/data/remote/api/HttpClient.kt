package com.tws.moments.data.remote.api

import com.tws.moments.TWApplication
import okhttp3.Cache
import okhttp3.OkHttpClient
import java.io.File

object HttpClient {
    private var httpCacheDirectory: File = File(TWApplication.app.cacheDir, "http-cache")
    private var cacheSize = 10 * 1024 * 1024 // 10 MiB

    private var cache = Cache(httpCacheDirectory, cacheSize.toLong())
    var okHttpClient: OkHttpClient = OkHttpClient.Builder()
        .addNetworkInterceptor(CacheInterceptor)
        .cache(cache)
        .build()
}