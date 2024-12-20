package org.alba.hortus.presentation.features.home.usecases

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import org.alba.hortus.data.LocationRepository
import org.alba.hortus.data.remote.ForecastApiService
import org.alba.hortus.domain.model.Forecast
import org.alba.hortus.domain.model.LocationResult
import org.alba.hortus.domain.model.RequestState
import org.alba.hortus.presentation.features.home.transformers.ForecastTransformer

class GetForeCastUseCase(
    private val locationRepository: LocationRepository,
    private val forecastApiService: ForecastApiService,
    private val transformer: ForecastTransformer,
) {
    suspend operator fun invoke(): RequestState<Forecast> = with(Dispatchers.IO) {
        when (val location = locationRepository()) {
            is LocationResult.Error -> {
                RequestState.Error(location.message)
            }

            is LocationResult.Location -> {
                if (AVAILABLE_COUNTRIES.contains(location.country.toString())) {
                    val response =
                        forecastApiService.getForecast(location.latitude, location.longitude)
                    when (response) {
                        is RequestState.Error -> {
                            RequestState.Error(response.message)
                        }

                        RequestState.Loading -> {
                            RequestState.Loading
                        }

                        is RequestState.Success -> {
                            RequestState.Success(transformer(location, response.data))
                        }
                    }
                } else {
                    RequestState.Error("The forecast API is only available in France, Belgium and Luxembourg")
                }
            }
        }
    }
}

const val AVAILABLE_COUNTRIES = "France, Belgium, Luxembourg, Belgique"