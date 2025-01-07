package org.alba.hortus.presentation.features.new

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.preat.peekaboo.image.picker.toImageBitmap
import hortus.composeapp.generated.resources.Res
import hortus.composeapp.generated.resources.baseline_arrow_back_ios_24
import kotlinx.coroutines.launch
import org.alba.hortus.domain.model.Exposure
import org.alba.hortus.domain.model.PlantDatabaseModel
import org.alba.hortus.presentation.components.BottomSheetValue
import org.alba.hortus.presentation.components.CameraView
import org.alba.hortus.presentation.components.ImagePicker
import org.alba.hortus.presentation.components.ObserveAsEvents
import org.alba.hortus.presentation.components.ReadOnlyTextField
import org.alba.hortus.presentation.components.ValuesBottomSheet
import org.alba.hortus.presentation.features.home.HomeScreen
import org.jetbrains.compose.resources.painterResource

class AddPlantScreen : Screen {

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val viewModel = getScreenModel<AddPlantScreenViewModel>()
        val snackBarHostState = remember { SnackbarHostState() }
        val scope = rememberCoroutineScope()

        val uiEffect = viewModel.uiEffect
        var showLoading by remember { mutableStateOf(false) }

        val uiState = viewModel.uiState.collectAsState()

        ObserveAsEvents(uiEffect) { event ->
            when (event) {
                is AddPlantScreenUIEffect.NavigateToHome -> {
                    navigator.replaceAll(HomeScreen(event.message))
                }

                is AddPlantScreenUIEffect.ShowToast -> {
                    scope.launch {
                        showLoading = false
                        snackBarHostState.showSnackbar(message = event.message)
                    }
                }
            }
        }

        var isSearchMode by remember { mutableStateOf(true) }
        var plantName by remember { mutableStateOf("") }
        var description by remember { mutableStateOf("") }
        var exposure by remember { mutableStateOf("") }
        var showCamera by remember { mutableStateOf(false) }
        var imageBitmap by remember { mutableStateOf<ImageBitmap?>(null) }

        if (showCamera) {
            CameraView(
                onBack = {
                    showCamera = false
                },
                onCapture = { byteArray ->
                    byteArray?.let {
                        imageBitmap = it.toImageBitmap()
                        viewModel.imageByteArray = it
                    }
                    showCamera = false
                }
            )
        } else {
            Scaffold(
                topBar = {
                    TopAppBar(
                        title = {
                            Text(
                                modifier = Modifier.fillMaxWidth(),
                                text = "Add a Plant",
                                style = MaterialTheme.typography.headlineMedium,
                                textAlign = TextAlign.Center,
                            )
                        },
                        navigationIcon = {
                            Icon(
                                modifier = Modifier
                                    .padding(start = 16.dp)
                                    .clickable {
                                        navigator.push(HomeScreen())
                                    },
                                painter = painterResource(Res.drawable.baseline_arrow_back_ios_24),
                                contentDescription = "close"
                            )
                        },
                    )
                },
                snackbarHost = {
                    SnackbarHost(hostState = snackBarHostState)
                },
            ) {

                Box(
                    modifier = Modifier.fillMaxSize()
                ) {
                    Column(
                        modifier = Modifier.padding(
                            top = it.calculateTopPadding() + 16.dp,
                            bottom = it.calculateBottomPadding(),
                            start = 16.dp,
                            end = 16.dp,
                        )
                            .fillMaxHeight(),
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(
                            modifier = Modifier
                                .verticalScroll(rememberScrollState())
                                .weight(1f, false),
                            verticalArrangement = Arrangement.spacedBy(4.dp),
                        ) {

                            ImagePicker(
                                modifier = Modifier.padding(bottom = 16.dp),
                                image = imageBitmap,
                                onStartCamera = {
                                    showCamera = true
                                },
                                onImageSelected = { image, byteArrayImage ->
                                    imageBitmap = image
                                    viewModel.imageByteArray = byteArrayImage
                                }
                            )

                            when (val state = uiState.value) {
                                AddPlantScreenUIState.Loading -> {
                                    showLoading = true
                                }

                                AddPlantScreenUIState.NoSearch -> {
                                    showLoading = false
                                    isSearchMode = true
                                    SearchView(
                                        plantName = plantName,
                                        onPlantNameChanged = {
                                            plantName = it
                                        },
                                        description = description,
                                        onDescriptionChanged = {
                                            description = it
                                        }
                                    )
                                }

                                is AddPlantScreenUIState.SearchSuccess -> {
                                    showLoading = false
                                    isSearchMode = false
                                    AddView(
                                        exposure = exposure,
                                        onExposureChanged = {
                                            exposure = it
                                        },
                                        plants = state.plants,
                                        showPlantsSelection = viewModel.selectedPlant == null,
                                        onPlantSelected = {
                                            viewModel.selectedPlant = it
                                            plantName = it.scientificName ?: it.commonName
                                        },
                                        plantName = plantName,

                                        )
                                }
                            }
                        }

                        Column {
                            if (!isSearchMode) {
                                OutlinedButton(
                                    modifier = Modifier
                                        .fillMaxWidth(),
                                    onClick = {
                                        showLoading = true
                                        viewModel.sendEvent(
                                            AddUIEvent.Clear
                                        )
                                    },
                                ) {
                                    Text("Clear")
                                }
                            }
                            Button(
                                modifier = Modifier
                                    .padding(bottom = 8.dp, top = 4.dp)
                                    .fillMaxWidth(),
                                onClick = {
                                    showLoading = true
                                    viewModel.sendEvent(
                                        if (isSearchMode) {
                                            AddUIEvent.SearchClicked(
                                                plantName = plantName,
                                                description = description,
                                            )
                                        } else {
                                            AddUIEvent.AddClicked(
                                                exposure
                                            )
                                        }
                                    )
                                },
                            ) {
                                Text(if (isSearchMode) "Search" else "Add")
                            }
                        }

                    }
                    if (showLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(64.dp).align(Alignment.Center),
                        )

                    }
                }
            }
        }
    }
}

