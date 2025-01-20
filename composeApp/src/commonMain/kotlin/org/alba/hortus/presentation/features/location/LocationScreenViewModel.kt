package org.alba.hortus.presentation.features.location

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.alba.hortus.data.LocationRepository
import org.alba.hortus.domain.model.LocationResult

@OptIn(FlowPreview::class)
class LocationScreenViewModel(
    private val locationRepository: LocationRepository
) : ScreenModel {

    private val _uiState: MutableStateFlow<LocationScreenUIState> =
        MutableStateFlow(LocationScreenUIState.IdleScreen)
    val uiState: StateFlow<LocationScreenUIState> = _uiState

    private val _inputText: MutableStateFlow<String> =
        MutableStateFlow("")
    val inputText: StateFlow<String> = _inputText

    private var isInitialized = false

    init {
        screenModelScope.launch {
            val location = locationRepository.getLocation()
            location?.let {
                _uiState.update { LocationScreenUIState.LocationSelected(location) }
            }

            if (!isInitialized) {
                isInitialized = true
                inputText.debounce(500).collectLatest { input ->

                    if (_uiState.value !is LocationScreenUIState.LocationSelected) {
                        if (input.blankOrEmpty()) {
                            _uiState.update { LocationScreenUIState.IdleScreen }
                            return@collectLatest
                        }

                        val result = locationRepository.getLocationForName(input)
                        if (result.isEmpty()) {
                            _uiState.update { LocationScreenUIState.NoResults }
                        } else {
                            _uiState.update { LocationScreenUIState.SearchResultsFetched(result) }
                        }
                    }
                }
            }
        }

    }

    fun updateInput(inputText: String) {
        _inputText.update { inputText }

        if (inputText.blankOrEmpty().not()) {
            _uiState.update { LocationScreenUIState.Loading }
        }
    }

    fun clearInput() {
        _uiState.update { LocationScreenUIState.Loading }
        _inputText.update { "" }
    }

    fun sendEvent(event: LocationScreenUIEvent) {
        when (event) {
            LocationScreenUIEvent.ClearLocation -> {
                screenModelScope.launch {
                    locationRepository.clearLocation()
                    _uiState.update { LocationScreenUIState.IdleScreen }
                }
            }

            is LocationScreenUIEvent.LocationSelected -> {
                screenModelScope.launch {
                    locationRepository.updateLocation(event.location as LocationResult.Location)
                    _uiState.value = LocationScreenUIState.LocationSelected(event.location)
                }
            }

            LocationScreenUIEvent.GeoLocate -> {
                screenModelScope.launch {
                    when (val result = locationRepository.getGeoLocationAndUpdateLocation()) {
                        is LocationResult.Error -> {
                            _uiState.update { LocationScreenUIState.NoResults }
                        }

                        is LocationResult.Location -> {
                            _uiState.update { LocationScreenUIState.LocationSelected(result) }
                        }
                    }
                }
            }
        }
    }

    private fun String.blankOrEmpty() = this.isBlank() || this.isEmpty()

    sealed class LocationScreenUIState {
        data object IdleScreen : LocationScreenUIState()
        data object Loading : LocationScreenUIState()
        data object Error : LocationScreenUIState()
        data object NoResults : LocationScreenUIState()
        data class SearchResultsFetched(val results: List<LocationResult>) : LocationScreenUIState()
        data class LocationSelected(val location: LocationResult) : LocationScreenUIState()
    }

    sealed class LocationScreenUIEvent {
        data class LocationSelected(val location: LocationResult) : LocationScreenUIEvent()
        data object ClearLocation : LocationScreenUIEvent()
        data object GeoLocate : LocationScreenUIEvent()
    }
}