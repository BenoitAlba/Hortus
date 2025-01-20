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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import hortus.composeapp.generated.resources.Res
import hortus.composeapp.generated.resources.baseline_close_24
import hortus.composeapp.generated.resources.baseline_location_pin_24
import hortus.composeapp.generated.resources.baseline_search_24
import hortus.composeapp.generated.resources.clear_button_content_description
import hortus.composeapp.generated.resources.close_button_content_description
import hortus.composeapp.generated.resources.home2
import hortus.composeapp.generated.resources.location_icon_content_description
import hortus.composeapp.generated.resources.no_results_found
import hortus.composeapp.generated.resources.search_for_location_title
import org.alba.hortus.domain.model.LocationResult
import org.alba.hortus.presentation.features.location.component.LocationCard
import org.alba.hortus.presentation.features.location.component.SearchInputField
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

class LocationScreen : Screen {
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())
        val viewModel = koinScreenModel<LocationScreenViewModel>()
        val navigator = LocalNavigator.currentOrThrow

        Scaffold(
            topBar = {
                TopAppBar(
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.Transparent,
                    ),
                    title = {
                    },
                    navigationIcon = {
                        IconButton(onClick = { navigator.pop() }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = stringResource(Res.string.close_button_content_description)
                            )
                        }
                    },
                    scrollBehavior = scrollBehavior
                )
            }
        ) {
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
                        contentDescription = stringResource(Res.string.location_icon_content_description)
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
                Text(stringResource(Res.string.no_results_found))
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
                    Text(stringResource(Res.string.search_for_location_title), style = MaterialTheme.typography.titleMedium)

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
                        contentDescription = stringResource(Res.string.clear_button_content_description)
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
                    style = MaterialTheme.typography.headlineSmall,
                    textAlign = TextAlign.Center,
                )
            }
        }
    }
}