package org.alba.hortus.data.remote

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.alba.hortus.domain.model.ForecastItem
import org.alba.hortus.domain.model.RequestState
import org.alba.hortus.domain.model.WeatherResponse

class ForecastApiService {

    private val httpClient = HttpClient {
        install(ContentNegotiation) {
            json(Json {
                prettyPrint = true
                isLenient = true
                ignoreUnknownKeys = true
            })
        }
        install(HttpTimeout) {
            requestTimeoutMillis = 15000
        }
    }

    suspend fun getForecast(latitude: Double, longitude: Double): RequestState<ForecastItem> {
        return try {
            val response = httpClient.get("$ENDPOINT&latlng=$latitude,$longitude&world=true")
            if (response.status.value == 200) {
                val apiResponse = Json.decodeFromString<WeatherResponse>(response.body()).forecast
                RequestState.Success(data = apiResponse)
            } else {
                RequestState.Error(message = "HTTP Error Code: ${response.status} $response")
            }
        } catch (e: Exception) {
            RequestState.Error(e.message ?: "Something went wrong")
        }
    }

    companion object {
        private const val TOKEN = "da01cc7f405f301158fd7d6663a5a3115e8b5f75254434358147986ac68b544c"
        private const val ENDPOINT =
            "https://api.meteo-concept.com/api/forecast/daily/0?token=$TOKEN"
    }
}