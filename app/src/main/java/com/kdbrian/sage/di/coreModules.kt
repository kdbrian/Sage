package com.kdbrian.sage.di

import com.kdbrian.sage.util.AppDataStore
import org.koin.dsl.module

val coreModules = module {

    single {
        AppDataStore(
            context = get()
        )
    }

}