@Composable
private fun SearchView(
    plantName: String,
    onPlantNameChanged: (String) -> Unit,
    description: String,
    onDescriptionChanged: (String) -> Unit,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp))
                .background(MaterialTheme.colorScheme.surfaceVariant)
                .padding(16.dp)
        ) {
            Text(
                style = MaterialTheme.typography.labelMedium,
                text = "'plant name' is required. \n" +
                        "The description may help the AI to find the right plants."
            )
        }

        OutlinedTextField(
            value = plantName,
            onValueChange = { onPlantNameChanged(it) },
            label = { Text("Plant name") },
            modifier = Modifier.fillMaxWidth(),
        )

        OutlinedTextField(
            modifier = Modifier.height(200.dp).fillMaxWidth(),
            value = description,
            onValueChange = { onDescriptionChanged(it) },
            label = { Text("Description") },
        )
    }
}

@Composable
private fun AddView(
    exposure: String,
    onExposureChanged: (String) -> Unit,
    plantName: String,
    plants: List<PlantDatabaseModel> = emptyList(),
    onPlantSelected: (PlantDatabaseModel) -> Unit,
    showPlantsSelection: Boolean = false,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        var showExposureBottomSheet by remember { mutableStateOf(false) }
        var showPlantsSelectionBottomSheet by remember { mutableStateOf(showPlantsSelection) }

        ReadOnlyTextField(
            value = plantName,
            label = "Plant",
            onClick = {
                showPlantsSelectionBottomSheet = true
            }
        )

        ReadOnlyTextField(
            value = Exposure.getEnumForName(exposure)?.value ?: "",
            label = "Exposure",
            onClick = {
                showExposureBottomSheet = true
            }
        )
        if (showExposureBottomSheet) {
            ExposureBottomSheet(
                onExposureChanged = {
                    onExposureChanged(it)
                }, onDismiss = {
                    showExposureBottomSheet = false
                })
        }
        if (showPlantsSelectionBottomSheet) {
            PlantsBottomSheet(
                plants = plants,
                onDismiss = {
                    showPlantsSelectionBottomSheet = false
                },
                onPlantSelected = {
                    onPlantSelected(it)
                }
            )
        }
    }
}

@Composable
private fun ExposureBottomSheet(onExposureChanged: (String) -> Unit, onDismiss: () -> Unit) {
    ValuesBottomSheet(
        title = "Exposure: ",
        values = listOf(
            BottomSheetValue(
                label = Exposure.SUN.value,
                description = Exposure.SUN.description,
                icon = painterResource(Exposure.SUN.drawableRes),
                value = Exposure.SUN.name,
            ),
            BottomSheetValue(
                label = Exposure.SHADE.value,
                description = Exposure.SHADE.description,
                icon = painterResource(Exposure.SHADE.drawableRes),
                value = Exposure.SHADE.name,
            ),
            BottomSheetValue(
                label = Exposure.PARTIAL_SHADE.value,
                description = Exposure.PARTIAL_SHADE.description,
                icon = painterResource(Exposure.PARTIAL_SHADE.drawableRes),
                value = Exposure.PARTIAL_SHADE.name,
            ),
        ),
        onDismiss = {
            onDismiss()
        },
        onValueSelected = {
            onExposureChanged(it)
        }
    )
}

@Composable
private fun PlantsBottomSheet(
    plants: List<PlantDatabaseModel>,
    onPlantSelected: (PlantDatabaseModel) -> Unit,
    onDismiss: () -> Unit
) {
    ValuesBottomSheet(
        isCard = true,
        skipPartiallyExpanded = true,
        title = "Recognized plants:",
        values = plants.map {
            BottomSheetValue(
                label = it.scientificName ?: "",
                description = it.description,
                value = it.id.toString()
            )
        },
        onDismiss = {
            onDismiss()
        },
        onValueSelected = {
            onPlantSelected(
                plants.find { plant -> plant.id == it.toLong() }!!
            )
        }
    )
}