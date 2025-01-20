package org.alba.hortus.presentation.features.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.valentinilk.shimmer.shimmer
import hortus.composeapp.generated.resources.Res
import hortus.composeapp.generated.resources.download
import hortus.composeapp.generated.resources.replay_24dp
import hortus.composeapp.generated.resources.unavailableweater
import org.alba.hortus.domain.model.Forecast
import org.alba.hortus.domain.model.RequestState
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun ForecastView(viewModel: HomeScreenViewModel) {
    val uiState = viewModel.forecastUiState.collectAsState()
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp),
    ) {
        when (val state = uiState.value) {
            is RequestState.Error -> {
                ForecastError(state.message) {
                    retry(viewModel)
                }
            }

            RequestState.Loading -> {
                LoadingView()
            }

            is RequestState.Success -> {
                SuccessView(state.data) {
                    retry(viewModel)
                }
            }
        }
    }
}

@Composable
fun SuccessView(data: Forecast, onRetry: (() -> Unit)) {
    Box(
        modifier = Modifier.clickable { onRetry() }
    ) {
        Image(
            painter = painterResource(
                data.weather?.image ?: Res.drawable.unavailableweater
            ),
            contentDescription = null,
            contentScale = ContentScale.Crop,
        )
        Box(
            modifier = Modifier.fillMaxSize()
                .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f))
        ) {
            Column(
                modifier = Modifier.padding(16.dp).fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(
                    8.dp,
                    Alignment.CenterVertically
                ),
            ) {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    style = MaterialTheme.typography.labelLarge,
                    text = "${data.locality}, ${data.country}",
                    textAlign = TextAlign.Center,
                )

                Text(
                    modifier = Modifier.fillMaxWidth(),
                    style = MaterialTheme.typography.headlineSmall,
                    text = data.weather?.description?.let {
                        stringResource(it)
                    } ?: "",
                    textAlign = TextAlign.Center,
                )
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    style = MaterialTheme.typography.labelLarge,
                    text = "${data.temperatureMin}°C - ${data.temperatureMax}°C",
                    textAlign = TextAlign.Center,
                )

                Row {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        style = MaterialTheme.typography.labelLarge,
                        text = "${data.windSpeed} km/h - ${data.windDirection}",
                        textAlign = TextAlign.Center,
                    )
                }
            }
        }
    }
}

@Composable
private fun ForecastError(
    errorMessage: String,
    onRetry: (() -> Unit)
) {
    Box(
        modifier = Modifier.clickable { onRetry() }
    ) {
        Image(
            painter = painterResource(
                Res.drawable.download
            ),
            contentDescription = null,
            contentScale = ContentScale.Crop,
        )
        Box(
            modifier = Modifier.fillMaxSize()
                .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.8f))
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    style = MaterialTheme.typography.headlineSmall,
                    text = errorMessage,
                    textAlign = TextAlign.Center,
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(
                        8.dp,
                        Alignment.CenterHorizontally
                    ),
                ) {
                    Text(
                        style = MaterialTheme.typography.headlineSmall,
                        text = "Retry",
                        textAlign = TextAlign.Center,
                    )
                    Icon(
                        painter = painterResource(Res.drawable.replay_24dp),
                        contentDescription = null
                    )
                }
            }
        }
    }
}

@Composable
private fun LoadingView() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .shimmer(),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .background(MaterialTheme.colorScheme.surface)
        )
    }
}

private fun retry(viewModel: HomeScreenViewModel) =
    viewModel.sendEvent(HomeScreenViewModel.HomeUIEvent.RetryForecast)