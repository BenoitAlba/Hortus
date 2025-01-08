package org.alba.hortus.presentation.features.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import org.alba.hortus.presentation.features.new.AddPlantScreen
import org.alba.hortus.presentation.utils.safeNavigate

class HomeScreen(
) : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val viewModel = koinScreenModel<HomeScreenViewModel>()
        viewModel.initScreen() // instead of using the init of the VM because of Voyager
        val snackBarHostState = remember { SnackbarHostState() }
        val coroutineScope = rememberCoroutineScope()

        Scaffold(
            floatingActionButton = {
                FloatingActionButton(
                    onClick = {
                        navigator.safeNavigate(coroutineScope, AddPlantScreen())
                    },
                ) {
                    Icon(Icons.Filled.Add, "Adding plant")
                }
            },
            snackbarHost = {
                SnackbarHost(hostState = snackBarHostState)
            }
        ) {
            Column(
                modifier = Modifier.padding(
                    top = it.calculateTopPadding() + 16.dp,
                    bottom = it.calculateBottomPadding(),
                    start = 16.dp,
                    end = 16.dp
                ),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                ForecastView(viewModel)
                PlantListView(viewModel)
            }

        }
    }
}