package org.alba.hortus.presentation.features.login.signup

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.auth.FirebaseAuthInvalidCredentialsException
import dev.gitlive.firebase.auth.FirebaseAuthUserCollisionException
import dev.gitlive.firebase.auth.auth
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import org.alba.hortus.presentation.features.login.usecase.SignInUseCase

class SignUpScreenViewModel(
    private val signInUseCase: SignInUseCase
) : ScreenModel {

    // channel for one-time events
    private var _uiEffect = Channel<LoginScreenUIEffect>()
    val uiEffect = _uiEffect.receiveAsFlow()

    private var _loginUiState: MutableStateFlow<LoginScreenUIState> =
        MutableStateFlow(LoginScreenUIState())
    val loginUiState: StateFlow<LoginScreenUIState> = _loginUiState

    private val auth = Firebase.auth


    fun createUserWithEmailAndPassword(email: String, password: String) {
        screenModelScope.launch {
            try {
                auth.createUserWithEmailAndPassword(email, password)
                _uiEffect.send(LoginScreenUIEffect.NavigateToHome)
            } catch (e: FirebaseAuthUserCollisionException) {
                _loginUiState.value = _loginUiState.value.copy(userAlreadyExist = true)
            }
        }
    }

    fun connect(email: String, password: String) {
        screenModelScope.launch {
            signInUseCase(email, password,
                onSuccess = {
                    screenModelScope.launch {
                        _uiEffect.send(LoginScreenUIEffect.NavigateToHome)
                    }
                },
                onError = {
                    when (it) {
                        is FirebaseAuthInvalidCredentialsException -> {
                            _loginUiState.value = _loginUiState.value.copy(badCredentials = true)
                        }
                    }
                }
            )
        }
    }

    fun isEmailValid(email: String) {
        val emailRegex = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$".toRegex()
        _loginUiState.value =
            _loginUiState.value.copy(
                isEmailValid = if (email.isBlank()) {
                    null
                } else {
                    emailRegex.matches(email)
                }
            )
    }

    fun isPassWordValid(password: String) {
        val passwordRegex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{8,}$".toRegex()
        _loginUiState.value = _loginUiState.value.copy(
            isPasswordValid = if (password.isBlank()) {
                null
            } else {
                passwordRegex.matches(password)
            }
        )
    }

    fun isSecondPassWordValid(password: String, passwordConfirmation: String) {
        _loginUiState.value = _loginUiState.value.copy(
            isConfirmationPasswordValid = if (passwordConfirmation.isBlank()) {
                null
            } else {
                password == passwordConfirmation
            }
        )
    }
}

data class LoginScreenUIState(
    val isEmailValid: Boolean? = null,
    val isPasswordValid: Boolean? = null,
    val isConfirmationPasswordValid: Boolean? = null,
    val userAlreadyExist: Boolean = false,
    val badCredentials: Boolean = false,
)

sealed class LoginScreenUIEffect {
    data object NavigateToHome : LoginScreenUIEffect()
}