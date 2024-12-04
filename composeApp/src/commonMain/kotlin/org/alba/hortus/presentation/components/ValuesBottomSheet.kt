package org.alba.hortus.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.dp
import hortus.composeapp.generated.resources.Res
import hortus.composeapp.generated.resources.baseline_cloud_24
import org.alba.hortus.ui.theme.AppTheme
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ValuesBottomSheet(
    onDismiss: () -> Unit,
    values: List<BottomSheetValue>,
    onValueSelected: (String) -> Unit,
    title: String? = null
) {
    val sheetState = rememberModalBottomSheetState()

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
                            Image(
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
            }
        }
    }
}

@Preview
@Composable
fun PreviewValuesBottomSheet() {
    AppTheme {
        ValuesBottomSheet(
            onDismiss = {

            },
            values = listOf(
                BottomSheetValue(
                    label = "Test 1",
                    value = "Test 1",
                    description = "lorem ipsum dolor sit amet",
                    icon = painterResource(Res.drawable.baseline_cloud_24)
                ),
                BottomSheetValue(
                    label = "Test 2",
                    value = "Test 2",
                    description = "lorem ipsum dolor sit amet",
                    icon = painterResource(Res.drawable.baseline_cloud_24)
                ),
                BottomSheetValue(
                    label = "Test 3",
                    value = "Test 3",
                    description = "lorem ipsum dolor sit amet",
                    icon = painterResource(Res.drawable.baseline_cloud_24)
                )
            ),
            onValueSelected = {

            },
            title = "Test"
        )
    }
}

data class BottomSheetValue(
    val label: String,
    val description: String? = null,
    val value: String,
    val icon: Painter? = null
)