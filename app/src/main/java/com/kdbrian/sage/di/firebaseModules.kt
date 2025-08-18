package com.kdbrian.sage.di

import android.content.Context
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import org.koin.dsl.module

val firebaseModules = module {

    single {
        FirebaseAnalytics.getInstance(get<Context>())
    }


    single {
        FirebaseFirestore.getInstance()
    }

    single {
        FirebaseStorage.getInstance()
    }

}