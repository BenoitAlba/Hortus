package org.alba.hortus.presentation.features.new.usecases

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import org.alba.hortus.data.local.PlantLocalDataSource
import org.alba.hortus.domain.model.PlantDatabaseModel

class AddPlantUseCase(
    private val plantLocalDataSource: PlantLocalDataSource
) {
    suspend operator fun invoke(
        commonName: String,
        scientificName: String?,
        description: String?,
        exposure: String,
        fileName: String?
    ) {
        with(Dispatchers.IO) {
            val plant = PlantDatabaseModel(
                commonName = commonName,
                scientificName = scientificName,
                description = description,
                currentExposure = exposure,
                image = fileName,
            )
            plantLocalDataSource.createPlant(plant)
        }
    }
}