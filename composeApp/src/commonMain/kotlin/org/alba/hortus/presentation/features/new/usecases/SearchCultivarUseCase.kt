package org.alba.hortus.presentation.features.new.usecases

import kotlinx.serialization.json.Json
import org.alba.hortus.data.remote.GenerativeModel
import org.alba.hortus.domain.model.Exposure
import org.alba.hortus.domain.model.PlantDatabaseModel
import kotlin.random.Random

class SearchCultivarUseCase(
    private val generativeModel: GenerativeModel,
) {
    suspend operator fun invoke(
        plantName: String,
        description: String?,
    ): List<PlantDatabaseModel> {
        var entities = listOf<PlantDatabaseModel>()

        val response = generativeModel.generateJsonContent(
            buildQuery(plantName, description)
        )

        if (response != null) {
            entities = Json.decodeFromString<List<PlantDatabaseModel>>(response)
        }

        return entities.map {
            PlantDatabaseModel(
                id = Random.nextLong(),
                commonName = it.commonName,
                scientificName = it.scientificName,
                description = it.description,
                floweringMonths = it.floweringMonths,
                fruitingMonths = it.fruitingMonths,
                isAFruitPlant = it.isAFruitPlant,
                isAnAnnualPlant = it.isAnAnnualPlant,
                maxHeight = it.maxHeight,
                maxWidth = it.maxWidth,
                exposure = it.exposure,
                exposureAdvise = it.exposureAdvise,
                soilMoisture = it.soilMoisture,
                pollination = it.pollination,
                harvestMonths = it.harvestMonths,
                hardiness = it.hardiness,
            )
        }
    }
}

private fun buildQuery(name: String, description: String?) =
    QUERY_TEXT.replace(PLANT_NAME_QUERY_PARAM, name)
        .replace(PLANT_DESCRIPTION_PARAM, description ?: "")

private const val PLANT_NAME_QUERY_PARAM = "#plant"
private const val PLANT_DESCRIPTION_PARAM = "#description"

private val QUERY_TEXT =
    "give me the most exhaustive list of plant cultivars and varieties the plant: $PLANT_NAME_QUERY_PARAM and whose description is $PLANT_DESCRIPTION_PARAM" +
            "I would like to know its scientific name, a detailed description, " +
            "its flowering and fruiting periods (listed by month numbers), " +
            "its maximum height and width at maturity (in metric units in cm), " +
            "soil type (like Sandy soil, Loamy soil, Clay soil, etc.), hardiness: What is the lowest temperature this plant can survive (e.g., -20°C)," +
            " its harvest period if it is a fruit tree. my garden is in Belgium" +
            "and its exposure which could be one of the following ${Exposure.getAllNames()}"

