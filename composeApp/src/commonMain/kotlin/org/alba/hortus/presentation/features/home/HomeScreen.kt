package org.alba.hortus.presentation.features.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import kotlinx.coroutines.launch
import org.alba.hortus.presentation.components.PlantCard
import org.alba.hortus.presentation.features.new.AddPlantScreen

class HomeScreen(
    val message: String? = null
) : Screen {
    @Composable
    override fun Content() {
        val viewModel = getScreenModel<HomeScreenViewModel>()
        val navigator = LocalNavigator.currentOrThrow
        val snackBarHostState = remember { SnackbarHostState() }
        val scope = rememberCoroutineScope()

        val uiState = viewModel.uiState.collectAsState()
        message?.let {
            scope.launch {
                snackBarHostState.showSnackbar(message = it)
            }
        }

        Scaffold(
            floatingActionButton = {
                FloatingActionButton(
                    onClick = {
                        navigator.push(AddPlantScreen())
                    },
                ) {
                    Icon(Icons.Filled.Add, "Adding plant")
                }
            },
            snackbarHost = {
                SnackbarHost(hostState = snackBarHostState)
            }
        ) {
            when (val state = uiState.value) {
                is HomeScreenViewModel.HomeScreenUIState.Error -> {

                }

                HomeScreenViewModel.HomeScreenUIState.Loading -> {

                }

                is HomeScreenViewModel.HomeScreenUIState.Success -> {
                    LazyColumn(
                        modifier = Modifier.padding(
                            top = it.calculateTopPadding() + 16.dp,
                            bottom = it.calculateBottomPadding(),
                            start = 16.dp,
                            end = 16.dp
                        ),
                        verticalArrangement = Arrangement.spacedBy(4.dp),

                        ) {
                        items(state.plants.size) { index ->
                            PlantCard(state.plants[index])
                        }
                    }
                }
            }
        }
    }
}