package org.alba.hortus.presentation.features.home

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.alba.hortus.domain.model.PlantDatabaseModel
import org.alba.hortus.presentation.features.home.usecases.GetPlantsUseCase

class HomeScreenViewModel(
    private val getPlantsUseCase: GetPlantsUseCase
) : ScreenModel {

    var _uiState: MutableStateFlow<HomeScreenUIState> = MutableStateFlow(HomeScreenUIState.Loading)
    val uiState: StateFlow<HomeScreenUIState> = _uiState

    // todo check how to handle errors with room
    fun getPlant() {
        screenModelScope.launch {
            try {
                _uiState.value = HomeScreenUIState.Success(getPlantsUseCase())
            } catch (e: Exception) {
                _uiState.value = HomeScreenUIState.Error(e.message ?: "Unknown error")
            }
        }
    }

    sealed class HomeScreenUIState {
        object Loading : HomeScreenUIState()
        data class Success(val plants: List<PlantDatabaseModel>) : HomeScreenUIState()
        data class Error(val message: String) : HomeScreenUIState()
    }
}