package org.alba.hortus.presentation.features.home

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel

class HomeScreen  : Screen {
    @Composable
    override fun Content() {
        val viewModel = getScreenModel<HomeScreenViewModel>()
        Text("Home Screen")

    }
}