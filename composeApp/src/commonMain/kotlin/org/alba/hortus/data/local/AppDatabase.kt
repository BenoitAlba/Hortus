package org.alba.hortus.data.local

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor
import org.alba.hortus.data.local.forecast.ForecastDao
import org.alba.hortus.data.local.plants.PlantDao
import org.alba.hortus.domain.model.Forecast
import org.alba.hortus.domain.model.PlantDatabaseModel

@Database(entities = [PlantDatabaseModel::class, Forecast::class], version = 1)
@ConstructedBy(AppDatabaseConstructor::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun getPlantDao(): PlantDao
    abstract fun getForecastDao(): ForecastDao
}

@Suppress("NO_ACTUAL_FOR_EXPECT")
expect object AppDatabaseConstructor : RoomDatabaseConstructor<AppDatabase> {
    override fun initialize(): AppDatabase
}
