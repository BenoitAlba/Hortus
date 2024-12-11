package org.alba.hortus.presentation.features.new

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.preat.peekaboo.image.picker.toImageBitmap
import hortus.composeapp.generated.resources.Res
import hortus.composeapp.generated.resources.baseline_arrow_back_ios_24
import hortus.composeapp.generated.resources.baseline_cloud_24
import hortus.composeapp.generated.resources.baseline_sunny_24
import hortus.composeapp.generated.resources.baseline_sunny_snowing_24
import kotlinx.coroutines.launch
import org.alba.hortus.domain.model.Exposure
import org.alba.hortus.presentation.components.BottomSheetValue
import org.alba.hortus.presentation.components.CameraView
import org.alba.hortus.presentation.components.ImagePicker
import org.alba.hortus.presentation.components.ObserveAsEvents
import org.alba.hortus.presentation.components.ReadOnlyTextField
import org.alba.hortus.presentation.components.ValuesBottomSheet
import org.alba.hortus.presentation.features.home.HomeScreen
import org.jetbrains.compose.resources.painterResource

class AddPlantScreen : Screen {

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val viewModel = getScreenModel<AddPlantScreenViewModel>()
        val snackBarHostState = remember { SnackbarHostState() }
        val scope = rememberCoroutineScope()

        val uiEffect = viewModel.uiEffect
        var showLoading by remember { mutableStateOf(false) }

        ObserveAsEvents(uiEffect) { event ->
            when (event) {
                is AddPlantScreenUIEffect.NavigateToHome -> {
                    navigator.replaceAll(HomeScreen(event.message))
                }

                is AddPlantScreenUIEffect.ShowToast -> {
                    scope.launch {
                        showLoading = false
                        snackBarHostState.showSnackbar(message = event.message)
                    }
                }
            }
        }

        var showCamera by remember { mutableStateOf(false) }
        var imageBitmap by remember { mutableStateOf<ImageBitmap?>(null) }

        if (showCamera) {
            CameraView(
                onBack = {
                    showCamera = false
                },
                onCapture = { byteArray ->
                    byteArray?.let {
                        imageBitmap = it.toImageBitmap()
                        viewModel.imageByteArray = it
                    }
                    showCamera = false
                }
            )
        } else {
            Scaffold(
                topBar = {
                    TopAppBar(
                        title = {
                            Text(
                                modifier = Modifier.fillMaxWidth(),
                                text = "Add a Plant",
                                style = MaterialTheme.typography.headlineMedium,
                                textAlign = TextAlign.Center,
                            )
                        },
                        navigationIcon = {
                            Icon(
                                modifier = Modifier
                                    .padding(start = 16.dp)
                                    .clickable {
                                        navigator.push(HomeScreen())
                                    },
                                painter = painterResource(Res.drawable.baseline_arrow_back_ios_24),
                                contentDescription = "close"
                            )
                        },
                    )
                },
                snackbarHost = {
                    SnackbarHost(hostState = snackBarHostState)
                },
            ) { it ->
                var commonName by remember { mutableStateOf("") }
                var scientificName by remember { mutableStateOf("") }
                var exposure by remember { mutableStateOf("") }
                var description by remember { mutableStateOf("") }

                Box(
                    modifier = Modifier.fillMaxSize()
                ) {
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

                            ImagePicker(
                                modifier = Modifier.padding(bottom = 16.dp),
                                image = imageBitmap,
                                onStartCamera = {
                                    showCamera = true
                                },
                                onImageSelected = { image, byteArrayImage ->
                                    imageBitmap = image
                                    viewModel.imageByteArray = byteArrayImage
                                }
                            )

                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(MaterialTheme.colorScheme.surfaceVariant)
                                    .padding(16.dp)
                            ) {
                                Text(
                                    style = MaterialTheme.typography.labelMedium,
                                    text = "'common name' and 'exposure' fields are required. All other empty fields will be auto-completed by AI."
                                )
                            }

                            OutlinedTextField(
                                value = commonName,
                                onValueChange = { commonName = it },
                                label = { Text("Common Name") },
                                modifier = Modifier.fillMaxWidth(),
                            )

                            OutlinedTextField(
                                value = scientificName,
                                onValueChange = { scientificName = it },
                                label = { Text("Scientific Name") },
                                modifier = Modifier.fillMaxWidth()
                            )

                            var showBottomSheet by remember { mutableStateOf(false) }

                            ReadOnlyTextField(
                                value = Exposure.getEnumForName(exposure)?.value ?: "",
                                label = "Exposure",
                                onClick = {
                                    showBottomSheet = true
                                }
                            )

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
                                            label = Exposure.SUN.value,
                                            description = Exposure.SUN.description,
                                            icon = painterResource(Exposure.SUN.drawableRes),
                                            value = Exposure.SUN.name,
                                        ),
                                        BottomSheetValue(
                                            label = Exposure.SHADE.value,
                                            description = Exposure.SHADE.description,
                                            icon = painterResource(Exposure.SHADE.drawableRes),
                                            value = Exposure.SHADE.name,
                                        ),
                                        BottomSheetValue(
                                            label = Exposure.PARTIAL_SHADE.value,
                                            description = Exposure.PARTIAL_SHADE.description,
                                            icon = painterResource(Exposure.PARTIAL_SHADE.drawableRes),
                                            value = Exposure.PARTIAL_SHADE.name,
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
                                showLoading = true
                                viewModel.sendEvent(
                                    AddUIEvent.AddClicked(
                                        commonName = commonName,
                                        scientificName = scientificName,
                                        description = description,
                                        exposure = exposure
                                    )
                                )
                            },
                        ) {
                            Text("Add")
                        }
                    }
                    if (showLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(64.dp).align(Alignment.Center),
                        )

                    }
                }
            }
        }
    }
}