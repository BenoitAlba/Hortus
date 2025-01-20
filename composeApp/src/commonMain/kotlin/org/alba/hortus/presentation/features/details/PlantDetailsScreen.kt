package org.alba.hortus.presentation.features.details

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import coil3.compose.AsyncImage
import hortus.composeapp.generated.resources.Res
import hortus.composeapp.generated.resources.arrow_range_24dp
import hortus.composeapp.generated.resources.baseline_arrow_back_ios_24
import hortus.composeapp.generated.resources.baseline_height_24
import hortus.composeapp.generated.resources.close_button_content_description
import hortus.composeapp.generated.resources.cm_label
import hortus.composeapp.generated.resources.common_name_title
import hortus.composeapp.generated.resources.current_exposures_title
import hortus.composeapp.generated.resources.default_plant
import hortus.composeapp.generated.resources.description_title
import hortus.composeapp.generated.resources.device_thermostat_24dp
import hortus.composeapp.generated.resources.flowering_months_title
import hortus.composeapp.generated.resources.fruiting_months_title
import hortus.composeapp.generated.resources.hardiness_title
import hortus.composeapp.generated.resources.height_title
import hortus.composeapp.generated.resources.plant_details_screen_title
import hortus.composeapp.generated.resources.pollination_title
import hortus.composeapp.generated.resources.recommended_exposures_title
import hortus.composeapp.generated.resources.scientific_name_title
import hortus.composeapp.generated.resources.soil_moisture_title
import hortus.composeapp.generated.resources.temperature_title
import hortus.composeapp.generated.resources.until_label
import hortus.composeapp.generated.resources.width_title
import org.alba.hortus.domain.model.Exposure
import org.alba.hortus.domain.model.PlantDatabaseModel
import org.alba.hortus.presentation.components.ErrorView
import org.alba.hortus.presentation.components.MonthsView
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
class PlantDetailsScreen(
    private val id: Long
) : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val viewModel = koinScreenModel<PlantDetailsScreenViewModel>()
        viewModel.getPlantDetails(id)
        val uiState = viewModel.uiState.collectAsState()

        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            modifier = Modifier.fillMaxWidth(),
                            text = stringResource(Res.string.plant_details_screen_title),
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
                is PlantDetailsScreenViewModel.PlantDetailsUIState.Error -> {
                    ErrorView(stringResource(state.message))
                }

                PlantDetailsScreenViewModel.PlantDetailsUIState.Loading -> {
                    Box(Modifier.fillMaxSize()) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(64.dp).align(Alignment.Center),
                        )
                    }
                }

                is PlantDetailsScreenViewModel.PlantDetailsUIState.Success -> {

                    LazyColumn(
                        modifier = Modifier.padding(
                            top = it.calculateTopPadding() + 16.dp,
                            bottom = it.calculateBottomPadding(),
                            start = 16.dp,
                            end = 16.dp,
                        ).fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        item {
                            AsyncImage(
                                model = state.plant.image ?: Res.drawable.default_plant,
                                placeholder = painterResource(Res.drawable.default_plant),
                                error = painterResource(Res.drawable.default_plant),
                                fallback = painterResource(Res.drawable.default_plant),
                                contentDescription = "",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.clip(CircleShape).size(200.dp)
                            )
                        }

                        item {
                            PlantDescription(state.plant)
                        }

                        state.plant.floweringMonths?.let {
                            item {
                                MonthsView(
                                    title = stringResource(Res.string.flowering_months_title),
                                    months = it
                                )
                            }
                        }

                        if (state.plant.isAFruitPlant == true) {
                            state.plant.fruitingMonths?.let {
                                item {
                                    MonthsView(
                                        title = stringResource(Res.string.fruiting_months_title),
                                        months = it
                                    )
                                }
                            }
                        }

                        state.plant.exposure?.let {
                            item {
                                Exposures(
                                    it,
                                    state.plant.currentExposure,
                                    state.plant.exposureAdvise ?: "",
                                    state.plant.hardiness ?: 0.0f
                                )
                            }
                        }

                        state.plant.soilMoisture?.let {
                            item {
                                SoilMoistureView(it)
                            }
                        }

                        // end of the view
                        item {
                            Spacer(Modifier.height(16.dp))
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun PlantDescription(plant: PlantDatabaseModel) {
    Container {
        TextRow(
            title = stringResource(Res.string.common_name_title),
            value = plant.commonName
        )

        plant.scientificName?.let {
            TextRow(
                title = stringResource(Res.string.scientific_name_title),
                value = it
            )
        }

        plant.description?.let {
            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                Title(text = stringResource(Res.string.description_title))
                TextView(text = it)
            }
        }

        if (plant.maxHeight != null && plant.maxWidth != null) {
            Size(maxHeight = plant.maxHeight, maxWidth = plant.maxWidth)
        }

        plant.pollination?.let {
            TextRow(title = stringResource(Res.string.pollination_title), value = it)
        }
    }
}

@Composable
fun MonthsView(title: String, months: List<String>) {
    Column(
        verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        Title(text = title)
        MonthsView(
            modifier = Modifier.padding(horizontal = 16.dp),
            selectedIndexes = months.map { it.toInt() }
        )
    }
}

@Composable
fun TextRow(title: String, value: String) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Title(text = title)
        TextView(text = value)
    }
}

