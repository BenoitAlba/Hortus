package org.alba.hortus.data.local.forecast

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import org.alba.hortus.domain.model.Forecast

@Dao
interface ForecastDao {
    @Query("SELECT * FROM forecasts")
    suspend fun getForecasts(): List<Forecast>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertForecast(forecast: Forecast)

    @Update
    suspend fun updateForecast(forecast: Forecast)
}