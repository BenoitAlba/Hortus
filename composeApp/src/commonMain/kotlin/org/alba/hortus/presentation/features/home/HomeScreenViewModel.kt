package org.alba.hortus.presentation.features.home

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.alba.hortus.domain.model.PlantDatabaseModel
import org.alba.hortus.presentation.features.home.usecases.GetForeCastUseCase
import org.alba.hortus.presentation.features.home.usecases.GetPlantsUseCase
import org.alba.hortus.presentation.features.usecases.DeletePlantUseCase

class HomeScreenViewModel(
    private val getPlantsUseCase: GetPlantsUseCase,
    private val deletePlantUseCase: DeletePlantUseCase,
    private val getForeCastUseCase: GetForeCastUseCase
) : ScreenModel {

    private var _uiState: MutableStateFlow<HomeScreenUIState> =
        MutableStateFlow(HomeScreenUIState.Loading)
    val uiState: StateFlow<HomeScreenUIState> = _uiState

    fun getPlant() {
        screenModelScope.launch {
            try {
                getForeCastUseCase()
                _uiState.value = HomeScreenUIState.Success(getPlantsUseCase())
            } catch (e: Exception) {
                _uiState.value = HomeScreenUIState.Error(e.message ?: "Unknown error")
            }
        }
    }

    fun sendEvent(event: HomeUIEvent) {
        when (event) {
            is HomeUIEvent.DeletePlantClicked -> deletePlant(event.id, event.fileName)
        }
    }

    private fun deletePlant(id: Long, fileName: String?) {
        screenModelScope.launch {
            deletePlantUseCase(id, fileName)
            getPlant()
        }
    }

    sealed class HomeScreenUIState {
        object Loading : HomeScreenUIState()
        data class Success(val plants: List<PlantDatabaseModel>) : HomeScreenUIState()
        data class Error(val message: String) : HomeScreenUIState()
    }

    sealed class HomeUIEvent {
        data class DeletePlantClicked(
            val id: Long,
            val fileName: String?
        ) : HomeUIEvent()
    }
}