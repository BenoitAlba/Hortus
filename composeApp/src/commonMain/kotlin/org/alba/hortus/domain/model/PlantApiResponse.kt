package org.alba.hortus.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Plant constructor(
    @SerialName("id")
    val id: Int,
    @SerialName("common_name")
    val commonName: String,
    @SerialName("scientific_name")
    val scientificName: List<String>,
    @SerialName("other_name")
    val otherNames: List<String>,
    val cycle: String,
    val watering: String,
    val sunlight: List<String> = listOf(),
    @SerialName("default_image")
    val defaultImage: DefaultImage
)

@Serializable
data class DefaultImage(
    val license: Int,
    @SerialName("license_name")
    val licenseName: String,
    @SerialName("license_url") val
    licenseUrl: String,
    @SerialName("original_url")
    val originalUrl: String,
    @SerialName("regular_url")
    val regularUrl: String,
    @SerialName("medium_url")
    val mediumUrl: String,
    @SerialName("small_url")
    val smallUrl: String,
    val thumbnail: String
)

@Serializable
data class PlantApiResponse(
    val data: List<Plant>,
    val to: Int,
    @SerialName("per_page")
    val perPage: Int,
    @SerialName("current_page")
    val currentPage: Int,
    @SerialName("from")
    val from: Int,
    @SerialName("last_page")
    val lastPage: Int,
    val total: Int
)