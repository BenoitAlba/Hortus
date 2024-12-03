package org.alba.hortus.presentation.features.new.usecases

import org.alba.hortus.data.local.PlantLocalDataSource
import org.alba.hortus.domain.model.PlantDatabaseModel

class AddPlantUseCase(
    private val plantLocalDataSource: PlantLocalDataSource
) {
    suspend operator fun invoke(
        commonName: String,
        scientificName: String?,
        description: String?,
        exposure: String
    ) {
        val plant = PlantDatabaseModel(
            commonName = commonName,
            scientificName = scientificName,
            description = description,
            currentExposure = exposure,
        )

        plantLocalDataSource.createPlant(plant)
        val plants = plantLocalDataSource.getAllPlants()
        println("---> $plants")
    }
}