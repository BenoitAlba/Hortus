package org.alba.hortus.data

import dev.jordond.compass.geocoder.Geocoder
import dev.jordond.compass.geocoder.placeOrNull
import dev.jordond.compass.geolocation.Geolocator
import dev.jordond.compass.geolocation.GeolocatorResult
import dev.jordond.compass.geolocation.mobile
import org.alba.hortus.domain.model.LocationResult

class LocationRepository {
    val geolocator: Geolocator = Geolocator.mobile()
    val geocoder = Geocoder()

    suspend operator fun invoke(): LocationResult =
        when (val result: GeolocatorResult = geolocator.current()) {
            is GeolocatorResult.Success -> {
                val place = geocoder.placeOrNull(
                    result.data.coordinates.latitude,
                    result.data.coordinates.longitude
                )
                LocationResult.Location(
                    latitude = result.data.coordinates.latitude,
                    longitude = result.data.coordinates.longitude,
                    country = place?.country,
                    locality = place?.locality
                )
            }

            is GeolocatorResult.Error -> when (result) {
                is GeolocatorResult.NotSupported -> {
                    LocationResult.Error("Geolocation not supported")
                }

                is GeolocatorResult.NotFound -> {
                    LocationResult.Error("Geolocation not found")
                }

                is GeolocatorResult.PermissionError -> {
                    LocationResult.Error("Geolocation permission error")
                }

                is GeolocatorResult.GeolocationFailed -> {
                    LocationResult.Error("Geolocation failed")
                }

                else -> {
                    LocationResult.Error("Geolocation failed ${result.message}\"")
                }
            }
        }
}