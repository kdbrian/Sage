package com.kdbrian.sage.di

import com.kdbrian.sage.data.remote.impl.DocumentRepoImpl
import com.kdbrian.sage.domain.repo.DocumentRepo
import com.kdbrian.sage.ui.state.DocumentDetailsScreenViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val documentModules = module {

    single<DocumentRepo> {
        DocumentRepoImpl(
            firebaseFirestore = get(),
            firebaseStorage = get()
        )
    }

    viewModelOf(::DocumentDetailsScreenViewModel)


}