package org.alba.hortus.presentation.features.new

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
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
import cafe.adriel.voyager.koin.koinScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.preat.peekaboo.image.picker.toImageBitmap
import hortus.composeapp.generated.resources.Res
import hortus.composeapp.generated.resources.add_a_plant_title
import hortus.composeapp.generated.resources.add_button
import hortus.composeapp.generated.resources.add_plant_info
import hortus.composeapp.generated.resources.baseline_arrow_back_ios_24
import hortus.composeapp.generated.resources.clear_button
import hortus.composeapp.generated.resources.close_button_content_description
import hortus.composeapp.generated.resources.description_label
import hortus.composeapp.generated.resources.exposure_bottom_sheet_title
import hortus.composeapp.generated.resources.exposure_label
import hortus.composeapp.generated.resources.loading_button
import hortus.composeapp.generated.resources.loading_content_description
import hortus.composeapp.generated.resources.plant_label
import hortus.composeapp.generated.resources.plant_name_label
import hortus.composeapp.generated.resources.recognized_plant_bottom_sheet_title
import hortus.composeapp.generated.resources.search_button
import io.github.alexzhirkevich.compottie.LottieCompositionSpec
import io.github.alexzhirkevich.compottie.animateLottieCompositionAsState
import io.github.alexzhirkevich.compottie.rememberLottieComposition
import io.github.alexzhirkevich.compottie.rememberLottiePainter
import kotlinx.coroutines.launch
import org.alba.hortus.domain.model.Exposure
import org.alba.hortus.domain.model.PlantDatabaseModel
import org.alba.hortus.presentation.components.BottomSheetValue
import org.alba.hortus.presentation.components.BottomSheetVisibility
import org.alba.hortus.presentation.components.CameraView
import org.alba.hortus.presentation.components.ImagePicker
import org.alba.hortus.presentation.components.ObserveAsEvents
import org.alba.hortus.presentation.components.ReadOnlyTextField
import org.alba.hortus.presentation.components.ValuesBottomSheet
import org.alba.hortus.presentation.features.home.HomeScreen
import org.alba.hortus.presentation.utils.safeNavigate
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

class AddPlantScreen : Screen {

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val viewModel = koinScreenModel<AddPlantScreenViewModel>()
        val snackBarHostState = remember { SnackbarHostState() }
        val scope = rememberCoroutineScope()

        val uiEffect = viewModel.uiEffect
        var showLoading by remember { mutableStateOf(false) }

        val uiState = viewModel.uiState.collectAsState()
        val coroutineScope = rememberCoroutineScope()

        ObserveAsEvents(uiEffect) { event ->
            when (event) {
                is AddPlantScreenUIEffect.NavigateToHome -> {
                    navigator.safeNavigate(coroutineScope, HomeScreen())
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
                                text = stringResource(Res.string.add_a_plant_title),
                                style = MaterialTheme.typography.headlineMedium,
                                textAlign = TextAlign.Center,
                            )
                        },
                        navigationIcon = {
                            Icon(
                                modifier = Modifier
                                    .padding(start = 16.dp)
                                    .clickable {
                                        navigator.pop()
                                    },
                                painter = painterResource(Res.drawable.baseline_arrow_back_ios_24),
                                contentDescription = stringResource(Res.string.close_button_content_description)
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
                        modifier = Modifier
                            .padding(
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

                            AnimatedVisibility(
                                !showLoading,
                                enter = fadeIn(),
                                exit = fadeOut()
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
                            }


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
                                        plantName = plantName
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
                                        viewModel.sendEvent(
                                            AddUIEvent.Clear
                                        )
                                    },
                                ) {
                                    Text(stringResource(Res.string.clear_button))
                                }
                            }
                            Button(
                                modifier = Modifier
                                    .padding(bottom = 8.dp, top = 4.dp)
                                    .fillMaxWidth(),
                                enabled = showLoading.not(),
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
                                Text(
                                    when {
                                        showLoading -> {
                                            stringResource(Res.string.loading_button)
                                        }

                                        isSearchMode -> {
                                            stringResource(Res.string.search_button)
                                        }

                                        else -> {
                                            stringResource(Res.string.add_button)
                                        }
                                    }
                                )
                            }
                        }
                    }

                    if (showLoading) {
                        Loading()
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
                text = stringResource(Res.string.add_plant_info)
            )
        }

        OutlinedTextField(
            value = plantName,
            onValueChange = { onPlantNameChanged(it) },
            label = { Text(stringResource(Res.string.plant_name_label)) },
            modifier = Modifier.fillMaxWidth(),
        )

        OutlinedTextField(
            modifier = Modifier.height(200.dp).fillMaxWidth(),
            value = description,
            onValueChange = { onDescriptionChanged(it) },
            label = { Text(stringResource(Res.string.description_label)) },
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
            label = stringResource(Res.string.plant_label),
            onClick = {
                showPlantsSelectionBottomSheet = true
            }
        )

        ReadOnlyTextField(
            value = Exposure.getEnumForName(exposure)?.value ?: "",
            label = stringResource(Res.string.exposure_label),
            onClick = {
                showExposureBottomSheet = true
            }
        )
        BottomSheetVisibility(
            visible = showExposureBottomSheet,
        ) {
            ExposureBottomSheet(
                onExposureChanged = {
                    onExposureChanged(it)
                }, onDismiss = {
                    showExposureBottomSheet = false
                })
        }
        BottomSheetVisibility(
            visible = showPlantsSelectionBottomSheet,
        ) {
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
        title = stringResource(Res.string.exposure_bottom_sheet_title),
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
        title = stringResource(Res.string.recognized_plant_bottom_sheet_title),
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

@OptIn(ExperimentalResourceApi::class)
@Composable
private fun BoxScope.Loading() {
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
        contentDescription = stringResource(Res.string.loading_content_description)
    )
}

private const val LOADING_ANIMATION_FILE = "files/growing.json"