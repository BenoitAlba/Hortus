package org.alba.hortus.domain.model

sealed class LocationResult {
    data class Location(
        val latitude: Double,
        val longitude: Double,
        val country: String?,
        val locality: String?
    ) : LocationResult()

    data class Error(val message: String) : LocationResult()
}
