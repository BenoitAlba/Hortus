package org.alba.hortus.data.local

import org.alba.hortus.domain.model.PlantDatabaseModel

class PlantLocalDataSource(
    private val appDatabase: AppDatabase
) {
    suspend fun getAllPlants(): List<PlantDatabaseModel> = appDatabase.getDao().getAllPlants()
    suspend fun createPlant(plant: PlantDatabaseModel) = appDatabase.getDao().createPlant(plant)
    suspend fun deletePlant(id: Long) = appDatabase.getDao().deletePlantById(id)
    suspend fun updateNote(note: PlantDatabaseModel) = appDatabase.getDao().updatePlant(note)
    suspend fun getPlantById(id: Long) = appDatabase.getDao().getPlantById(id)
}