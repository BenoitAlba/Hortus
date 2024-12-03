package org.alba.hortus

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.NavigatorDisposeBehavior
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.alba.hortus.presentation.features.home.HomeScreen

@Composable
@Preview
fun App() {
    MaterialTheme {
        Navigator(
            disposeBehavior = NavigatorDisposeBehavior(disposeSteps = false),
            screen = HomeScreen()
        )
    }
}