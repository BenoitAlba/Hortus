package org.alba.hortus.data.local.plants

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import org.alba.hortus.domain.model.PlantDatabaseModel

@Dao
interface PlantDao {
    @Query("SELECT * FROM plants")
    suspend fun getAllPlants(): List<PlantDatabaseModel>

    @Update
    suspend fun updatePlant(plant: PlantDatabaseModel)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun createPlant(plant: PlantDatabaseModel)

    @Query("DELETE FROM plants WHERE id = :plantId")
    suspend fun deletePlantById(plantId: Long)

    @Query("SELECT * FROM plants WHERE id = :plantId")
    suspend fun getPlantById(plantId: Long): PlantDatabaseModel?
}