package org.alba.hortus.di

import org.alba.hortus.data.remote.PlantsApiService
import org.alba.hortus.presentation.features.home.HomeScreenViewModel
import org.alba.hortus.presentation.features.new.AddPlantScreenViewModel
import org.koin.core.context.startKoin
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.module

val appModule = module {
    factoryOf(::HomeScreenViewModel)
    factoryOf(::AddPlantScreenViewModel)
    singleOf(::PlantsApiService)
}

fun initKoin(config: KoinAppDeclaration? = null) =
    startKoin {
        config?.invoke(this)
        modules(
            appModule
        )
    }