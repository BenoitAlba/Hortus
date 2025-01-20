package org.alba.hortus.presentation.features.location

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinScreenModel
import hortus.composeapp.generated.resources.Res
import hortus.composeapp.generated.resources.baseline_close_24
import hortus.composeapp.generated.resources.baseline_location_pin_24
import hortus.composeapp.generated.resources.baseline_search_24
import hortus.composeapp.generated.resources.home2
import org.alba.hortus.domain.model.LocationResult
import org.alba.hortus.presentation.features.location.component.LocationCard
import org.alba.hortus.presentation.features.location.component.SearchInputField
import org.jetbrains.compose.resources.painterResource

class LocationScreen : Screen {
    @Composable
    override fun Content() {
        val viewModel = koinScreenModel<LocationScreenViewModel>()
        Scaffold {
            Column {
                Image(
                    modifier = Modifier
                        .clip(
                            RoundedCornerShape(
                                bottomStart = 16.dp,
                                bottomEnd = 16.dp
                            )
                        )
                        .height(300.dp),
                    painter = painterResource(Res.drawable.home2),
                    contentDescription = null,
                    contentScale = ContentScale.Crop
                )
                when (val uiState = viewModel.uiState.collectAsState().value) {
                    is LocationScreenViewModel.LocationScreenUIState.LocationSelected -> {
                        LocationView(viewModel, uiState.location as LocationResult.Location)
                    }

                    else -> {
                        SearchView(viewModel, uiState)
                    }
                }

            }
        }
    }
}

@Composable
private fun SearchView(
    viewModel: LocationScreenViewModel,
    uiState: LocationScreenViewModel.LocationScreenUIState
) {
    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,

            ) {
            val inputText = viewModel.inputText.collectAsState().value

            SearchInputField(
                modifier = Modifier.weight(1f),
                inputText = inputText,
                onSearchInputChanged = { input -> viewModel.updateInput(input) },
                onClearInputClicked = { viewModel.clearInput() }
            )
            IconButton(
                onClick = {
                    viewModel.sendEvent(LocationScreenViewModel.LocationScreenUIEvent.GeoLocate)
                },
                content = {
                    Icon(
                        painter = painterResource(Res.drawable.baseline_location_pin_24),
                        contentDescription = "Localized description"
                    )
                }
            )
        }

        when (uiState) {
            is LocationScreenViewModel.LocationScreenUIState.SearchResultsFetched -> {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                ) {
                    items(uiState.results.size) { index ->
                        when (val value = uiState.results[index]) {
                            is LocationResult.Error -> {

                            }

                            is LocationResult.Location -> {
                                LocationCard(value) {
                                    viewModel.sendEvent(
                                        LocationScreenViewModel.LocationScreenUIEvent.LocationSelected(
                                            it
                                        )
                                    )
                                }
                            }
                        }
                    }
                }
            }

            LocationScreenViewModel.LocationScreenUIState.NoResults -> {
                Text("No Results")
            }

            LocationScreenViewModel.LocationScreenUIState.IdleScreen -> {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        modifier = Modifier.size(64.dp),
                        painter = painterResource(Res.drawable.baseline_search_24),
                        contentDescription = null
                    )
                    Text("Search for a location", style = MaterialTheme.typography.titleMedium)

                }
            }

            LocationScreenViewModel.LocationScreenUIState.Loading -> {

            }

            is LocationScreenViewModel.LocationScreenUIState.LocationSelected -> {
                Text(uiState.location.toString())
            }

            else -> {}
        }
    }
}

@Composable
private fun LocationView(viewModel: LocationScreenViewModel, location: LocationResult.Location) {
    Card(
        modifier = Modifier.padding(16.dp),
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(2.dp),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
            ) {
                IconButton(onClick = {
                    viewModel.sendEvent(LocationScreenViewModel.LocationScreenUIEvent.ClearLocation)
                }) {
                    Icon(
                        painter = painterResource(Res.drawable.baseline_close_24),
                        contentDescription = "Clear"
                    )
                }
            }
            Column(
                modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 16.dp)
            ) {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = location.country ?: "",
                    style = MaterialTheme.typography.headlineMedium,
                    textAlign = TextAlign.Center,
                )

                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = location.locality ?: "",
                    style = MaterialTheme.typography.headlineMedium,
                    textAlign = TextAlign.Center,
                )
            }

        }

    }
}