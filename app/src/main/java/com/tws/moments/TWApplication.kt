package com.tws.moments

import android.app.Application
import com.tws.moments.data.imageloader.ImageLoader
import com.tws.moments.di.AppInject
import com.tws.moments.utils.ScreenAdaptiveUtil
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class TWApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        ScreenAdaptiveUtil.adaptive(this)

        startKoin {
            androidContext(this@TWApplication)
            modules(provideDependency())
        }
    }

    private fun provideDependency() = AppInject.modules()
}
