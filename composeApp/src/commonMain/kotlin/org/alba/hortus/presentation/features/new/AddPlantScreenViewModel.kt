package org.alba.hortus.presentation.features.new

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import org.alba.hortus.presentation.features.new.usecases.AddPlantUseCase
import org.alba.hortus.presentation.features.new.usecases.CreatePlantImageFileUseCase

class AddPlantScreenViewModel(
    private val addPlantUseCase: AddPlantUseCase,
    private val createPlantImageFileUseCase: CreatePlantImageFileUseCase
) : ScreenModel {

    // channel for one-time events
    private var _uiEffect = Channel<AddPlantScreenUIEffect>()
    val uiEffect = _uiEffect.receiveAsFlow()

    var imageByteArray: ByteArray? = null

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
        }
    }

    private fun createPlant(
        commonName: String,
        scientificName: String?,
        description: String?,
        exposure: String
    ) {
        screenModelScope.launch {
            if (commonName.isBlank() || exposure.isBlank()) {
                _uiEffect.send(AddPlantScreenUIEffect.ShowToast("Missing information, Common name and exposure are required"))
            } else {
                var fileName = imageByteArray?.let {
                     createPlantImageFileUseCase(it, commonName)
                }

                addPlantUseCase.invoke(commonName, scientificName, description, exposure, fileName)
                _uiEffect.send(AddPlantScreenUIEffect.NavigateToHome("Plant added successfully !"))
            }
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
}

sealed class AddPlantScreenUIEffect {
    data class ShowToast(val message: String) : AddPlantScreenUIEffect()
    data class NavigateToHome(val message: String) : AddPlantScreenUIEffect()
}