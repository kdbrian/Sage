package com.kdbrian.sage.di

import com.sage.data.remote.impl.TopicRepoImpl
import com.sage.domain.domain.repo.TopicRepo
import com.sage.library.ui.state.TopicDetailsViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val topicModules = module {

    single<TopicRepo> {
        TopicRepoImpl(

        )
    }

    viewModelOf(::TopicDetailsViewModel)


}