 package com.kdbrian.sage.di

import com.sage.data.remote.impl.DocumentRepoImpl
import com.sage.domain.domain.repo.DocumentRepo
import org.koin.dsl.module

 val documentModules = module {

    single<DocumentRepo> {
        DocumentRepoImpl(
        )
    }

//    viewModelOf(::DocumentDetailsScreenViewModel)


}