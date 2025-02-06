package org.alba.hortus.presentation.features.edit.usecases

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import org.alba.hortus.data.local.plants.PlantLocalDataSource
import org.alba.hortus.domain.model.PlantDatabaseModel

class EditPlantUseCase(
    private val plantLocalDataSource: PlantLocalDataSource,
) {
    suspend operator fun invoke(plant: PlantDatabaseModel) = with(Dispatchers.IO) { plantLocalDataSource.updatePlant(plant) }
}