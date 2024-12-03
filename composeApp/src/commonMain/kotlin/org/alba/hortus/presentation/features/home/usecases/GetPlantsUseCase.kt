package org.alba.hortus.presentation.features.home.usecases

import org.alba.hortus.data.local.PlantLocalDataSource

class GetPlantsUseCase(
    private val plantLocalDataSource: PlantLocalDataSource

) {
    suspend operator fun invoke() = plantLocalDataSource.getAllPlants()
}