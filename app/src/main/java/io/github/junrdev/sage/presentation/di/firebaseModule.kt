package io.github.junrdev.sage.presentation.di

import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import io.github.junrdev.sage.data.remote.repoImpl.FirebaseStorageDocumentsRepoImpl
import io.github.junrdev.sage.domain.repo.FirebaseStorageDocumentsRepo
import io.github.junrdev.sage.domain.repo.GenerativeAIRepo
import org.koin.dsl.module



val firebaseModule = module {

    single<FirebaseAuth> {
        Firebase.auth
    }

    single<FirebaseStorageDocumentsRepo> {
        FirebaseStorageDocumentsRepoImpl(
            get<GenerativeAIRepo>()
        )
    }

}