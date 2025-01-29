package org.alba.hortus.presentation.features.location.usecases

import dev.jordond.compass.autocomplete.Autocomplete
import dev.jordond.compass.autocomplete.mobile
import org.alba.hortus.domain.model.LocationResult

class GetLocationsForNameUseCase {
    /**
     * Searches for locations matching a given search query.
     *
     * @param query The search query to perform.
     * @return A list of location results, or an empty list if no matches are found.
     */
    suspend operator fun invoke(query: String): List<LocationResult> {
        // Uses the Compass library to perform autocomplete search
        val autocomplete = Autocomplete.mobile()
        return autocomplete.search(query).getOrNull()?.map { place ->
            LocationResult.Location(
                latitude = place.coordinates.latitude,
                longitude = place.coordinates.longitude,
                country = place.country,
                locality = place.locality
            )
        }.orEmpty()
    }
}
