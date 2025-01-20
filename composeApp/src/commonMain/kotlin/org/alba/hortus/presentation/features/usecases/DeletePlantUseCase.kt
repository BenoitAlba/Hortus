package org.alba.hortus.presentation.features.usecases

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import org.alba.hortus.data.local.plants.PlantLocalDataSource
import org.alba.hortus.presentation.managers.images.deleteFile

class DeletePlantUseCase(
    private val plantLocalDataSource: PlantLocalDataSource

) {
    suspend operator fun invoke(id: Long, fileName: String?) = with(Dispatchers.IO) {
        // delete file
        plantLocalDataSource.deletePlant(id)
        fileName?.let {
            deleteFile(fileName)
        }
    }
}