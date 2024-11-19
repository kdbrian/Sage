package io.github.junrdev.sage.presentation.di

import com.google.ai.client.generativeai.GenerativeModel
import io.github.junrdev.sage.BuildConfig
import io.github.junrdev.sage.data.remote.repoImpl.GenerativeAIRepoImpl
import io.github.junrdev.sage.domain.repo.GenerativeAIRepo
import io.github.junrdev.sage.util.Constants
import org.koin.dsl.module

val apiKey = BuildConfig.geminiApiKey

val geminiModule = module {

    single<GenerativeModel> {
        GenerativeModel(
            Constants.MODEL_NAME,
            apiKey = apiKey,
        )
    }

    single<GenerativeAIRepo> {
        GenerativeAIRepoImpl(get())
    }
}