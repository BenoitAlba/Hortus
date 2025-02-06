package org.alba.hortus.presentation.features.edit

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Switch
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
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import hortus.composeapp.generated.resources.Res
import hortus.composeapp.generated.resources.baseline_arrow_back_ios_24
import hortus.composeapp.generated.resources.close_button_content_description
import hortus.composeapp.generated.resources.common_name_title
import hortus.composeapp.generated.resources.current_exposures_title
import hortus.composeapp.generated.resources.description_label
import hortus.composeapp.generated.resources.edit_screen_title
import hortus.composeapp.generated.resources.exposure_label
import hortus.composeapp.generated.resources.flowering_months_title
import hortus.composeapp.generated.resources.fruiting_months_title
import hortus.composeapp.generated.resources.hardiness_title
import hortus.composeapp.generated.resources.is_a_fruit_plant_label
import hortus.composeapp.generated.resources.max_height_title
import hortus.composeapp.generated.resources.max_width_title
import hortus.composeapp.generated.resources.ok_button
import hortus.composeapp.generated.resources.pollination_title
import hortus.composeapp.generated.resources.scientific_name_title
import hortus.composeapp.generated.resources.soil_moisture_title
import org.alba.hortus.domain.model.Exposure
import org.alba.hortus.domain.model.PlantDatabaseModel
import org.alba.hortus.presentation.components.ObserveAsEvents
import org.alba.hortus.presentation.features.details.PlantDetailsScreen
import org.alba.hortus.presentation.utils.safeNavigate
import org.alba.hortus.utils.vibratePhone
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import kotlin.math.round
import kotlin.math.roundToInt

class EditScreen(
    private val id: Long
) : Screen {

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val viewModel = koinScreenModel<EditScreenViewModel>()
        viewModel.getPlantDetails(id)
        val uiState = viewModel.editUiState.collectAsState()
        val uiEffect = viewModel.uiEffect
        val coroutineScope = rememberCoroutineScope()

        ObserveAsEvents(uiEffect) { event ->
            when (event) {
                is EditScreenViewModel.EditPlantScreenUIEffect.NavigateToPlantDetails -> {
                    navigator.safeNavigate(coroutineScope, PlantDetailsScreen(id = event.id))
                }
            }
        }

        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = stringResource(Res.string.edit_screen_title),
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
                    }
                )
            }
        ) {
            when (val state = uiState.value) {
                is EditScreenViewModel.EditScreenUIState.Error -> {

                }

                EditScreenViewModel.EditScreenUIState.Loading -> {
                    Box(Modifier.fillMaxSize()) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(64.dp).align(Alignment.Center),
                        )
                    }
                }

                is EditScreenViewModel.EditScreenUIState.Success -> {
                    EditView(it, viewModel, state.plant)
                }
            }
        }
    }
}

