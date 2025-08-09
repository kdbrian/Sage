package com.kdbrian.sage.di

import com.kdbrian.sage.ui.state.CreateScreenViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val createDocumentModules = module {
    viewModelOf(::CreateScreenViewModel)
}