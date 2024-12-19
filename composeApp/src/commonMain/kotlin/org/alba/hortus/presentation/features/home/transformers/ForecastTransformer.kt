package org.alba.hortus.presentation.features.home.transformers

import org.alba.hortus.domain.model.Forecast
import org.alba.hortus.domain.model.ForecastItem
import org.alba.hortus.domain.model.LocationResult
import org.alba.hortus.domain.model.WeatherCode

class ForecastTransformer {
    operator fun invoke(
        locationResult: LocationResult.Location, forecast: ForecastItem
    ): Forecast = Forecast(
        country = locationResult.country ?: "Unknown",
        locality = locationResult.locality ?: "Unknown",
        temperatureMax = forecast.tmax,
        temperatureMin = forecast.tmin,
        weather = WeatherCode.fromCode(forecast.weather),
        windSpeed = forecast.wind10m,
        windDirection = degreesToCardinalDirection(forecast.dirwind10m),
    )
}

fun degreesToCardinalDirection(degrees: Int): String {
    // Normaliser l'angle entre 0 et 360
    val normalizedDegrees = (degrees % 360 + 360) % 360

    val directions = arrayOf(
        "N", "NNE", "NE", "ENE", "E", "ESE", "SE", "SSE",
        "S", "SSO", "SO", "OSO", "O", "ONO", "NO", "NNO"
    )

    // Calculer l'index dans le tableau des directions
    val index = (normalizedDegrees / 22.5).toInt()

    return directions[index % 16] // Utiliser modulo 16 pour gérer le cas 360° qui doit retourner N
}