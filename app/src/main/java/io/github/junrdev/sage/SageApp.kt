package io.github.junrdev.sage

import android.app.Application
import io.github.junrdev.sage.presentation.di.firebaseModule
import io.github.junrdev.sage.presentation.di.geminiModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class SageApp : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@SageApp)
            modules(
                firebaseModule,
                geminiModule
            )
        }
    }
}