package org.alba.hortus

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.NavigatorDisposeBehavior
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.alba.hortus.presentation.features.home.HomeScreen
import org.alba.hortus.ui.theme.AppTheme

@Composable
@Preview
fun App(
    darkTheme: Boolean,
    dynamicColor: Boolean,
) {
    AppTheme(
        darkTheme = darkTheme,
        dynamicColor = dynamicColor
    ) {
        Navigator(
            disposeBehavior = NavigatorDisposeBehavior(disposeSteps = false),
            screen = HomeScreen()
        )
    }
}