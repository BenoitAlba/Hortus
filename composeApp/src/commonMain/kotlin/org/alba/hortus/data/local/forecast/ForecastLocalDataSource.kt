package org.alba.hortus.data.local.forecast

import org.alba.hortus.data.local.AppDatabase
import org.alba.hortus.domain.model.Forecast

class ForecastLocalDataSource(
    private val appDatabase: AppDatabase,
) {
    suspend fun getForecast() = appDatabase.getForecastDao().getForecasts().firstOrNull()
    suspend fun createForecast(forecast: Forecast) =
        appDatabase.getForecastDao().insertForecast(forecast)

    suspend fun updateForecast(forecast: Forecast) =
        appDatabase.getForecastDao().updateForecast(forecast)

}