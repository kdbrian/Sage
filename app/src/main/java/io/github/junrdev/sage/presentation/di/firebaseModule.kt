package io.github.junrdev.sage.presentation.di

import io.github.junrdev.sage.data.remote.repoImpl.FirebaseStorageDocumentsRepoImpl
import io.github.junrdev.sage.domain.repo.FirebaseStorageDocumentsRepo
import io.github.junrdev.sage.domain.repo.GenerativeAIRepo
import org.koin.dsl.module



val firebaseModule = module {

    single<FirebaseStorageDocumentsRepo> {
        FirebaseStorageDocumentsRepoImpl(
            get<GenerativeAIRepo>()
        )
    }

}