package org.alba.hortus.data.remote

import kotlinx.datetime.Clock
import org.alba.hortus.data.local.forecast.ForecastLocalDataSource
import org.alba.hortus.domain.model.Forecast
import org.alba.hortus.domain.model.ForecastItem
import org.alba.hortus.domain.model.LocationResult
import org.alba.hortus.domain.model.RequestState
import org.alba.hortus.presentation.features.home.transformers.ForecastTransformer

class ForecastRepository(
    private val forecastApiService: ForecastApiService,
    private val forecastLocalDataSource: ForecastLocalDataSource,
    private val transformer: ForecastTransformer,
) {
    suspend fun getForecast(
        location: LocationResult.Location,
        forceRefresh: Boolean = false
    ): RequestState<Forecast> {
        var forecast = forecastLocalDataSource.getForecast()
        if (forecast == null) {
            val remoteForecast = getRemoteForecast(location)

            if (remoteForecast != null) {
                forecast = transformer.invoke(
                    location,
                    remoteForecast
                )
                forecastLocalDataSource.createForecast(forecast)
            }
        } else {
            val now = Clock.System.now().epochSeconds
            if (isOneHourElapsed(
                    forecast.updatedAt,
                    now
                ) || forceRefresh || (location.locality != forecast.locality)
            ) {
                val remoteForecast = getRemoteForecast(location)
                if (remoteForecast != null) {
                    forecast = transformer.invoke(
                        location,
                        remoteForecast,
                        forecast.id,
                        now
                    )
                    forecastLocalDataSource.updateForecast(forecast)
                } else {
                    RequestState.Error("No remote forecast found")
                }
            }
        }
        return if (forecast == null) {
            RequestState.Error("No forecast found")
        } else {
            RequestState.Success(forecast)
        }
    }

    private fun isOneHourElapsed(timestamp1: Long, timestamp2: Long): Boolean {
        val oneHourInSeconds = 3600L
        return timestamp2 - timestamp1 >= oneHourInSeconds
    }

    private suspend fun getRemoteForecast(location: LocationResult.Location): ForecastItem? {
        val response =
            forecastApiService.getForecast(location.latitude, location.longitude)
        return when (response) {
            is RequestState.Error -> {
                println("error: ${response.message}")
                null
            }

            is RequestState.Success -> {
                response.data
            }

            else -> {
                null
            }
        }
    }
}

