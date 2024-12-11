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
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import coil3.compose.AsyncImage
import hortus.composeapp.generated.resources.Res
import hortus.composeapp.generated.resources.arrow_range_24dp
import hortus.composeapp.generated.resources.baseline_arrow_back_ios_24
import hortus.composeapp.generated.resources.baseline_height_24
import hortus.composeapp.generated.resources.default_plant
import hortus.composeapp.generated.resources.device_thermostat_24dp
import org.alba.hortus.domain.model.Exposure
import org.alba.hortus.domain.model.PlantDatabaseModel
import org.alba.hortus.presentation.components.MonthsView
import org.alba.hortus.presentation.features.home.HomeScreen
import org.jetbrains.compose.resources.painterResource

@OptIn(ExperimentalMaterial3Api::class)
class PlantDetailsScreen(
    private val id: Long
) : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val viewModel = getScreenModel<PlantDetailsScreenViewModel>()
        viewModel.getPlantDetails(id)
        val uiState = viewModel.uiState.collectAsState()

        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            modifier = Modifier.fillMaxWidth(),
                            text = "Plant Details",
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
                    }
                )
            }
        ) {
            when (val state = uiState.value) {
                is PlantDetailsScreenViewModel.PlantDetailsUIState.Error -> {

                }

                PlantDetailsScreenViewModel.PlantDetailsUIState.Loading -> {

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
                                contentDescription = "Image de la plante",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.clip(CircleShape).size(200.dp)
                            )
                        }

                        item {
                            PlantDescription(state.plant)
                        }

                        state.plant.floweringMonths?.let {
                            item {
                                MonthsView(title = "Flowering Months:", months = it)
                            }
                        }

                        if (state.plant.isAFruitPlant == true) {
                            state.plant.fruitingMonths?.let {
                                item {
                                    MonthsView(title = "Fruiting Months:", months = it)
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
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .padding(16.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            TextRow(title = "Common Name:", value = plant.commonName)

            plant.scientificName?.let {
                TextRow(title = "Scientific Name:", value = it)
            }

            plant.description?.let {
                Column(
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                ) {
                    Title(text = "Description:")
                    TextView(text = it)
                }
            }

            if (plant.maxHeight != null && plant.maxWidth != null) {
                Size(maxHeight = plant.maxHeight, maxWidth = plant.maxWidth)
            }
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
            contentDescription = "Height"
        )
        Text(
            modifier = Modifier.padding(end = 8.dp),
            text = "$maxHeight cm",
        )

        Icon(
            painter = painterResource(Res.drawable.arrow_range_24dp),
            contentDescription = "Width"
        )
        Text(
            text = "$maxWidth cm",
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
            ExposureColumn(title = "Recommended Exposures:", exposures = exposures)
            ExposureColumn(title = "Current Exposures:", exposures = listOf(currentExposure))
            HardinessView(
                hardiness
            )
        }

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
                .padding(start = 16.dp)
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
                contentDescription = contentDescription,
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
        Title(text = "Hardiness:")
        Row(
            modifier = Modifier
                .padding(start = 16.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(MaterialTheme.colorScheme.surfaceVariant)
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(Res.drawable.device_thermostat_24dp),
                contentDescription = "Température"
            )
            Column {
                Text("util")
                Text("$value °C")
            }
        }
    }
}