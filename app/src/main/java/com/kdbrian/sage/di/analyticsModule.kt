package com.kdbrian.sage.di

import com.sage.data.remote.impl.AnalyticsAdapterImpl
import com.sage.domain.domain.repo.AnalyticsAdapter
import org.koin.dsl.module

val analyticsModule = module {
    single<AnalyticsAdapter> {
        AnalyticsAdapterImpl(
            analytics = get()
        )
    }
}