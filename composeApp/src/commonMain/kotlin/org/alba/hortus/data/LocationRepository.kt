package org.alba.hortus.data

import com.russhwolf.settings.ExperimentalSettingsApi
import com.russhwolf.settings.ObservableSettings
import com.russhwolf.settings.Settings
import com.russhwolf.settings.coroutines.FlowSettings
import com.russhwolf.settings.coroutines.toFlowSettings
import dev.jordond.compass.geocoder.Geocoder
import dev.jordond.compass.geocoder.placeOrNull
import dev.jordond.compass.geolocation.Geolocator
import dev.jordond.compass.geolocation.GeolocatorResult
import dev.jordond.compass.geolocation.mobile
import hortus.composeapp.generated.resources.Res
import hortus.composeapp.generated.resources.geolocation_failed
import hortus.composeapp.generated.resources.geolocation_failed_with_message
import hortus.composeapp.generated.resources.geolocation_not_found
import hortus.composeapp.generated.resources.geolocation_not_supported
import hortus.composeapp.generated.resources.geolocation_permission_error
import org.alba.hortus.domain.model.LocationResult

@OptIn(ExperimentalSettingsApi::class)
class LocationRepository {
    private val geolocator: Geolocator = Geolocator.mobile()
    private val geocoder = Geocoder()
    private val settings: Settings = Settings()

    @OptIn(ExperimentalSettingsApi::class)
    private val flowSettings: FlowSettings = (settings as ObservableSettings).toFlowSettings()

    suspend fun updateLocation(location: LocationResult.Location) {
        flowSettings.putDouble(LOCATION_LATITUDE_KEY, location.latitude)
        flowSettings.putDouble(LOCATION_LONGITUDE_KEY, location.longitude)
    }

    suspend fun clearLocation() {
        flowSettings.putDouble(LOCATION_LATITUDE_KEY, 0.0)
        flowSettings.putDouble(LOCATION_LONGITUDE_KEY, 0.0)
    }

    suspend fun getLocation(withGeoLocation: Boolean = false): LocationResult? {
        val latitude = flowSettings.getDoubleOrNull(LOCATION_LATITUDE_KEY)
        val longitude = flowSettings.getDoubleOrNull(LOCATION_LONGITUDE_KEY)

        return if (
            (latitude != null && longitude != null) &&
            (latitude != 0.0 && longitude != 0.0) &&
            !withGeoLocation
        ) {
            val place = geocoder.placeOrNull(
                latitude = latitude,
                longitude = longitude
            )
            LocationResult.Location(
                latitude = latitude,
                longitude = longitude,
                country = place?.country,
                locality = place?.locality
            )
        } else {
            null
        }
    }

    suspend fun getGeoLocationAndUpdateLocation(): LocationResult {
        return when (val result: GeolocatorResult = geolocator.current()) {
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
                ).also {
                    updateLocation(it)
                }
            }

            is GeolocatorResult.Error -> when (result) {
                is GeolocatorResult.NotSupported -> {
                    LocationResult.Error(Res.string.geolocation_not_supported)
                }

                is GeolocatorResult.NotFound -> {
                    LocationResult.Error(Res.string.geolocation_not_found)
                }

                is GeolocatorResult.PermissionError -> {
                    LocationResult.Error(Res.string.geolocation_permission_error)
                }

                is GeolocatorResult.GeolocationFailed -> {
                    LocationResult.Error(Res.string.geolocation_failed)
                }

                else -> {
                    LocationResult.Error(Res.string.geolocation_failed_with_message, result.message)
                }
            }
        }
    }

    private companion object {
        const val LOCATION_LATITUDE_KEY = "location_latitude"
        const val LOCATION_LONGITUDE_KEY = "location_longitude"

    }
}