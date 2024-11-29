package org.alba.hortus.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import io.ktor.websocket.Frame

@Composable
fun ReadOnlyTextField(
    modifier: Modifier = Modifier,
    value: String,
    label: String,
    onClick: () -> Unit,
) {
    Box(
        modifier = modifier,
    ) {
        OutlinedTextField(
            value = value,
            onValueChange = {},
            modifier = Modifier
                .fillMaxWidth(),
            label = {
                Frame.Text(
                    text = label,
                )
            },
        )
        Box(
            modifier = Modifier
                .matchParentSize()
                .alpha(0f)
                .clickable(
                    onClick = onClick,
                ),
        )
    }
}