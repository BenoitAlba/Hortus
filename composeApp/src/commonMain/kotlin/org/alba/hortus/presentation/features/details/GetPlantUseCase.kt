package org.alba.hortus.presentation.features.details

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext
import org.alba.hortus.data.local.plants.PlantLocalDataSource

class GetPlantUseCase(
    private val plantLocalDataSource: PlantLocalDataSource,
) {
    suspend operator fun invoke(id: Long) = withContext(Dispatchers.IO) { plantLocalDataSource.getPlantById(id) }
}