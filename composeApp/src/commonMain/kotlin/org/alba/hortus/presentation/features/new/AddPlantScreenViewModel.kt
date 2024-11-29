package org.alba.hortus.presentation.features.new

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.launch
import org.alba.hortus.data.local.PlantLocalDataSource
import org.alba.hortus.domain.model.PlantDatabaseModel

class AddPlantScreenViewModel(
    private val plantLocalDataSource: PlantLocalDataSource
) : ScreenModel {

    init {
       screenModelScope.launch(Dispatchers.IO) {
            createPlant(
                PlantDatabaseModel(
                    commonName = "test",
                    floweringPeriod = listOf("sun", "partial shade")
                )
            )
        }
    }

    private suspend fun createPlant(plant: PlantDatabaseModel) {
        plantLocalDataSource.createPlant(plant)
        val plants = plantLocalDataSource.getAllPlants()
        println("---> $plants")
    }
}