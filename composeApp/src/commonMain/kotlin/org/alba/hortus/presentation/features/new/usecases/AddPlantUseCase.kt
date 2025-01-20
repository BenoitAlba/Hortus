package org.alba.hortus.presentation.features.new.usecases

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import org.alba.hortus.data.local.plants.PlantLocalDataSource
import org.alba.hortus.data.remote.GenerativeModel
import org.alba.hortus.domain.model.PlantDatabaseModel

class AddPlantUseCase(
    private val plantLocalDataSource: PlantLocalDataSource,
    private val generativeModel: GenerativeModel,
) {
    suspend operator fun invoke(
        plant: PlantDatabaseModel,
        exposure: String,
        fileName: String?
    ) {
        with(Dispatchers.IO) {
            var exposureAdvise: String? = null
            try {
                exposureAdvise =
                    generativeModel.generateTextContent(
                        buildExposureQuery(
                            plant,
                            plant.scientificName ?: plant.commonName,
                            exposure
                        )
                    )?.trimEnd()
            } catch (e: Exception) {
                println("---> error $e")
            }

            val copyPlant = plant.copy(
                currentExposure = exposure,
                image = fileName,
                exposureAdvise = exposureAdvise,
            )
            plantLocalDataSource.createPlant(copyPlant)
        }
    }
}

private fun buildExposureQuery(entity: PlantDatabaseModel, commonName: String, exposure: String) =
    QUERY_EXPOSURE
        .replace(COMMON_NAME_PARAM, commonName)
        .replace(SCIENTIFIC_NAME_PARAM, entity.scientificName ?: "")
        .replace(EXPOSURES_PARAM, entity.exposure.toString())
        .replace(CURRENT_EXPOSURE, exposure)


private const val COMMON_NAME_PARAM = "#commonName"
private const val SCIENTIFIC_NAME_PARAM = "#scientificName"
private const val EXPOSURES_PARAM = "#exposures"
private const val CURRENT_EXPOSURE = "#exposure"

val QUERY_EXPOSURE = "the plant named $COMMON_NAME_PARAM $SCIENTIFIC_NAME_PARAM " +
        "where the exposure should be $EXPOSURES_PARAM is placed at the $CURRENT_EXPOSURE, " +
        "please give your advises, this answer will be displayed as is in a garden managing app" +
        "be concise, max 2 sentences.  my garden is in Belgium"

