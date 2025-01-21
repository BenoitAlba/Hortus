package org.alba.hortus.presentation.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import hortus.composeapp.generated.resources.Res
import hortus.composeapp.generated.resources.baseline_cloud_24
import hortus.composeapp.generated.resources.default_plant
import hortus.composeapp.generated.resources.plant_card_temperature_too_low
import org.alba.hortus.domain.model.Exposure
import org.alba.hortus.domain.model.PlantListDataModel
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PlantCard(
    plant: PlantListDataModel,
    onClick: () -> Unit,
    onLongClick: () -> Unit
) {
    val haptics = LocalHapticFeedback.current

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .combinedClickable(
                onClick = {
                    onClick()
                },
                onLongClick = {
                    haptics.performHapticFeedback(HapticFeedbackType.LongPress)
                    onLongClick()
                }
            ),
        colors = if (plant.isTemperatureTooLow) {
            CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.errorContainer,
            )
        } else {
            CardDefaults.cardColors()
        },
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(16.dp)

        ) {
            AsyncImage(
                model = plant.image ?: Res.drawable.default_plant,
                placeholder = painterResource(Res.drawable.default_plant),
                error = painterResource(Res.drawable.default_plant),
                fallback = painterResource(Res.drawable.default_plant),
                contentDescription = "Image de la plante",
                contentScale = ContentScale.Crop,
                modifier = Modifier.clip(CircleShape).size(80.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(
                    text = plant.commonName,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.titleMedium
                )
                plant.scientificName?.let {
                    Text(
                        text = plant.scientificName,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        painter = when (plant.currentExposure) {
                            Exposure.SUN.name -> painterResource(Exposure.SUN.drawableRes)
                            Exposure.SHADE.name -> painterResource(Exposure.SHADE.drawableRes)
                            Exposure.PARTIAL_SHADE.name -> painterResource(Exposure.PARTIAL_SHADE.drawableRes)
                            else -> painterResource(Res.drawable.baseline_cloud_24)
                        },
                        contentDescription = "Exposition",
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = when (plant.currentExposure) {
                            Exposure.SUN.name -> stringResource(Exposure.SUN.value)
                            Exposure.SHADE.name -> stringResource(Exposure.SHADE.value)
                            Exposure.PARTIAL_SHADE.name -> stringResource(Exposure.PARTIAL_SHADE.value)
                            else -> ""
                        },
                        style = MaterialTheme.typography.labelLarge
                    )
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    plant.hardiness?.let {
                        Text(
                            text = "$it C°",
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            style = MaterialTheme.typography.labelLarge,
                        )
                    }


                    if (plant.isTemperatureTooLow) {
                        Text(
                            text = stringResource(Res.string.plant_card_temperature_too_low),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.Bold,
                        )
                    }
                }
            }
        }
    }
}