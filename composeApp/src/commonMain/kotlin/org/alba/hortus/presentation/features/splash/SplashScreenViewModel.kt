package org.alba.hortus.presentation.features.splash

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.auth.auth
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class SplashScreenViewModel : ScreenModel {
    // channel for one-time events
    private var _uiEffect = Channel<SplashScreenUIEffect>()
    val uiEffect = _uiEffect.receiveAsFlow()

    init {
        screenModelScope.launch {
            delay(4000)
            if (Firebase.auth.currentUser != null) {
                _uiEffect.send(SplashScreenUIEffect.NavigateToHome)
            } else {
                _uiEffect.send(SplashScreenUIEffect.NavigateToLogin)
            }
        }
    }
}

sealed class SplashScreenUIEffect {
    data object NavigateToHome : SplashScreenUIEffect()
    data object NavigateToLogin : SplashScreenUIEffect()
}