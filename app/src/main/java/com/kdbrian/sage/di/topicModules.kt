package com.kdbrian.sage.di

import com.kdbrian.sage.data.remote.impl.TopicRepoImpl
import com.kdbrian.sage.domain.repo.TopicRepo
import com.kdbrian.sage.ui.state.TopicDetailsViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val topicModules = module {

    single<TopicRepo> {
        TopicRepoImpl(
            firestore = get()
        )
    }

    viewModelOf(::TopicDetailsViewModel)


}