@Composable
private fun EditView(
    paddingValues: PaddingValues,
    viewModel: EditScreenViewModel,
    plant: PlantDatabaseModel,
) {


    val id by remember { mutableStateOf(plant.id) }
    var commonName by remember { mutableStateOf(plant.commonName) }
    var scientificName by remember { mutableStateOf(plant.scientificName ?: "") }
    var description by remember { mutableStateOf(plant.description ?: "") }
    var mawHeight by remember { mutableStateOf(plant.maxHeight?.toString() ?: "") }
    var maxWidth by remember { mutableStateOf(plant.maxWidth?.toString() ?: "") }
    var pollination by remember { mutableStateOf(plant.pollination ?: "") }
    var floweringMonths by remember { mutableStateOf(plant.floweringMonths ?: mutableListOf()) }
    var fruitingMonths by remember { mutableStateOf(plant.fruitingMonths ?: mutableListOf()) }
    var isAFruitPlant by remember { mutableStateOf(plant.isAFruitPlant ?: false) }
    var exposure by remember { mutableStateOf(plant.exposure ?: mutableListOf()) }
    var currentExposure by remember { mutableStateOf(plant.currentExposure) }
    var soilMoisture by remember { mutableStateOf(plant.soilMoisture ?: "") }
    var hardiness by remember { mutableStateOf(plant.hardiness ?: 0.0f) }
    var exposureAdvise by remember { mutableStateOf(plant.exposureAdvise ?: "") }

    Column(
        modifier = Modifier
            .padding(
                top = paddingValues.calculateTopPadding() + 16.dp,
                bottom = paddingValues.calculateBottomPadding(),
                start = 16.dp,
                end = 16.dp,
            )
            .fillMaxHeight(),
    ) {
        LazyColumn(
            Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            item {
                OutlinedTextField(
                    value = commonName,
                    onValueChange = { commonName = "" },
                    label = { Text(stringResource(Res.string.common_name_title)) },
                    modifier = Modifier.fillMaxWidth(),
                )

                OutlinedTextField(
                    value = scientificName,
                    onValueChange = { scientificName = it },
                    label = { Text(stringResource(Res.string.scientific_name_title)) },
                    modifier = Modifier.fillMaxWidth(),
                )

                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text(stringResource(Res.string.description_label)) },
                    modifier = Modifier.fillMaxWidth(),
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    OutlinedTextField(
                        modifier = Modifier.weight(1f),
                        value = mawHeight,
                        onValueChange = { mawHeight = it },
                        label = { Text(stringResource(Res.string.max_height_title)) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )

                    OutlinedTextField(
                        modifier = Modifier.weight(1f),
                        value = maxWidth,
                        onValueChange = { maxWidth = it },
                        label = { Text(stringResource(Res.string.max_width_title)) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )
                }

                OutlinedTextField(
                    value = pollination,
                    onValueChange = { pollination = it },
                    label = { Text(stringResource(Res.string.pollination_title)) },
                    modifier = Modifier.fillMaxWidth(),
                )

                MonthSelector(stringResource(Res.string.flowering_months_title), floweringMonths) {
                    floweringMonths = floweringMonths.toMutableList().apply {
                        if (contains(it)) {
                            remove(it)
                        } else {
                            add(it)
                        }
                    }
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(stringResource(Res.string.is_a_fruit_plant_label))
                    Switch(isAFruitPlant, onCheckedChange = { isAFruitPlant = it })
                }

                AnimatedVisibility(isAFruitPlant) {
                    MonthSelector(
                        stringResource(Res.string.fruiting_months_title),
                        fruitingMonths
                    ) {
                        fruitingMonths = fruitingMonths.toMutableList().apply {
                            if (contains(it)) {
                                remove(it)
                            } else {
                                add(it)
                            }
                        }
                    }
                }

                ExposureSelector(
                    stringResource(Res.string.exposure_label),
                    exposure,
                ) {
                    exposure = exposure.toMutableList().apply {
                        if (contains(it)) {
                            remove(it)
                        } else {
                            add(it)
                        }
                    }
                }

                ExposureSelector(
                    stringResource(Res.string.current_exposures_title),
                    listOf(currentExposure),
                ) { tappedExposure ->
                    currentExposure = tappedExposure
                }

                TemperatureSlider(
                    temperature = hardiness,
                    onTemperatureChange = { hardiness = it }
                )

                OutlinedTextField(
                    value = exposureAdvise,
                    onValueChange = { exposureAdvise = it },
                    label = { Text(stringResource(Res.string.current_exposures_title)) },
                    modifier = Modifier.fillMaxWidth(),
                )

                OutlinedTextField(
                    value = soilMoisture,
                    onValueChange = { soilMoisture = it },
                    label = { Text(stringResource(Res.string.soil_moisture_title)) },
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        }

        Button(
            modifier = Modifier
                .padding(bottom = 8.dp, top = 16.dp)
                .fillMaxWidth(),
            onClick = {
                viewModel.sendEven(
                    EditScreenViewModel.EditUIEvent.SaveClicked(
                        PlantDatabaseModel(
                            id = id,
                            commonName = commonName,
                            scientificName = scientificName,
                            description = description,
                            maxHeight = mawHeight.toIntOrNull(),
                            maxWidth = maxWidth.toIntOrNull(),
                            pollination = pollination,
                            floweringMonths = floweringMonths,
                            fruitingMonths = fruitingMonths,
                            isAFruitPlant = isAFruitPlant,
                            exposure = exposure,
                            currentExposure = currentExposure,
                            soilMoisture = soilMoisture,
                            hardiness = hardiness,
                            exposureAdvise = exposureAdvise,
                        )
                    )
                )
            },
        ) {
            Text(text = stringResource(Res.string.ok_button))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TemperatureSlider(
    temperature: Float,
    onTemperatureChange: (Float) -> Unit
) {
    val minValue = -30f
    val maxValue = 20f
    val zeroPositionFraction = (-minValue) / (maxValue - minValue) // Position de 0 en fraction

    Column {
        Text(
            text = stringResource(Res.string.hardiness_title),
            style = MaterialTheme.typography.titleMedium
        )
        Slider(
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .drawBehind {
                    val width = size.width
                    val zeroX = width * zeroPositionFraction // Calcul de la position du zéro
                    val trackHeight = 30.dp.toPx()
                    val cornerRadius = trackHeight / 2 // Arrondi basé sur la hauteur du track

                    // Dégradé bleu de gauche à zéro
                    val blueGradient = Brush.linearGradient(
                        colors = listOf(Color(0xFF3A6EA5), Color(0xFFA7C7E7)),
                        start = Offset(0f, 0f),
                        end = Offset(zeroX, 0f)
                    )

                    // Dégradé rouge de zéro à droite
                    val redGradient = Brush.linearGradient(
                        colors = listOf(Color(0xFFF4A7B9), Color(0xFFB22222)),
                        start = Offset(zeroX, 0f),
                        end = Offset(width, 0f)
                    )

                    drawRoundRect(
                        brush = blueGradient,
                        topLeft = Offset(0f, (size.height - trackHeight) / 2),
                        size = Size(zeroX, trackHeight),
                        cornerRadius = CornerRadius(
                            cornerRadius,
                            cornerRadius
                        ) // Seulement côté gauche
                    )

                    // Partie rouge (droite) avec bord droit arrondi et bord gauche rectiligne
                    drawRoundRect(
                        brush = redGradient,
                        topLeft = Offset(zeroX, (size.height - trackHeight) / 2),
                        size = Size(width - zeroX, trackHeight),
                        cornerRadius = CornerRadius(
                            cornerRadius,
                            cornerRadius
                        ) // Seulement côté droit
                    )

                    // Dessiner redessiner les parties centrales précédement dessinées en arrondits
                    drawRect(
                        brush = blueGradient,
                        topLeft = Offset(0f + cornerRadius, (size.height - trackHeight) / 2),
                        size = Size(zeroX, trackHeight)
                    )
                    drawRect(
                        brush = redGradient,
                        topLeft = Offset(zeroX, (size.height - trackHeight) / 2),
                        size = Size(
                            width - zeroX - cornerRadius,
                            trackHeight
                        ) // Un fin trait pour masquer l’arrondi
                    )

                },
            value = temperature,
            onValueChange = { newValue ->
                val newStep = newValue.toInt()
                if (newStep != temperature.toInt()) {
                    vibratePhone() // Déclenche la vibration quand un step est franchi
                }
                onTemperatureChange(round(newValue))
            },
            valueRange = minValue..maxValue,
            steps = 49,
            thumb = {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .background(Color.Black.copy(alpha = 0.7f), shape = CircleShape)
                        .clip(CircleShape)
                        .padding(4.dp)
                ) {
                    Text(
                        text = "${temperature.roundToInt()} C°",
                        color = MaterialTheme.colorScheme.onSecondary,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            },
            colors = SliderDefaults.colors(
                activeTrackColor = Color.Transparent, // On met transparent pour ne pas cacher le fond
                inactiveTrackColor = Color.Transparent,
                activeTickColor = Color.White,
                inactiveTickColor = Color.White,
            )
        )
    }
}

@Composable
private fun MonthSelector(
    title: String,
    selectedMonths: List<String>,
    onSelected: (String) -> Unit,
) {
    Column {
        Text(text = title, style = MaterialTheme.typography.titleMedium)
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            for (i in 1..12) {
                item {
                    Button(
                        onClick = { onSelected(i.toString()) },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (selectedMonths.contains(i.toString())
                            ) {
                                MaterialTheme.colorScheme.secondary
                            } else {
                                MaterialTheme.colorScheme.surfaceVariant
                            }
                        )
                    ) {
                        Text(text = i.toString())
                    }
                }
            }
        }
    }
}

@Composable
private fun ExposureSelector(
    title: String,
    selectedExposure: List<String>,
    onSelected: (String) -> Unit,
) {
    Column {
        Text(text = title, style = MaterialTheme.typography.titleMedium)
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            Exposure.entries.forEach {
                val icons = when (it) {
                    Exposure.SUN -> Exposure.SUN.drawableRes
                    Exposure.SHADE -> Exposure.SHADE.drawableRes
                    Exposure.PARTIAL_SHADE -> Exposure.PARTIAL_SHADE.drawableRes
                }

                val description = when (it) {
                    Exposure.SUN -> Exposure.SUN.description
                    Exposure.SHADE -> Exposure.SHADE.description
                    Exposure.PARTIAL_SHADE -> Exposure.PARTIAL_SHADE.description
                }
                item {
                    IconButton(
                        onClick = {
                            onSelected(it.name)
                        },
                        colors = IconButtonDefaults.iconButtonColors(
                            if (selectedExposure.contains(it.name)
                            ) {
                                MaterialTheme.colorScheme.secondary
                            } else {
                                MaterialTheme.colorScheme.surfaceVariant
                            }
                        )
                    ) {
                        Icon(
                            painter = painterResource(icons),
                            tint = if (selectedExposure.contains(it.name)
                            ) {
                                MaterialTheme.colorScheme.surfaceVariant
                            } else {
                                MaterialTheme.colorScheme.secondary
                            },
                            contentDescription = stringResource(description)
                        )
                    }
                }
            }
        }
    }
}