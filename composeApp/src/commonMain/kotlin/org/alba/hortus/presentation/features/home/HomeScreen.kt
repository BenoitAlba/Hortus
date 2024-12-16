package org.alba.hortus.presentation.features.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import hortus.composeapp.generated.resources.Res
import hortus.composeapp.generated.resources.background
import kotlinx.coroutines.launch
import org.alba.hortus.domain.model.PlantDatabaseModel
import org.alba.hortus.presentation.components.AlertMessageDialog
import org.alba.hortus.presentation.components.ErrorView
import org.alba.hortus.presentation.components.PlantCard
import org.alba.hortus.presentation.features.details.PlantDetailsScreen
import org.alba.hortus.presentation.features.new.AddPlantScreen
import org.jetbrains.compose.resources.painterResource

class HomeScreen(
    val message: String? = null
) : Screen {
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val viewModel = getScreenModel<HomeScreenViewModel>()
        viewModel.getPlant() // instead of using the init of the VM because of Voyager
        val navigator = LocalNavigator.currentOrThrow
        val snackBarHostState = remember { SnackbarHostState() }
        val scope = rememberCoroutineScope()

        var showDeleteDialog by remember { mutableStateOf<PlantDatabaseModel?>(null) }

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
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            modifier = Modifier.fillMaxWidth(),
                            style = MaterialTheme.typography.headlineMedium,
                            text = "Plants",
                            textAlign = TextAlign.Center,
                        )
                    },
                )
            },
            snackbarHost = {
                SnackbarHost(hostState = snackBarHostState)
            }
        ) {
            when (val state = uiState.value) {
                is HomeScreenViewModel.HomeScreenUIState.Error -> {
                    ErrorView(state.message) {
                        viewModel.getPlant()
                    }
                }

                HomeScreenViewModel.HomeScreenUIState.Loading -> {
                    Box(Modifier.fillMaxSize()) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(64.dp).align(Alignment.Center),
                        )
                    }
                }

                is HomeScreenViewModel.HomeScreenUIState.Success -> {
                    if (state.plants.isEmpty()) {
                        Box {
                            Image(
                                modifier = Modifier.align(Alignment.Center).fillMaxSize(),
                                painter = painterResource(Res.drawable.background),
                                contentDescription = null,
                                contentScale = ContentScale.FillWidth
                            )
                        }

                    } else {
                        if (showDeleteDialog != null) {
                            AlertMessageDialog(
                                title = "Delete the plant",
                                message = "${showDeleteDialog?.commonName} ?",
                                positiveButtonText = "Yes",
                                negativeButtonText = "No",
                                onPositiveClick = {
                                    viewModel.sendEvent(
                                        HomeScreenViewModel.HomeUIEvent.DeletePlantClicked(
                                            showDeleteDialog!!.id,
                                            showDeleteDialog!!.image
                                        )
                                    )
                                    showDeleteDialog = null
                                },
                                onNegativeClick = {
                                    showDeleteDialog = null
                                }
                            )
                        }

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
                                PlantCard(
                                    state.plants[index],
                                    onClick = {
                                        navigator.push(PlantDetailsScreen(state.plants[index].id))
                                    },
                                    onLongClick = {
                                        showDeleteDialog = state.plants[index]
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}