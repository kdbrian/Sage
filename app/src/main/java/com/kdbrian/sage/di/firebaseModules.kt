package com.kdbrian.sage.di

import com.kdbrian.sage.data.remote.impl.FirebaseCategoryRepoImpl
import com.kdbrian.sage.data.remote.impl.FirebaseDocumentRepo
import com.kdbrian.sage.domain.repo.CategoryRepo
import com.kdbrian.sage.domain.repo.DocumentRepo
import org.koin.dsl.module

val firebaseModules = module {

    single<DocumentRepo> {
        FirebaseDocumentRepo(
            appActivityService = get()
        )
    }

    single<CategoryRepo> {
        FirebaseCategoryRepoImpl(
        )
    }

}