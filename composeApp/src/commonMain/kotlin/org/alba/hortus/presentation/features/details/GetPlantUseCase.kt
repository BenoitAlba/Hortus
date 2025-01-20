package org.alba.hortus.presentation.features.details

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import org.alba.hortus.data.local.plants.PlantLocalDataSource

class GetPlantUseCase(
    private val plantLocalDataSource: PlantLocalDataSource,
) {
    suspend fun invoke(id: Long) = with(Dispatchers.IO) { plantLocalDataSource.getPlantById(id) }
}