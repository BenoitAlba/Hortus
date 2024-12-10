package org.alba.hortus.presentation.features.details

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.alba.hortus.domain.model.PlantDatabaseModel

class PlantDetailsScreenViewModel(
    private val getPlantDetailsUseCase: GetPlantUseCase
) : ScreenModel {

    private var _uiState: MutableStateFlow<PlantDetailsUIState> =
        MutableStateFlow(PlantDetailsUIState.Loading)
    val uiState: StateFlow<PlantDetailsUIState> = _uiState

    fun getPlantDetails(plantId: Long) {
        screenModelScope.launch {
            getPlantDetailsUseCase.invoke(plantId)?.let {
                _uiState.value = PlantDetailsUIState.Success(it)
            } ?: run {
                _uiState.value = PlantDetailsUIState.Error("Plant not found")
            }
        }
    }

    sealed class PlantDetailsUIState {
        object Loading : PlantDetailsUIState()
        data class Success(val plant: PlantDatabaseModel) : PlantDetailsUIState()
        data class Error(val message: String) : PlantDetailsUIState()
    }
}