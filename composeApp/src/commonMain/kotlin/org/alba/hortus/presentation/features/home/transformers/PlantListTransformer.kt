package org.alba.hortus.presentation.features.home.transformers

import org.alba.hortus.domain.model.PlantDatabaseModel
import org.alba.hortus.domain.model.PlantListDataModel

class PlantListTransformer {

    operator fun invoke(
        lowestTemperature: Int?,
        plants: List<PlantDatabaseModel>
    ) = plants.map {
        PlantListDataModel(
            id = it.id,
            commonName = it.commonName,
            scientificName = it.scientificName ?: "",
            currentExposure = it.currentExposure,
            hardiness = it.hardiness,
            image = it.image,
            isTemperatureTooLow = lowestTemperature != null && it.hardiness != null && lowestTemperature < it.hardiness
        )
    }
}