package com.kdbrian.sage.di

import com.kdbrian.sage.ui.state.SearchResultsScreenViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val searchModules = module {
    viewModelOf(::SearchResultsScreenViewModel)
}