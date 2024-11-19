package io.github.junrdev.sage

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class SageApp : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@SageApp)
        }
    }
}