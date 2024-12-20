package org.alba.hortus.presentation.features.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import hortus.composeapp.generated.resources.Res
import hortus.composeapp.generated.resources.background
import org.alba.hortus.domain.model.PlantDatabaseModel
import org.alba.hortus.presentation.components.AlertMessageDialog
import org.alba.hortus.presentation.components.ErrorView
import org.alba.hortus.presentation.components.PlantCard
import org.alba.hortus.presentation.features.details.PlantDetailsScreen
import org.jetbrains.compose.resources.painterResource

@Composable
fun PlantListView(viewModel: HomeScreenViewModel) {
    val navigator = LocalNavigator.currentOrThrow
    var showDeleteDialog by remember { mutableStateOf<PlantDatabaseModel?>(null) }
    val uiState = viewModel.plantUiState.collectAsState()

    when (val state = uiState.value) {
        is HomeScreenViewModel.PlantUIState.Error -> {
            ErrorView(state.message) {
                viewModel.initScreen()
            }
        }

        HomeScreenViewModel.PlantUIState.Loading -> {
            Box(Modifier.fillMaxSize()) {
                CircularProgressIndicator(
                    modifier = Modifier.size(64.dp).align(Alignment.Center),
                )
            }
        }

        is HomeScreenViewModel.PlantUIState.Success -> {
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
                    modifier = Modifier.clip(CardDefaults.shape),
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

