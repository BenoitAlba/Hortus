package org.alba.hortus.presentation.features.home

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.auth.auth
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import org.alba.hortus.domain.model.Forecast
import org.alba.hortus.domain.model.PlantListDataModel
import org.alba.hortus.domain.model.RequestState
import org.alba.hortus.presentation.features.home.usecases.GetForeCastUseCase
import org.alba.hortus.presentation.features.home.usecases.GetPlantsUseCase
import org.alba.hortus.presentation.features.home.usecases.UpdatePlantListForTemperatureUseCase
import org.alba.hortus.presentation.features.usecases.DeletePlantUseCase

class HomeScreenViewModel(
    private val getPlantsUseCase: GetPlantsUseCase,
    private val deletePlantUseCase: DeletePlantUseCase,
    private val getForeCastUseCase: GetForeCastUseCase,
    private val updatePlantListForTemperatureUseCase: UpdatePlantListForTemperatureUseCase

) : ScreenModel {
    private val auth = Firebase.auth

    // channel for one-time events
    private var _uiEffect = Channel<HomeScreenUIEffect>()
    val uiEffect = _uiEffect.receiveAsFlow()

    private var _plantUiState: MutableStateFlow<PlantUIState> =
        MutableStateFlow(PlantUIState.Loading)
    val plantUiState: StateFlow<PlantUIState> = _plantUiState

    private var _forecastUiState: MutableStateFlow<RequestState<Forecast>> =
        MutableStateFlow(RequestState.Loading)
    val forecastUiState: StateFlow<RequestState<Forecast>> = _forecastUiState

    init {
        screenModelScope.launch {
            _forecastUiState.collectLatest {
                _plantUiState.value =
                    updatePlantListForTemperatureUseCase(_plantUiState.value, it)
            }
        }
    }

    fun initScreen() {
        getForecast(false)
        getPlants()
    }

    private fun getPlants() {
        screenModelScope.launch {
            try {
                val lowestTemperature =
                    (_forecastUiState.value as? RequestState.Success)?.data?.temperatureMin
                _plantUiState.value = PlantUIState.Success(getPlantsUseCase(lowestTemperature))
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

            HomeUIEvent.OpenLocationScreen -> {
                screenModelScope.launch {
                    _uiEffect.send(HomeScreenUIEffect.NavigateToLocationScreen)
                }
            }

            HomeUIEvent.Disconnect -> {
                screenModelScope.launch {
                    auth.signOut()
                    _uiEffect.send(HomeScreenUIEffect.NavigateToLoginScreen)
                }
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
        data class Success(val plants: List<PlantListDataModel>) : PlantUIState()
        data class Error(val message: String) : PlantUIState()
    }

    sealed class HomeUIEvent {
        data class DeletePlantClicked(
            val id: Long,
            val fileName: String?
        ) : HomeUIEvent()

        data object RetryForecast : HomeUIEvent()
        data object OpenLocationScreen : HomeUIEvent()
        data object Disconnect:  HomeUIEvent()
    }

    sealed class HomeScreenUIEffect {
        data object NavigateToLocationScreen : HomeScreenUIEffect()
        data object NavigateToLoginScreen : HomeScreenUIEffect()

    }
}