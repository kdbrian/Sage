package com.kdbrian.sage

import android.app.Application
import com.kdbrian.sage.di.analyticsModule
import com.kdbrian.sage.di.coreModules
import com.kdbrian.sage.di.createDocumentModules
import com.kdbrian.sage.di.documentModules
import com.kdbrian.sage.di.firebaseModules
import com.kdbrian.sage.di.homeModules
import com.kdbrian.sage.di.searchModules
import com.kdbrian.sage.di.topicModules
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import timber.log.Timber

class Sage : Application() {
    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG)
            Timber.plant(Timber.DebugTree())

        startKoin {

            androidLogger()
            androidContext(this@Sage)

            modules(
                analyticsModule,
                coreModules,
                createDocumentModules,
                documentModules,
                firebaseModules,
                homeModules,
                searchModules,
                topicModules,
            )
        }
    }
}