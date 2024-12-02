package org.alba.hortus.presentation.features.new

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import hortus.composeapp.generated.resources.Res
import hortus.composeapp.generated.resources.baseline_arrow_back_ios_24
import hortus.composeapp.generated.resources.baseline_close_24
import hortus.composeapp.generated.resources.baseline_cloud_24
import hortus.composeapp.generated.resources.baseline_sunny_24
import hortus.composeapp.generated.resources.baseline_sunny_snowing_24
import org.alba.hortus.presentation.components.BottomSheetValue
import org.alba.hortus.presentation.components.ReadOnlyTextField
import org.alba.hortus.presentation.components.ValuesBottomSheet
import org.jetbrains.compose.resources.painterResource

class AddPlantScreen : Screen {

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val viewModel = getScreenModel<AddPlantScreenViewModel>()
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            modifier = Modifier.fillMaxWidth(),
                            text = "Add a Plant",
                            textAlign = TextAlign.Center,
                        )
                    },
                    navigationIcon = {
                        Icon(
                            modifier = Modifier
                                .padding(start = 16.dp)
                                .clickable {
                                    navigator.pop()
                                },
                            painter = painterResource(Res.drawable.baseline_arrow_back_ios_24),
                            contentDescription = "close"
                        )
                    },
                )
            }
        ) { it ->
            Column(
                modifier = Modifier.padding(
                    top = it.calculateTopPadding() + 16.dp,
                    bottom = it.calculateBottomPadding(),
                    start = 16.dp,
                    end = 16.dp,
                )
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Column(
                    modifier = Modifier
                        .verticalScroll(rememberScrollState())
                        .weight(1f, false),
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                ) {
                    var commonName by remember { mutableStateOf("") }
                    OutlinedTextField(
                        value = commonName,
                        onValueChange = { commonName = it },
                        label = { Text("Common Name") },
                        modifier = Modifier.fillMaxWidth(),
                    )

                    var scientificName by remember { mutableStateOf("") }
                    OutlinedTextField(
                        value = scientificName,
                        onValueChange = { scientificName = it },
                        label = { Text("Scientific Name") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    var exposure by remember { mutableStateOf("") }
                    var showBottomSheet by remember { mutableStateOf(false) }

                    ReadOnlyTextField(
                        value = exposure,
                        label = "Exposure",
                        onClick = {
                            showBottomSheet = true
                        }
                    )

                    var description by remember { mutableStateOf("") }
                    OutlinedTextField(
                        modifier = Modifier.height(200.dp).fillMaxWidth(),
                        value = description,
                        onValueChange = { description = it },
                        label = { Text("Description") },
                    )

                    if (showBottomSheet) {
                        ValuesBottomSheet(
                            title = "Exposure: ",
                            values = listOf(
                                BottomSheetValue(
                                    label = "sun",
                                    icon = painterResource(Res.drawable.baseline_sunny_24)
                                ),
                                BottomSheetValue(
                                    label = "shade",
                                    icon = painterResource(Res.drawable.baseline_cloud_24)
                                ),
                                BottomSheetValue(
                                    label = "partial shade",
                                    icon = painterResource(Res.drawable.baseline_sunny_snowing_24)
                                ),
                            ),
                            onDismiss = {
                                showBottomSheet = false
                            },
                            onValueSelected = {
                                exposure = it
                            }
                        )
                    }
                }
                Button(
                    modifier = Modifier
                        .padding(vertical = 8.dp)
                        .fillMaxWidth(),
                    onClick = {

                    },
                ) {
                    Text("Add")
                }
            }
        }
    }
}