package org.alba.hortus.di

import org.alba.hortus.presentation.features.home.HomeScreenViewModel
import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.module

val appModule = module {
    factory {
        HomeScreenViewModel()
    }
}

fun initKoin(config: KoinAppDeclaration? = null) =
    startKoin {
        config?.invoke(this)
        modules(
            appModule
        )
    }