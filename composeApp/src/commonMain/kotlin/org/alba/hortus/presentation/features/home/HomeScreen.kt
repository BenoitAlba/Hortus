package org.alba.hortus.presentation.features.home

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
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import kotlinx.coroutines.launch
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

        }
    }
}