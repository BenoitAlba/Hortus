package org.alba.hortus.presentation.features.new

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import org.alba.hortus.domain.model.PlantDatabaseModel
import org.alba.hortus.presentation.features.new.usecases.AddPlantUseCase
import org.alba.hortus.presentation.features.new.usecases.CreatePlantImageFileUseCase
import org.alba.hortus.presentation.features.new.usecases.SearchCultivarUseCase

class AddPlantScreenViewModel(
    private val addPlantUseCase: AddPlantUseCase,
    private val searchCultivarUseCase: SearchCultivarUseCase,
    private val createPlantImageFileUseCase: CreatePlantImageFileUseCase
) : ScreenModel {

    // channel for one-time events
    private var _uiEffect = Channel<AddPlantScreenUIEffect>()
    val uiEffect = _uiEffect.receiveAsFlow()

    private var _uiState = MutableStateFlow<AddPlantScreenUIState>(AddPlantScreenUIState.NoSearch)
    val uiState: StateFlow<AddPlantScreenUIState> = _uiState

    var imageByteArray: ByteArray? = null
    var selectedPlant: PlantDatabaseModel? = null

    fun sendEvent(event: AddUIEvent) {
        when (event) {
            is AddUIEvent.AddClicked -> {
                createPlant(
                    event.exposure
                )
            }

            is AddUIEvent.SearchClicked -> {
                searchCultivar(
                    event.plantName,
                    event.description
                )
            }

            AddUIEvent.Clear -> {
                clear()
            }
        }
    }

    private fun clear() {
        selectedPlant = null
        _uiState.value = AddPlantScreenUIState.NoSearch
    }

    private fun searchCultivar(
        plantName: String,
        description: String?,
    ) {
        screenModelScope.launch {
            if (plantName.isBlank()) {
                _uiEffect.send(AddPlantScreenUIEffect.ShowToast("plant name is required"))
            } else {
                _uiState.value = AddPlantScreenUIState.Loading
                _uiState.value = AddPlantScreenUIState.SearchSuccess(
                    searchCultivarUseCase(plantName, description)
                )
            }
        }
    }

    private fun createPlant(
        exposure: String
    ) {
        screenModelScope.launch {
            if (exposure.isBlank()) {
                _uiEffect.send(AddPlantScreenUIEffect.ShowToast("Missing information, Common name and exposure are required"))
            } else {
                try {
                    val fileName = imageByteArray?.let {
                        createPlantImageFileUseCase(
                            it,
                            selectedPlant!!.scientificName ?: selectedPlant!!.commonName
                        )
                    }
                    addPlantUseCase(
                        plant = selectedPlant!!,
                        exposure,
                        fileName
                    )
                    _uiEffect.send(AddPlantScreenUIEffect.NavigateToHome("Plant added successfully !"))

                } catch (e: Exception) {
                    println("Error while adding plant: $e")
                    _uiEffect.send(AddPlantScreenUIEffect.NavigateToHome("Error while adding plant"))
                }
            }
        }
    }
}

sealed class AddUIEvent {
    data class AddClicked(
        val exposure: String
    ) : AddUIEvent()

    data class SearchClicked(
        val plantName: String,
        val description: String?
    ) : AddUIEvent()

    data object Clear : AddUIEvent()
}

sealed class AddPlantScreenUIEffect {
    data class ShowToast(val message: String) : AddPlantScreenUIEffect()
    data class NavigateToHome(val message: String) : AddPlantScreenUIEffect()
}

sealed class AddPlantScreenUIState {
    data object Loading : AddPlantScreenUIState()
    data object NoSearch : AddPlantScreenUIState()
    data class SearchSuccess(val plants: List<PlantDatabaseModel>) : AddPlantScreenUIState()
}