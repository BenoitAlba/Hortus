package org.alba.hortus.domain.model

import org.jetbrains.compose.resources.StringResource

sealed class LocationResult {
    data class Location(
        val latitude: Double,
        val longitude: Double,
        val country: String?,
        val locality: String?
    ) : LocationResult()

    data class Error(val messageResource: StringResource? = null, val message: String? = null) : LocationResult()
}
