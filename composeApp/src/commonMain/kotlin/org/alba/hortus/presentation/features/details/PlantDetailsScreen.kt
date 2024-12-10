package org.alba.hortus.presentation.features.details

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel

@OptIn(ExperimentalMaterial3Api::class)
class PlantDetailsScreen(
    private val id: Long
) : Screen {
    @Composable
    override fun Content() {

        val viewModel = getScreenModel<PlantDetailsScreenViewModel>()
        viewModel.getPlantDetails(id)
        val uiState = viewModel.uiState.collectAsState()

        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            modifier = Modifier.fillMaxWidth(),
                            text = "Plant Details",
                        )
                    }
                )
            }
        ) {
            when (val state = uiState.value) {
                is PlantDetailsScreenViewModel.PlantDetailsUIState.Error -> {

                }
                PlantDetailsScreenViewModel.PlantDetailsUIState.Loading -> {

                }
                is PlantDetailsScreenViewModel.PlantDetailsUIState.Success -> {

                }
            }
        }
    }
}