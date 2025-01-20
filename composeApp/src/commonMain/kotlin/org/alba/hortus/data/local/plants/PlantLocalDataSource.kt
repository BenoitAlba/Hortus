package org.alba.hortus.data.local.plants

import org.alba.hortus.data.local.AppDatabase
import org.alba.hortus.domain.model.PlantDatabaseModel

class PlantLocalDataSource(
    private val appDatabase: AppDatabase
) {
    suspend fun getAllPlants(): List<PlantDatabaseModel> = appDatabase.getPlantDao().getAllPlants()
    suspend fun createPlant(plant: PlantDatabaseModel) = appDatabase.getPlantDao().createPlant(plant)
    suspend fun deletePlant(id: Long) = appDatabase.getPlantDao().deletePlantById(id)
    suspend fun updatePlant(plant: PlantDatabaseModel) = appDatabase.getPlantDao().updatePlant(plant)
    suspend fun getPlantById(id: Long) = appDatabase.getPlantDao().getPlantById(id)
}