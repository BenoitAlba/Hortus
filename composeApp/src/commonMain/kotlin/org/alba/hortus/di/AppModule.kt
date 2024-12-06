package org.alba.hortus.di

import org.alba.hortus.data.remote.PlantsApiService
import org.alba.hortus.presentation.features.home.HomeScreenViewModel
import org.alba.hortus.presentation.features.new.AddPlantScreenViewModel
import org.alba.hortus.data.local.PlantLocalDataSource
import org.alba.hortus.presentation.features.new.usecases.AddPlantUseCase
import org.alba.hortus.presentation.features.home.usecases.GetPlantsUseCase
import org.alba.hortus.presentation.features.new.usecases.CreatePlantImageFileUseCase
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.module

expect fun platformModule(): Module

val appModule = module {
    singleOf(::PlantsApiService)
    singleOf(::PlantLocalDataSource)

    // feature home
    factoryOf(::HomeScreenViewModel)
    factoryOf(::GetPlantsUseCase)

    // feature add
    factoryOf(::AddPlantScreenViewModel)
    factoryOf(::AddPlantUseCase)
    factoryOf(::CreatePlantImageFileUseCase)
}

fun initKoin(config: KoinAppDeclaration? = null) =
    startKoin {
        config?.invoke(this)
        modules(
            appModule,
            platformModule()
        )
    }