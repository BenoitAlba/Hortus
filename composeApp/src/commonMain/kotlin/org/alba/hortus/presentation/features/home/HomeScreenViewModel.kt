package org.alba.hortus.presentation.features.home

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.alba.hortus.domain.model.Forecast
import org.alba.hortus.domain.model.PlantDatabaseModel
import org.alba.hortus.domain.model.RequestState
import org.alba.hortus.presentation.features.home.usecases.GetForeCastUseCase
import org.alba.hortus.presentation.features.home.usecases.GetPlantsUseCase
import org.alba.hortus.presentation.features.usecases.DeletePlantUseCase
import org.jetbrains.compose.resources.StringResource

class HomeScreenViewModel(
    private val getPlantsUseCase: GetPlantsUseCase,
    private val deletePlantUseCase: DeletePlantUseCase,
    private val getForeCastUseCase: GetForeCastUseCase
) : ScreenModel {

    private var _plantUiState: MutableStateFlow<PlantUIState> =
        MutableStateFlow(PlantUIState.Loading)
    val plantUiState: StateFlow<PlantUIState> = _plantUiState

    private var _forecastUiState: MutableStateFlow<RequestState<Forecast>> =
        MutableStateFlow(RequestState.Loading)
    val forecastUiState: StateFlow<RequestState<Forecast>> = _forecastUiState

    fun initScreen() {
        getForecast(false)
        getPlants()
    }

    private fun getPlants() {
        screenModelScope.launch {
            try {
                _plantUiState.value = PlantUIState.Success(getPlantsUseCase())
            } catch (e: Exception) {
                _plantUiState.value = PlantUIState.Error(e.message ?: "Unknown error")
            }
        }
    }

    private fun getForecast(forceRefresh: Boolean) {
        _forecastUiState.value = RequestState.Loading
        screenModelScope.launch {
            _forecastUiState.value = getForeCastUseCase(forceRefresh)
        }
    }

    fun sendEvent(event: HomeUIEvent) {
        when (event) {
            is HomeUIEvent.DeletePlantClicked -> deletePlant(event.id, event.fileName)
            HomeUIEvent.RetryForecast -> {
                getForecast(true)
            }
        }
    }

    private fun deletePlant(id: Long, fileName: String?) {
        screenModelScope.launch {
            deletePlantUseCase(id, fileName)
            getPlants()
        }
    }

    sealed class PlantUIState {
        data object Loading : PlantUIState()
        data class Success(val plants: List<PlantDatabaseModel>) : PlantUIState()
        data class Error(val message: String) : PlantUIState()
    }

    sealed class HomeUIEvent {
        data class DeletePlantClicked(
            val id: Long,
            val fileName: String?
        ) : HomeUIEvent()

        data object RetryForecast : HomeUIEvent()
    }
}