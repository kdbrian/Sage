package com.kdbrian.sage.di

import com.sage.datastore.AppDataStore
import org.koin.dsl.module

val coreModules = module {

    single {
        AppDataStore(
            context = get()
        )
    }

}