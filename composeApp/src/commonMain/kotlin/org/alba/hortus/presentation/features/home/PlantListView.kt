package org.alba.hortus.presentation.features.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import hortus.composeapp.generated.resources.Res
import hortus.composeapp.generated.resources.delete_plant_dialog_negative_button
import hortus.composeapp.generated.resources.delete_plant_dialog_positive_button
import hortus.composeapp.generated.resources.delete_plant_dialog_title
import io.github.alexzhirkevich.compottie.LottieCompositionSpec
import io.github.alexzhirkevich.compottie.animateLottieCompositionAsState
import io.github.alexzhirkevich.compottie.rememberLottieComposition
import io.github.alexzhirkevich.compottie.rememberLottiePainter
import org.alba.hortus.domain.model.PlantListDataModel
import org.alba.hortus.presentation.components.AlertMessageDialog
import org.alba.hortus.presentation.components.ErrorView
import org.alba.hortus.presentation.components.PlantCard
import org.alba.hortus.presentation.features.details.PlantDetailsScreen
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.stringResource

@Composable
fun PlantListView(
    viewModel: HomeScreenViewModel,
    onNoPlants: (Boolean) -> Unit,
) {
    val navigator = LocalNavigator.currentOrThrow
    var showDeleteDialog by remember { mutableStateOf<PlantListDataModel?>(null) }
    val uiState = viewModel.plantUiState.collectAsState()

    when (val state = uiState.value) {
        is HomeScreenViewModel.PlantUIState.Error -> {
            ErrorView(errorMessage = state.message) {
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
                Box(
                    modifier = Modifier.fillMaxSize()
                ) {
                    onNoPlants(true)
                    Empty()
                }

            } else {
                onNoPlants(false)
                if (showDeleteDialog != null) {
                    AlertMessageDialog(
                        title = stringResource(Res.string.delete_plant_dialog_title),
                        message = "${showDeleteDialog?.commonName} ?",
                        positiveButtonText = stringResource(Res.string.delete_plant_dialog_positive_button),
                        negativeButtonText = stringResource(Res.string.delete_plant_dialog_negative_button),
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

@OptIn(ExperimentalResourceApi::class)
@Composable
private fun BoxScope.Empty() {
    val composition by rememberLottieComposition {
        LottieCompositionSpec.JsonString(
            Res.readBytes(LOADING_ANIMATION_FILE).decodeToString()
        )
    }
    val progress by animateLottieCompositionAsState(composition)

    Image(
        modifier = Modifier.size(400.dp).align(Alignment.Center)
            .padding(top = 60.dp),
        painter = rememberLottiePainter(
            composition = composition,
            progress = { progress },
        ),
        contentDescription = ""
    )
}

private const val LOADING_ANIMATION_FILE = "files/empty.json"

