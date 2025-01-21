package org.alba.hortus.presentation.features.home.usecases

import org.alba.hortus.domain.model.Forecast
import org.alba.hortus.domain.model.PlantListDataModel
import org.alba.hortus.domain.model.RequestState
import org.alba.hortus.presentation.features.home.HomeScreenViewModel.PlantUIState

class UpdatePlantListForTemperatureUseCase {
    suspend operator fun invoke(
        plantUiState: PlantUIState,
        forecast: RequestState<Forecast>
    ) = if (forecast is RequestState.Success && plantUiState is PlantUIState.Success) {
        PlantUIState.Success(
            plantUiState.plants.map { plant ->
                PlantListDataModel(
                    id = plant.id,
                    commonName = plant.commonName,
                    scientificName = plant.scientificName ?: "",
                    currentExposure = plant.currentExposure,
                    hardiness = plant.hardiness,
                    image = plant.image,
                    isTemperatureTooLow = plant.hardiness != null && forecast.data.temperatureMin < plant.hardiness
                )
            }
        )
    } else {
        plantUiState
    }
}