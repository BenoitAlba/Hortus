package org.alba.hortus.presentation.features.home.usecases

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import org.alba.hortus.data.local.plants.PlantLocalDataSource

class GetPlantsUseCase(
    private val plantLocalDataSource: PlantLocalDataSource

) {
    suspend operator fun invoke() = with(Dispatchers.IO) {
        plantLocalDataSource.getAllPlants()
    }
}