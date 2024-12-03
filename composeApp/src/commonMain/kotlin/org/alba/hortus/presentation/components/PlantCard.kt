package org.alba.hortus.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import hortus.composeapp.generated.resources.Res
import hortus.composeapp.generated.resources.baseline_cloud_24
import hortus.composeapp.generated.resources.default_plant
import org.alba.hortus.domain.model.Exposure
import org.alba.hortus.domain.model.PlantDatabaseModel
import org.jetbrains.compose.resources.painterResource

@Composable
fun PlantCard(
    plant: PlantDatabaseModel,
    plantImage: Painter? = null,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(16.dp)

        ) {

            Image(
                painter = plantImage ?: painterResource(Res.drawable.default_plant),
                contentDescription = "Image de la plante",
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(
                    text = plant.commonName,
                    fontSize = 18.sp,
                    style = MaterialTheme.typography.headlineSmall
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        painter = when (plant.currentExposure) {
                            Exposure.SUN.value -> painterResource(Exposure.SUN.drawableRes)
                            Exposure.SHADE.value -> painterResource(Exposure.SHADE.drawableRes)
                            Exposure.PARTIAL_SHADE.value -> painterResource(Exposure.PARTIAL_SHADE.drawableRes)
                            else -> painterResource(Res.drawable.baseline_cloud_24)
                        },
                        contentDescription = "Exposition",
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = "Soleil")
                }
            }
        }
    }
}