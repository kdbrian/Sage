package com.kdbrian.sage.di

import com.kdbrian.sage.ui.state.HomeScreenViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val homeModules = module {

    viewModelOf(::HomeScreenViewModel)
}