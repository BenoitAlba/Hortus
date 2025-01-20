package org.alba.hortus.presentation.features.details

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import hortus.composeapp.generated.resources.Res
import hortus.composeapp.generated.resources.add_plant_info
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.alba.hortus.domain.model.PlantDatabaseModel
import org.jetbrains.compose.resources.StringResource

class PlantDetailsScreenViewModel(
    private val getPlantDetailsUseCase: GetPlantUseCase
) : ScreenModel {

    private var _uiState: MutableStateFlow<PlantDetailsUIState> =
        MutableStateFlow(PlantDetailsUIState.Loading)
    val uiState: StateFlow<PlantDetailsUIState> = _uiState

    fun getPlantDetails(plantId: Long) {
        screenModelScope.launch {
            try {
                getPlantDetailsUseCase.invoke(plantId)?.let {
                    _uiState.value = PlantDetailsUIState.Success(it)
                } ?: run {
                    _uiState.value = PlantDetailsUIState.Error(Res.string.add_plant_info)
                }
            } catch (e: Exception) {
                _uiState.value = PlantDetailsUIState.Error(Res.string.add_plant_info)

            }
        }
    }

    sealed class PlantDetailsUIState {
        object Loading : PlantDetailsUIState()
        data class Success(val plant: PlantDatabaseModel) : PlantDetailsUIState()
        data class Error(val message: StringResource) : PlantDetailsUIState()
    }
}