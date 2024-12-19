package org.alba.hortus.domain.model

data class Forecast(
    val country: String,
    val locality: String,
    val temperatureMax: Int,
    val temperatureMin: Int,
    val weather: WeatherCode?,
    val windSpeed: Int,
    val windDirection: String
)