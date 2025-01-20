package org.alba.hortus.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Serializable
@Entity(tableName = "forecasts")
data class Forecast(
    @PrimaryKey(autoGenerate = true) val id: Long = 0L,
    val country: String,
    val locality: String,
    val temperatureMax: Int,
    val temperatureMin: Int,
    val weather: WeatherCode?,
    val windSpeed: Int,
    val windDirection: String,
    val updatedAt: Long = 0,
)