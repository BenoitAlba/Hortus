package org.alba.hortus.presentation.features.new.usecases

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.serialization.json.Json
import org.alba.hortus.data.local.PlantLocalDataSource
import org.alba.hortus.data.remote.GenerativeModel
import org.alba.hortus.domain.model.Exposure
import org.alba.hortus.domain.model.PlantDatabaseModel

class AddPlantUseCase(
    private val plantLocalDataSource: PlantLocalDataSource,
    private val generativeModel: GenerativeModel,
) {
    suspend operator fun invoke(
        commonName: String,
        scientificName: String?,
        description: String?,
        exposure: String,
        fileName: String?
    ) {
        with(Dispatchers.IO) {
            var entities = listOf<PlantDatabaseModel>()
            var exposureAdvise: String? = null
            try {
                val response = generativeModel.generateJsonContent(buildQuery(commonName))
                if (response != null) {
                    entities = Json.decodeFromString<List<PlantDatabaseModel>>(response)
                    exposureAdvise =
                        generativeModel.generateTextContent(
                            buildExposureQuery(entities[0], commonName, exposure)
                        )?.trimEnd()
                }
            } catch (e: Exception) {
                println("---> error $e")
            }

            val plant = PlantDatabaseModel(
                commonName = if (entities.isNotEmpty()) entities[0].commonName else commonName,
                scientificName = if (scientificName.isNullOrBlank()) {
                    entities[0].scientificName
                } else {
                    scientificName
                },
                description = if (description.isNullOrBlank()) {
                    entities[0].description?.trimEnd()
                } else {
                    description
                },
                floweringMonths = if (entities.isNotEmpty()) entities[0].floweringMonths else null,
                fruitingMonths = if (entities.isNotEmpty()) entities[0].fruitingMonths else null,
                isAFruitPlant = if (entities.isNotEmpty()) entities[0].isAFruitPlant else null,
                isAnAnnualPlant = if (entities.isNotEmpty()) entities[0].isAnAnnualPlant else null,
                maxHeight = if (entities.isNotEmpty()) entities[0].maxHeight else null,
                maxWidth = if (entities.isNotEmpty()) entities[0].maxWidth else null,
                exposure = if (entities.isNotEmpty()) entities[0].exposure else null,
                exposureAdvise = if (entities.isNotEmpty()) exposureAdvise else null,
                soilMoisture = if (entities.isNotEmpty()) entities[0].soilMoisture else null,
                pollination = if (entities.isNotEmpty()) entities[0].pollination else null,
                harvestMonths = if (entities.isNotEmpty()) entities[0].harvestMonths else null,
                hardiness = if (entities.isNotEmpty()) entities[0].hardiness else null,
                currentExposure = exposure,
                image = fileName,
            )
            plantLocalDataSource.createPlant(plant)
        }
    }
}

private fun buildQuery(commonName: String) = QUERY_TEXT.replace(PLANT_NAME_QUERY_PARAM, commonName)

private fun buildExposureQuery(entity: PlantDatabaseModel, commonName: String, exposure: String) =
    QUERY_EXPOSURE
        .replace(COMMON_NAME_PARAM, commonName)
        .replace(SCIENTIFIC_NAME_PARAM, entity.scientificName ?: "")
        .replace(EXPOSURES_PARAM, entity.exposure.toString())
        .replace(CURRENT_EXPOSURE, exposure)


const val PLANT_NAME_QUERY_PARAM = "#plant"
const val COMMON_NAME_PARAM = "#commonName"
const val SCIENTIFIC_NAME_PARAM = "#scientificName"
const val EXPOSURES_PARAM = "#exposures"
const val CURRENT_EXPOSURE = "#exposure"

// to provide the garden location
val QUERY_TEXT =
    "Provide me with information about the following plant: $PLANT_NAME_QUERY_PARAM." +
            "I would like to know its scientific name, a detailed description, " +
            "its flowering and fruiting periods (listed by month numbers), " +
            "its maximum height and width at maturity (in metric units in cm), " +
            "its ideal exposures, (base on: ${Exposure.getAllNames()}) " +
            "soil type (like Sandy soil, Loamy soil, Clay soil, etc.), hardiness: What is the lowest temperature this plant can survive (e.g., -20°C)," +
            " and its harvest period if it is a fruit tree. my garden is in Belgium"

val QUERY_EXPOSURE = "the plant named $COMMON_NAME_PARAM $SCIENTIFIC_NAME_PARAM " +
        "where the exposure should be $EXPOSURES_PARAM is placed at the $CURRENT_EXPOSURE, " +
        "please give your advises, this answer will be displayed as is in a garden managing app" +
        "be concise, max 2 sentences.  my garden is in Belgium"

