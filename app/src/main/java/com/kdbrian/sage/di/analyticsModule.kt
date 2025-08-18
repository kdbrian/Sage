package com.kdbrian.sage.di

import com.kdbrian.sage.data.remote.impl.AnalyticsAdapterImpl
import com.kdbrian.sage.domain.repo.AnalyticsAdapter
import org.koin.dsl.module

val analyticsModule = module {
    single<AnalyticsAdapter> {
        AnalyticsAdapterImpl(
            analytics = get()
        )
    }
}