package org.alba.hortus.presentation.features.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import hortus.composeapp.generated.resources.Res
import hortus.composeapp.generated.resources.loading_content_description
import io.github.alexzhirkevich.compottie.LottieCompositionSpec
import io.github.alexzhirkevich.compottie.animateLottieCompositionAsState
import io.github.alexzhirkevich.compottie.rememberLottieComposition
import io.github.alexzhirkevich.compottie.rememberLottiePainter
import org.alba.hortus.presentation.components.ObserveAsEvents
import org.alba.hortus.presentation.features.home.HomeScreen
import org.alba.hortus.presentation.features.login.choice.LoginOrSignUpScreen
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.stringResource

class SplashScreen : Screen {
    @OptIn(ExperimentalResourceApi::class)
    @Composable
    override fun Content() {
        val viewModel = koinScreenModel<SplashScreenViewModel>()
        val uiEffect = viewModel.uiEffect
        val navigator = LocalNavigator.currentOrThrow

        ObserveAsEvents(uiEffect) { event ->
            when (event) {
                SplashScreenUIEffect.NavigateToHome -> {
                    navigator.replaceAll(HomeScreen())
                }

                SplashScreenUIEffect.NavigateToLogin -> {
                    navigator.replaceAll(LoginOrSignUpScreen())
                }
            }
        }

        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            val composition by rememberLottieComposition {
                LottieCompositionSpec.JsonString(
                    Res.readBytes(LOADING_ANIMATION_FILE).decodeToString()
                )
            }
            val progress by animateLottieCompositionAsState(composition)
            Image(
                modifier = Modifier.size(400.dp)
                    .padding(top = 60.dp).align(Alignment.Center),
                painter = rememberLottiePainter(
                    composition = composition,
                    progress = { progress },
                ),
                contentDescription = stringResource(Res.string.loading_content_description)
            )
        }
    }
}

private const val LOADING_ANIMATION_FILE = "files/growing.json"