package org.alba.hortus.presentation.features.edit

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import hortus.composeapp.generated.resources.Res
import hortus.composeapp.generated.resources.add_plant_info
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import org.alba.hortus.domain.model.PlantDatabaseModel
import org.alba.hortus.presentation.features.details.GetPlantUseCase
import org.alba.hortus.presentation.features.edit.usecases.EditPlantUseCase
import org.jetbrains.compose.resources.StringResource

class EditScreenViewModel(
    private val getPlantDetailsUseCase: GetPlantUseCase,
    private val editPlantUseCase: EditPlantUseCase,
) : ScreenModel {

    // channel for one-time events
    private var _uiEffect = Channel<EditPlantScreenUIEffect>()
    val uiEffect = _uiEffect.receiveAsFlow()

    private var _uiState: MutableStateFlow<EditScreenUIState> =
        MutableStateFlow(EditScreenUIState.Loading)
    val editUiState: StateFlow<EditScreenUIState> = _uiState

    fun sendEven(event: EditUIEvent) {
        when (event) {
            is EditUIEvent.SaveClicked -> {
                savePlant(event.plant)
            }
        }
    }

    private fun savePlant(plant: PlantDatabaseModel) {
        screenModelScope.launch {
            try {
                editPlantUseCase(plant)
                _uiEffect.send(EditPlantScreenUIEffect.NavigateToPlantDetails(plant.id))
            } catch (e: Exception) {
                _uiState.value = EditScreenUIState.Error(Res.string.add_plant_info)
            }
        }
    }

    fun getPlantDetails(plantId: Long) {
        screenModelScope.launch {
            try {
                getPlantDetailsUseCase(plantId)?.let {
                    _uiState.value = EditScreenUIState.Success(it)
                } ?: run {
                    _uiState.value = EditScreenUIState.Error(Res.string.add_plant_info)
                }
            } catch (e: Exception) {
                _uiState.value = EditScreenUIState.Error(Res.string.add_plant_info)
            }
        }
    }

    sealed class EditScreenUIState {
        data object Loading : EditScreenUIState()
        data class Success(val plant: PlantDatabaseModel) : EditScreenUIState()
        data class Error(val message: StringResource) : EditScreenUIState()
    }

    sealed class EditUIEvent {
        data class SaveClicked(val plant: PlantDatabaseModel) : EditUIEvent()
    }

    sealed class EditPlantScreenUIEffect {
        data class NavigateToPlantDetails(val id: Long) : EditPlantScreenUIEffect()
    }
}