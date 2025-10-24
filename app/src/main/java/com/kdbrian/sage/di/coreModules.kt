package com.kdbrian.sage.di

import com.kdbrian.sage.data.local.AppDataStore
import org.koin.dsl.module

val coreModules = module {

    single {
        AppDataStore(
            context = get()
        )
    }

}