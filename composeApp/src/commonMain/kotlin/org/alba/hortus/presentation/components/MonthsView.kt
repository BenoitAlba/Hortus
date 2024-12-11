package org.alba.hortus.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

/**
 * @param modifier the [Modifier] to be applied to this Stepper
 * @param selectedIndexes index of the current step
 */
@Composable
fun MonthsView(
    modifier: Modifier = Modifier,
    selectedIndexes: List<Int>
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        for (
        i in 1..12
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                val selected = selectedIndexes.contains(i)
                val style = if (selected) {
                    FontWeight.Bold
                } else {
                    FontWeight.Normal
                }
                Text(
                    i.toString(),
                    fontWeight = style,
                    style = MaterialTheme.typography.labelLarge
                )
                StepperElement(isSelected = selected)
            }
        }
    }
}

/**
 * Renders a stepper element.
 *
 * @param isSelected Whether the stepper element is selected.
 */
@Composable
private fun StepperElement(isSelected: Boolean) {
    LinearProgressIndicator(
        progress = { if (isSelected) 1.0f else 0.0f },
        modifier = Modifier
            .padding(end = 2.dp)
            .width(20.dp)
            .height(8.dp),
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        trackColor = MaterialTheme.colorScheme.surfaceVariant,
    )
}