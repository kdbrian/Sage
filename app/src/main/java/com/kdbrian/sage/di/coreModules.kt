package com.kdbrian.sage.di

import com.kdbrian.sage.data.local.SageDb
import com.kdbrian.sage.data.local.impl.AppActivityServiceImpl
import com.kdbrian.sage.domain.repo.AppActivityService
import com.kdbrian.sage.data.datastore.AppDataStore
import com.kdbrian.sage.presentation.viewmodel.MainViewModel
import kotlinx.serialization.json.Json
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val coreModules = module {

    single {
        Json {
            ignoreUnknownKeys=true
        }
    }
    single {
        AppDataStore(
            context = get(),
            json = get(),
        )
    }

    single<SageDb> {
        SageDb.getDb(
            context = get()
        )
    }

    single<AppActivityService> {
        AppActivityServiceImpl(
            get()
        )
    }


    viewModelOf(::MainViewModel)

}