@Composable
fun Title(text: String) {
    Text(
        text = text,
        fontWeight = FontWeight.Bold,
        style = MaterialTheme.typography.labelLarge
    )
}

@Composable
fun TextView(text: String, color: Color = Color.Unspecified) {
    Text(
        text = text,
        color = color,
        style = MaterialTheme.typography.bodyMedium
    )
}

@Composable
fun Size(maxHeight: Int, maxWidth: Int) {
    Row(
        Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(Res.drawable.baseline_height_24),
            contentDescription = stringResource(Res.string.height_title)
        )
        Text(
            modifier = Modifier.padding(end = 8.dp),
            text = "$maxHeight ${stringResource(Res.string.cm_label)}",
        )

        Icon(
            painter = painterResource(Res.drawable.arrow_range_24dp),
            contentDescription = stringResource(Res.string.width_title)
        )
        Text(
            text = "$maxWidth ${stringResource(Res.string.cm_label)}",
        )
    }
}

@Composable
fun Exposures(exposures: List<String>, currentExposure: String, advises: String, hardiness: Float) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(32.dp),
        verticalAlignment = Alignment.Top
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            ExposureColumn(
                title = stringResource(Res.string.recommended_exposures_title),
                exposures = exposures
            )
            ExposureColumn(
                title = stringResource(Res.string.current_exposures_title),
                exposures = listOf(currentExposure)
            )
            HardinessView(
                hardiness
            )
        }

        if (advises.isNotBlank()) {
            val isExposureCorrect = exposures.contains(currentExposure)
            Box(
                Modifier
                    .padding(top = 24.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .then(
                        if (isExposureCorrect) {
                            Modifier.background(MaterialTheme.colorScheme.surfaceVariant)
                        } else {
                            Modifier.background(MaterialTheme.colorScheme.errorContainer)
                        }
                    )
                    .padding(8.dp)
            ) {
                TextView(
                    color = if (isExposureCorrect) {
                        MaterialTheme.colorScheme.onSurfaceVariant
                    } else {
                        MaterialTheme.colorScheme.onErrorContainer
                    },
                    text = advises
                )
            }
        } else {
            Spacer(Modifier.weight(1f))
        }

    }
}

@Composable
fun ExposureColumn(title: String, exposures: List<String>) {
    Column(
        verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        Title(text = title)
        Row(
            modifier = Modifier
                .clip(RoundedCornerShape(16.dp))
                .background(MaterialTheme.colorScheme.surfaceVariant)
                .padding(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            ExposureRow(exposures)
        }
    }
}

@Composable
fun ExposureRow(exposures: List<String>) {
    exposures.forEach {
        val icons = when (it) {
            Exposure.SUN.name -> painterResource(Exposure.SUN.drawableRes)
            Exposure.SHADE.name -> painterResource(Exposure.SHADE.drawableRes)
            Exposure.PARTIAL_SHADE.name -> painterResource(Exposure.PARTIAL_SHADE.drawableRes)
            else -> null
        }
        val contentDescription = when (it) {
            Exposure.SUN.name -> Exposure.SUN.value
            Exposure.SHADE.name -> Exposure.SHADE.value
            Exposure.PARTIAL_SHADE.name -> Exposure.PARTIAL_SHADE.value
            else -> null
        }

        icons?.let {
            Icon(
                painter = icons,
                contentDescription = contentDescription?.let { description ->
                    stringResource(description)
                },
                modifier = Modifier.size(32.dp),
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun HardinessView(value: Float) {
    Column(
        verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        Title(text = stringResource(Res.string.hardiness_title))
        Row(
            modifier = Modifier
                .clip(RoundedCornerShape(16.dp))
                .background(MaterialTheme.colorScheme.surfaceVariant)
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(Res.drawable.device_thermostat_24dp),
                contentDescription = stringResource(Res.string.temperature_title)
            )
            Column {
                TextView(stringResource(Res.string.until_label))
                TextView("$value °C")
            }
        }
    }
}

@Composable
fun SoilMoistureView(value: String) {
    Container {
        Title(text = stringResource(Res.string.soil_moisture_title))
        TextView(text = value)
    }
}

@Composable
fun Container(content: @Composable () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .padding(16.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            content()
        }
    }
}