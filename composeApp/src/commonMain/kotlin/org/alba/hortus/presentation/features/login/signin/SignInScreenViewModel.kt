package org.alba.hortus.presentation.features.login.signin

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import dev.gitlive.firebase.auth.FirebaseAuthInvalidCredentialsException
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import org.alba.hortus.presentation.features.login.usecase.SignInUseCase

class SignInScreenViewModel(
    private val signInUseCase: SignInUseCase
) : ScreenModel {

    // channel for one-time events
    private var _uiEffect = Channel<SignInScreenUIEffect>()
    val uiEffect = _uiEffect.receiveAsFlow()

    private var _signInUiState: MutableStateFlow<SignInScreenUIState> =
        MutableStateFlow(SignInScreenUIState())
    val signInUiState: StateFlow<SignInScreenUIState> = _signInUiState

    fun connect(email: String, password: String) {
        screenModelScope.launch {
            signInUseCase(email, password,
                onSuccess = {
                    screenModelScope.launch {
                        _uiEffect.send(SignInScreenUIEffect.NavigateToHome)
                    }
                },
                onError = {
                    when (it) {
                        is FirebaseAuthInvalidCredentialsException -> {
                            _signInUiState.value = _signInUiState.value.copy(badCredentials = true)
                        }
                    }
                }
            )
        }
    }
}

data class SignInScreenUIState(
    val badCredentials: Boolean = false,
    val error: Boolean = false,
)

sealed class SignInScreenUIEffect {
    data object NavigateToHome : SignInScreenUIEffect()
}