package org.alba.hortus.presentation.features.home.usecases

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import org.alba.hortus.data.LocationRepository
import org.alba.hortus.domain.model.LocationResult

class GetForeCastUseCase(
    private val locationRepository: LocationRepository
) {
    suspend operator fun invoke() = with(Dispatchers.IO) {
        when (val location = locationRepository()) {
            is LocationResult.Error -> {
                println("Error: ${location.message}")
            }

            is LocationResult.Location -> {
                println("Location: ${location.latitude}, ${location.longitude}, ${location.country}, ${location.locality}")
            }
        }
    }
}