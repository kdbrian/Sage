package com.kdbrian.sage.di

import com.kdbrian.sage.data.remote.impl.DocumentRepoImpl
import com.kdbrian.sage.domain.repo.DocumentRepo
import org.koin.dsl.module

val documentModules = module {

    single<DocumentRepo> {
        DocumentRepoImpl(
            firebaseFirestore = get()
        )
    }

}