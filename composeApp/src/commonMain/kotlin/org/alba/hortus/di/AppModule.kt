package org.alba.hortus.di

import org.alba.hortus.presentation.features.home.HomeScreenViewModel
import org.alba.hortus.presentation.features.new.AddPlantScreenViewModel
import org.alba.hortus.data.local.plants.PlantLocalDataSource
import org.alba.hortus.data.local.forecast.ForecastLocalDataSource
import org.alba.hortus.data.remote.ForecastRepository
import org.alba.hortus.data.remote.ForecastApiService
import org.alba.hortus.presentation.features.new.usecases.AddPlantUseCase
import org.alba.hortus.presentation.features.new.usecases.SearchCultivarUseCase
import org.alba.hortus.presentation.features.home.usecases.GetPlantsUseCase
import org.alba.hortus.presentation.features.new.usecases.CreatePlantImageFileUseCase
import org.alba.hortus.presentation.features.usecases.DeletePlantUseCase
import org.alba.hortus.presentation.features.details.PlantDetailsScreenViewModel
import org.alba.hortus.presentation.features.details.GetPlantUseCase
import org.alba.hortus.presentation.features.home.transformers.ForecastTransformer
import org.alba.hortus.presentation.features.home.transformers.PlantListTransformer
import org.alba.hortus.presentation.features.home.usecases.GetForeCastUseCase
import org.alba.hortus.presentation.features.home.usecases.UpdatePlantListForTemperatureUseCase
import org.alba.hortus.presentation.features.location.LocationScreenViewModel
import org.alba.hortus.presentation.features.location.usecases.GetLocationsForNameUseCase
import org.alba.hortus.data.LocationRepository
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.module

expect fun platformModule(): Module

val appModule = module {
    singleOf(::ForecastApiService)
    singleOf(::ForecastLocalDataSource)
    singleOf(::ForecastRepository)
    singleOf(::PlantLocalDataSource)
    singleOf(::LocationRepository)

    // feature home
    factoryOf(::HomeScreenViewModel)
    factoryOf(::GetPlantsUseCase)
    factoryOf(::DeletePlantUseCase)
    factoryOf(::GetForeCastUseCase)
    factoryOf(::UpdatePlantListForTemperatureUseCase)
    factoryOf(::ForecastTransformer)
    factoryOf(::PlantListTransformer)

    // feature add
    factoryOf(::AddPlantScreenViewModel)
    factoryOf(::AddPlantUseCase)
    factoryOf(::SearchCultivarUseCase)
    factoryOf(::CreatePlantImageFileUseCase)

    // feature details
    factoryOf(::PlantDetailsScreenViewModel)
    factoryOf(::GetPlantUseCase)

    // feature location
    factoryOf(::LocationScreenViewModel)
    factoryOf(::GetLocationsForNameUseCase)
}

fun initKoin(config: KoinAppDeclaration? = null) =
    startKoin {
        config?.invoke(this)
        modules(
            appModule,
            platformModule()
        )
    }