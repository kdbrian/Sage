package com.kdbrian.sage.di

import android.content.Context
import com.google.firebase.analytics.FirebaseAnalytics
import org.koin.dsl.module

val firebaseModules = module {

    single {
        FirebaseAnalytics.getInstance(get<Context>())
    }


}