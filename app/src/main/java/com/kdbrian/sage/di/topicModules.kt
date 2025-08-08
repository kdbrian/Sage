package com.kdbrian.sage.di

import com.kdbrian.sage.data.remote.impl.TopicRepoImpl
import com.kdbrian.sage.domain.repo.TopicRepo
import org.koin.dsl.module

val topicModules = module {

    single<TopicRepo> {
        TopicRepoImpl(
            firestore = get()
        )
    }


}