package org.alba.hortus.presentation.features.new

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import org.alba.hortus.presentation.features.new.usecases.AddPlantUseCase

class AddPlantScreenViewModel(
    private val addPlantUseCase: AddPlantUseCase
) : ScreenModel {

    // channel for one-time events
    private var _uiEffect = Channel<AddPlantScreenUIEffect>()
    val uiEffect = _uiEffect.receiveAsFlow()

    fun sendEvent(event: AddUIEvent) {
        when (event) {
            is AddUIEvent.AddClicked -> {
                createPlant(
                    event.commonName,
                    event.scientificName,
                    event.description,
                    event.exposure
                )
            }
            AddUIEvent.MissingInfo -> {
                screenModelScope.launch {
                    _uiEffect.send(AddPlantScreenUIEffect.ShowToast("Missing information, Common name and exposure are required"))
                }
            }
        }
    }

    private fun createPlant(
        commonName: String,
        scientificName: String?,
        description: String?,
        exposure: String
    ) {
        screenModelScope.launch(Dispatchers.IO) {
            addPlantUseCase(commonName, scientificName, description, exposure)
        }
        screenModelScope.launch {
            _uiEffect.send(AddPlantScreenUIEffect.NavigateToHome("Plant added successfully !"))
        }
    }
}

sealed class AddUIEvent {
    data class AddClicked(
        val commonName: String,
        val scientificName: String?,
        val description: String?,
        val exposure: String
    ) : AddUIEvent()

    data object MissingInfo : AddUIEvent()
}

sealed class AddPlantScreenUIEffect {
    data class ShowToast(val message: String) : AddPlantScreenUIEffect()
    data class NavigateToHome(val message: String) : AddPlantScreenUIEffect()
}