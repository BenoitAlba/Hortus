package org.alba.hortus.domain.model

data class PlantListDataModel(
    val id: Long,
    val commonName: String,
    val scientificName: String,
    val currentExposure: String,
    val hardiness: Float?,
    val image: String?,
    val isTemperatureTooLow: Boolean = false
)
