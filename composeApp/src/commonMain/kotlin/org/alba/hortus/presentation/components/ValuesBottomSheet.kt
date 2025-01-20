package org.alba.hortus.presentation.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ValuesBottomSheet(
    onDismiss: () -> Unit,
    values: List<BottomSheetValue>,
    onValueSelected: (String) -> Unit,
    title: String? = null,
    skipPartiallyExpanded: Boolean = false,
    isCard: Boolean = false
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = skipPartiallyExpanded)

    ModalBottomSheet(
        onDismissRequest = {
            onDismiss()
        },
        sheetState = sheetState
    ) {
        Column {
            title?.let {
                Text(
                    text = it,
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }
            LazyColumn(
                contentPadding = PaddingValues(16.dp)
            ) {
                items(values.size) { index ->
                    if (isCard) {
                        CardBottomSheetItem(
                            index = index,
                            onValueSelected = {
                                onValueSelected(it)
                            },
                            values = values
                        ) {
                            onDismiss()
                        }
                    } else {
                        SimpleBottomSheetItem(
                            index = index,
                            onValueSelected = {
                                onValueSelected(it)
                            },
                            values = values
                        ) {
                            onDismiss()
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ColumnScope.SimpleBottomSheetItem(
    index: Int,
    onValueSelected: (String) -> Unit,
    values: List<BottomSheetValue>,
    onDismiss: () -> Unit
) {
    Row(
        modifier = Modifier
            .clickable {
                onValueSelected(values[index].value)
                onDismiss()
            }
            .align(Alignment.CenterHorizontally)
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        values[index].icon?.let {
            Icon(
                modifier = Modifier.align(Alignment.CenterVertically),
                painter = it,
                contentDescription = null
            )
        }
        Column(
            verticalArrangement = Arrangement.spacedBy(4.dp)

        ) {
            Text(
                values[index].label,
                style = MaterialTheme.typography.bodyLarge
            )
            values[index].description?.let {
                Text(
                    it,
                    style = MaterialTheme.typography.labelMedium
                )
            }
        }
    }
}

@Composable
fun CardBottomSheetItem(
    index: Int,
    onValueSelected: (String) -> Unit,
    values: List<BottomSheetValue>,
    onDismiss: () -> Unit
) {
    Card(
        modifier = Modifier
            .clickable {
                onValueSelected(values[index].value)
                onDismiss()
            }
            .padding(vertical = 4.dp)
            .fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(8.dp)
        ) {
            values[index].icon?.let {
                Icon(
                    modifier = Modifier.align(Alignment.CenterVertically),
                    painter = it,
                    contentDescription = null
                )
            }

            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp)

            ) {
                Text(
                    values[index].label,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.bodyLarge
                )
                values[index].description?.let {
                    Text(
                        it,
                        style = MaterialTheme.typography.labelMedium
                    )
                }
            }
        }
    }
}

@Composable
fun BottomSheetVisibility(
    modifier: Modifier = Modifier,
    visible: Boolean,
    content: @Composable AnimatedVisibilityScope.() -> Unit
) {
    AnimatedVisibility(
        modifier = modifier,
        visible = visible,
        enter = slideInVertically(
            initialOffsetY = { it },
            animationSpec = tween(durationMillis = 500)
        ),
        exit = slideOutVertically(
            targetOffsetY = { it },
            animationSpec = tween(durationMillis = 500)
        ),
        content = content,
    )
}

data class BottomSheetValue(
    val label: String,
    val description: String? = null,
    val value: String,
    val icon: Painter? = null
)