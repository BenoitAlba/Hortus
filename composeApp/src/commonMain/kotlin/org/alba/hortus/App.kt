package org.alba.hortus

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import cafe.adriel.voyager.navigator.Navigator
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.alba.hortus.presentation.features.home.HomeScreen

@Composable
@Preview
fun App() {
    MaterialTheme {
        Navigator(
            HomeScreen()
        )
    }
}