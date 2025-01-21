package org.alba.hortus.presentation.features.home.usecases

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import org.alba.hortus.data.local.plants.PlantLocalDataSource
import org.alba.hortus.domain.model.PlantListDataModel
import org.alba.hortus.presentation.features.home.transformers.PlantListTransformer

class GetPlantsUseCase(
    private val plantLocalDataSource: PlantLocalDataSource,
    private val plantListTransformer: PlantListTransformer

) {
    suspend operator fun invoke(lowestTemperature: Int?) = with(Dispatchers.IO) {
        plantListTransformer(lowestTemperature, plantLocalDataSource.getAllPlants())
    }
}