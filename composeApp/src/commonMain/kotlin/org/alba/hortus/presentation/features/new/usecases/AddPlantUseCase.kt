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
            try {
                val response = generativeModel.generateJsonContent(buildQuery(commonName))
                if (response != null) {
                    entities = Json.decodeFromString<List<PlantDatabaseModel>>(response)
                }
            } catch (e: Exception) {
                println("---> error $e")
            }

            val plant = PlantDatabaseModel(
                commonName = commonName,
                scientificName = scientificName
                    ?: if (entities.isNotEmpty()) entities[0].scientificName else null,
                description = description
                    ?: if (entities.isNotEmpty()) entities[0].description else null,
                floweringMonths = if (entities.isNotEmpty()) entities[0].floweringMonths else null,
                fruitingMonths = if (entities.isNotEmpty()) entities[0].fruitingMonths else null,
                isAFruitPlant = if (entities.isNotEmpty()) entities[0].isAFruitPlant else null,
                isAnAnnualPlant = if (entities.isNotEmpty()) entities[0].isAnAnnualPlant else null,
                maxHeight = if (entities.isNotEmpty()) entities[0].maxHeight else null,
                maxWidth = if (entities.isNotEmpty()) entities[0].maxWidth else null,
                exposure = if (entities.isNotEmpty()) entities[0].exposure else null,
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

private fun buildQuery(commonName: String) =
    QUERY_TEXT.addStringAtIndex(commonName, QUERY_TEXT.indexOf(':') + 1)

private fun String.addStringAtIndex(string: String, index: Int) =
    StringBuilder(this).apply { insert(index, string) }.toString()

val QUERY_TEXT =
    "Provide me with information about the following plant: ." +
            "I would like to know its scientific name, a detailed description, " +
            "its flowering and fruiting periods (listed by month numbers), " +
            "its maximum height and width at maturity (in metric units), " +
            "its ideal exposure, (base on: ${Exposure.getAllNames()}) " +
            "soil type (like Sandy soil, Loamy soil, Clay soil, etc.), hardiness: What is the lowest temperature this plant can survive (e.g., -20°C)," +
            " and its harvest period if it is a fruit tree."
