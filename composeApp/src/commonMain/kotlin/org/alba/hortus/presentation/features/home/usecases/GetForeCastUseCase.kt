package org.alba.hortus.presentation.features.home.usecases

import hortus.composeapp.generated.resources.Res
import hortus.composeapp.generated.resources.forecast_location_error
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext
import org.alba.hortus.data.LocationRepository
import org.alba.hortus.data.remote.ForecastRepository
import org.alba.hortus.domain.model.Forecast
import org.alba.hortus.domain.model.LocationResult
import org.alba.hortus.domain.model.RequestState

class GetForeCastUseCase(
    private val locationRepository: LocationRepository,
    private val forecastRepository: ForecastRepository,
) {
    suspend operator fun invoke(forceRefresh: Boolean = false): RequestState<Forecast> =
        withContext(Dispatchers.IO) {
            when (val location = locationRepository.getLocation()) {
                is LocationResult.Error -> {
                    RequestState.Error(location.messageResource)
                }

                is LocationResult.Location -> {
                    if (AVAILABLE_COUNTRIES.contains(location.country.toString())) {
                        forecastRepository.getForecast(location, forceRefresh)
                    } else {
                        RequestState.Error(Res.string.forecast_location_error)
                    }
                }

                null ->
                    when (val geoLocation = locationRepository.getGeoLocationAndUpdateLocation()) {
                        is LocationResult.Error -> {
                            RequestState.Error(messageResource = geoLocation.messageResource)
                        }

                        is LocationResult.Location -> {
                            if (AVAILABLE_COUNTRIES.contains(geoLocation.country.toString())) {
                                forecastRepository.getForecast(geoLocation, forceRefresh)
                            } else {
                                RequestState.Error(Res.string.forecast_location_error)
                            }
                        }
                    }
            }
        }
}

const val AVAILABLE_COUNTRIES = "France, Belgium, Luxembourg, Belgique"