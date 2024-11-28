package org.alba.hortus.presentation.features.new

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel

class AddPlantScreen : Screen {

    @Composable
    override fun Content() {
        val viewModel = getScreenModel<AddPlantScreenViewModel>()
        Scaffold {
            Column {

            }
        }
    }
}