package org.alba.hortus.data.remote

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.alba.hortus.domain.model.Plant
import org.alba.hortus.domain.model.PlantApiResponse
import org.alba.hortus.domain.model.RequestState

class PlantsApiService {

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


    suspend fun getPlantsForName(search: String): RequestState<List<Plant>> {
        return try {
            val response = httpClient.get("$ENDPOINT&q=coconut")
            if (response.status.value == 200) {
                val apiResponse = Json.decodeFromString<PlantApiResponse>(response.body()).data
                RequestState.Success(data = apiResponse)
            } else {
                RequestState.Error(message = "HTTP Error Code: ${response.status} $response")
            }
        } catch (e: Exception) {
            RequestState.Error(e.message ?: "Something went wrong")

        }
    }


    companion object {
        const val TOKEN = "5aNx1laaU7_xhth5_AcIpnQCYZh5C2jd2dL19fMclE8"
        const val ENDPOINT = "https://trefle.io/api/v1/plants/search?token=$TOKEN"
        const val API_KEY = "sk-sJtN674727925eea47802"
    }
}